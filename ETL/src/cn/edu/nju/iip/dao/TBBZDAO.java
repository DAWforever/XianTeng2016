package cn.edu.nju.iip.dao;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	

    @Override
	public boolean saveData(RawHtml raw_html) {
		try{
			TBBZ Data = new TBBZ();
//			Data.setIssue_Date(raw_html.getCrawltime());
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setcDate(new Date());
			Data.setuDate(Data.getcDate());
			Data.setTitle(raw_html.getTitle());
			Data.setContent(raw_html.getContent());
			Data.setIndustry(raw_html.getIndustry());
//			Data.setUnit(raw_html.getSource());
			Data.setData_Source(raw_html.getUrl());
			Data.setCorp_Id(raw_html.getUnitName());
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
		String content = Data.getContent();
		
		String code = "";
		String year = "";
		String pdate = "";
		String unit = "";
		
		Matcher match =  null;
		
		Pattern yearPattern = Pattern.compile("([0-9]{4}(年度))|(〔[0-9]{4}〕)");
		match = yearPattern.matcher(content);
		
		if(match.find()){
			String findString =  match.group();			
			year = findString.replace("〔", "").replace("〕","").replace("年度", "");							
		}
		
		Pattern codePattern = Pattern.compile("([\u4e00-\u9fa5]{2,6})(［|〔|（|\\[|\\(|【)[0-9]{4}(］|）|\\)|\\]|】|〕)(.?[0-9]{1,4}.?)(号?)");
		match = codePattern.matcher(content);
		
		if(match.find()){
			code =  match.group();			
		}
		
		Data.setCode(code);
		Data.setYear(year);
		
		Pattern pattern = Pattern.compile("([\u4e00-\u9fa5]{1,20}(会|室|厅|站|府|局|部|院|所|处))(\\s| | )+([0-9]{4}|(二...))年.{1,2}月.{1,3}日");
		match = pattern.matcher(content);
		
		String str = "";
		while(match.find()){
			str = match.group();
		}
		
		if(str.length() < 10){
			Pattern datePattern = Pattern.compile("([0-9]{4}-[0-9]{2}-[0-9]{2})|(([0-9]{4}|(二...))年.{1,2}月.{1,3}日)");
			match = datePattern.matcher(content);
			if(match.find()){
				pdate = match.group();
			}		
		}else{

			Pattern datePattern = Pattern.compile("([0-9]{4}|(二...))年.{1,2}月.{1,3}日");
			match = datePattern.matcher(str);
			if(match.find()){
				pdate = match.group();
			}
			unit = str.replace(pdate, "").trim();
		}
		
		Data.setIssue_Date(pdate);
		Data.setUnit(unit);
		
	}
	
	/**
	 * 正文摘要
	 * @param Data
	 */
	public boolean abstractContent(TBBZ Data) {
		
		String content = Data.getContent();
		String[] sentences = content.split("[\\s|。]+");
		for (int i=0;i<sentences.length;i++) {
			String sentence = sentences[i];
			if (sentence.contains("关于表彰")) {
				sentence = sentence.trim().replaceAll("[？>]", "");
				int index = sentence.indexOf("关于表彰");
				sentence = sentence.substring(index);
				if (sentence.length() > 100) {
					sentence = sentence.substring(0, 100);
				}
				Data.setTitle(sentence);
				Data.setContent(sentence);
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
