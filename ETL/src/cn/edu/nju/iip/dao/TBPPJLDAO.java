package cn.edu.nju.iip.dao;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.model.TBPPJL;
import cn.edu.nju.iip.util.CommonUtil;

/**
 * 公路水运建设市场从业企业 通报批评表DAO
 * @author wangqiang
 *
 */
public class TBPPJLDAO extends DAO{
	
private static final Logger logger = LoggerFactory.getLogger(TBPPJLDAO.class);
private static CORPINFODAO dao = new CORPINFODAO();
	

    @Override
	public boolean saveData(RawHtml raw_html) {
		try{
			TBPPJL Data = new TBPPJL();
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setcDate(new Date());
			Data.setuDate(Data.getcDate());
			Data.setCorp_Id(dao.fetchID(raw_html.getUnitName()));
			Data.setIndustry(raw_html.getIndustry());
			Data.setUnit(raw_html.getSource());
			Data.setData_Source(raw_html.getUrl());
			Data.setWebName(raw_html.getSource());
			Data.setCorp_Name(raw_html.getUnitName());
			Data.setWebContent(raw_html.getContent());
			Data.setWebLevel(raw_html.getType());
			Data.setCorp_Name(raw_html.getUnitName());
			extractField(Data);
			if(!abstractContent(Data)) {
				return false;
			}
			begin();
			getSession().save(Data);
			commit();
			return true;
		}catch(Exception e) {
			rollback();
			logger.error("TBPPJLDAO saveData failed!",e);
		}
		return false;
	}
    
    /**
	 * 抽取正文字段
	 * @param Data
	 */
	public void extractField(TBPPJL Data) {
		String content = Data.getWebContent();

		String pdate = "";
		String unit = "";
		String code = "";
		
		Matcher match =  null;
		
		
		Pattern codePattern = Pattern.compile("([\u4e00-\u9fa5]{2,6})(［|〔|（|\\[|\\(|【)[0-9]{4}(］|）|\\)|\\]|】|〕)(.?[0-9]{1,4}.?)(号?)");
		match = codePattern.matcher(content);
		
		if(match.find()){
			code =  match.group();			
		}
		Data.setCode(code);
		
		Pattern datePattern = Pattern.compile("((20)[0-9]{2}(-|/|-)[0-9]{1,2}(-|-|/)[0-9]{1,2})");			
		match = datePattern.matcher(content);
		
		if(match.find()){
			pdate = match.group();
		}else{
			Pattern datePattern2 = Pattern.compile("(([0-9]{4})年[0-9]{1,2}月[0-9]{1,2}日)");
			match = datePattern2.matcher(content);
			if(match.find()){
				pdate = match.group();
			}
			
		}
		
		pdate = pdate.replaceAll("-", "/").replaceAll("年", "/").replaceAll("月", "/").replaceAll("日", "");
		if(pdate.length()<5) {
			try {
				pdate = ContentExtractor.getNewsByUrl(Data.getData_Source()).getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Data.setpDate(pdate);
		
		System.out.println(Data.getpDate());
		
		Pattern pattern = Pattern.compile("([\u4e00-\u9fa5]{1,20}(会|室|厅|站|府|局|部|院|所|处))(\\s| | )+([0-9]{4}|(二...))年.{1,2}月.{1,3}日");
		match = pattern.matcher(content);
		
		String str = "";
		while(match.find()){
			str = match.group();
		}
		
		if(str.length() < 10){
			Pattern datePattern1 = Pattern.compile("([0-9]{4}-[0-9]{2}-[0-9]{2})|(([0-9]{4}|(二...))年.{1,2}月.{1,3}日)");
			match = datePattern1.matcher(content);
			if(match.find()){
				pdate = match.group();
			}		
		}else{

			Pattern datePattern1 = Pattern.compile("([0-9]{4}|(二...))年.{1,2}月.{1,3}日");
			match = datePattern1.matcher(str);
			if(match.find()){
				pdate = match.group();
			}
			unit = str.replace(pdate, "").trim();
		}	
		
		if(!unit.equals("")) {
			Data.setUnit(unit);
		}
	}
	
	/**
	 * 正文摘要
	 * @param Data
	 */
	public boolean abstractContent(TBPPJL Data) {
		String content = Data.getWebContent();
		String[] sentences = content.split("[\\s。？]+");
		for (String sentence : sentences) {
			if (sentence.contains("关于")&&sentence.contains("通报")) {
				int index = sentence.indexOf("关于");
				int index2 = sentence.indexOf("的通报");
				sentence = sentence.substring(index,index2+3);
				Data.setWebTitle(sentence);
				if (sentence.length() > 100) {
					sentence = sentence.substring(0, 100);
				}
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		ConstructComETL road_HJQK_etl = new ConstructComETL("水运建设企业","批评",new TBPPJLDAO());
		Thread thread = new Thread(road_HJQK_etl);
		thread.start();
	}

}
