package cn.edu.nju.iip.dao;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.model.TBBZ;
import cn.edu.nju.iip.util.CommonUtil;

/**
 * 公路水运建设市场从业企业 通报表彰表DAO
 * @author wangqiang
 *
 */
public class TBBZDAO extends DAO{
	
private static final Logger logger = LoggerFactory.getLogger(TBBZDAO.class);
private static CORPINFODAO dao = new CORPINFODAO();
	

    @Override
	public boolean saveData(RawHtml raw_html) {
		try{
			TBBZ Data = new TBBZ();
//			Data.setIssue_Date(raw_html.getCrawltime());
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setcDate(new Date());
			Data.setuDate(Data.getcDate());
			Data.setIndustry(raw_html.getIndustry());
			Data.setUnit(raw_html.getSource());
			Data.setData_Source(raw_html.getUrl());
			Data.setCorp_Id(dao.fetchID(raw_html.getUnitName()));
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
			logger.error("TBBZDAO saveData failed!",e);
		}
		return false;
	}
    
    
    /**
	 * 抽取正文字段
	 * @param Data
	 */
	public void extractField(TBBZ Data) {
		String content = Data.getWebContent();

		String pdate = "";
		String code = "";
		
		Matcher match =  null;
		
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Data.setIssue_Date(pdate);
		
		System.out.println(Data.getIssue_Date());
		
		Pattern codePattern = Pattern.compile("([\u4e00-\u9fa5]{2,6})(［|〔|（|\\[|\\(|【)[0-9]{4}(］|）|\\)|\\]|】|〕)(.?[0-9]{1,4}.?)(号?)");
		match = codePattern.matcher(content);
		
		if(match.find()){
			code =  match.group();			
		}
		Data.setCode(code);
		
	}
	
	/**
	 * 正文摘要
	 * @param Data
	 */
	public boolean abstractContent(TBBZ Data) {
		String content = Data.getWebContent();
		String[] sentences = content.split("\\s+");
		for (String sentence : sentences) {
			if (sentence.contains("关于表彰")&&sentence.contains("的")) {
				sentence = sentence.trim().replaceAll("[？>]", "");
				int index = sentence.indexOf("关于表彰");
				int index2 = sentence.indexOf("的");
				try {
					sentence = sentence.substring(index,index2+3);
				}catch(Exception e) {
					continue;
				}
				if (sentence.length() > 50) {
					sentence = sentence.substring(0, 50);
				}
				Data.setWebTitle(sentence);
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		ConstructComETL road_TBBZ_etl = new ConstructComETL("水运建设企业","表彰",new TBBZDAO());
		Thread thread = new Thread(road_TBBZ_etl);
		thread.start();
	}

}
