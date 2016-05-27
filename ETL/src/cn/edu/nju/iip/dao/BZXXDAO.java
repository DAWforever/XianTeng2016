package cn.edu.nju.iip.dao;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.BZXX;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.util.CommonUtil;

public class BZXXDAO extends DAO {
	private static final Logger logger = LoggerFactory.getLogger(BZXXDAO.class);
	
	private static CORPINFODAO dao = new CORPINFODAO();

	@Override
	public boolean saveData(RawHtml raw_html) {
		try {
			BZXX Data = new BZXX();
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setUnit(raw_html.getSource());
			Data.setcDate(new Date());// 录入时间
			Data.setuDate(Data.getcDate());
			Data.setContent(raw_html.getContent());
			
			Data.setCorp_Id(dao.fetchID(raw_html.getUnitName()));
			Data.setCorp_Name(raw_html.getUnitName());
			Data.setData_Source(raw_html.getUrl());
			Data.setType(raw_html.getType());
			
			Data.setWebContent(raw_html.getContent());
			Data.setWebLevel(raw_html.getType());
			Data.setWebName(raw_html.getSource());
			
			System.out.println(raw_html.getUrl());
			
			extractField(Data);
			if(!abstractContent(Data)) {
				return false;
			}
			begin();
			getSession().save(Data);
			commit();
			return true;
		} catch (Exception e) {
			rollback();
			logger.error("BZXXDAO saveData failed!", e);
		}
		return false;
	}

	/**
	 * 抽取正文字段
	 * @param Data
	 */
	public void extractField(BZXX Data) {

		String content = Data.getWebContent();

		String pdate = "";
		String unit = "";
		
		Matcher match =  null;
		
		Pattern datePattern = Pattern.compile("(((^[0-9]){1})(20)[0-9]{2}(-|/|-)[0-9]{1,2}(-|-|/)[0-9]{1,2})");			
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
		
		
		String year = "";
		String month = "";
		String day = "";
		
		int index = 0;
		
		while(index < pdate.length()){
			if(pdate.charAt(index) <= '9' && pdate.charAt(index) >= '0'){
				year += pdate.charAt(index);
				index ++;
			}else{
				break;
			}
		}
		index ++;
		
		while(index < pdate.length()){
			if(pdate.charAt(index) <= '9' && pdate.charAt(index) >= '0'){
				month += pdate.charAt(index);
				index ++;
			}else{
				break;
			}
		}
		index++;
		
		while(index < pdate.length()){
			if(pdate.charAt(index) <= '9' && pdate.charAt(index) >= '0'){
				day += pdate.charAt(index);
				index ++;
			}else{
				break;
			}
		}
		
		if(Integer.parseInt(year) <= 2030 && 
		   Integer.parseInt(year) >= 1990 && 
     	   Integer.parseInt(month) >= 1 && 
     	   Integer.parseInt(month) <= 12 &&	
     	   Integer.parseInt(day) >= 1 &&
     	   Integer.parseInt(day) <= 31
		){
			System.out.println(year+"/"+month+"/"+day);
			Data.setpDate(year+"/"+month+"/"+day);
		}
				
		
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
			Pattern unitPattern = Pattern.compile("([\u4e00-\u9fa5])+");
			match = unitPattern.matcher(unit);
			
			if(match.find()){
				unit = match.group();
				
			}
			if(unit.length() <= 25){
				Data.setUnit(unit);
			}
		}

		System.out.println(unit);
	}

	
	
	/**
	 * 抽取正文字段
	 * 
	 * @param Data
	 */
	public boolean abstractContent(BZXX Data) {
		String content = Data.getContent();
		String[] sentences = content.split("\\s+");
		for (String sentence : sentences) {
			if (sentence.contains("关于表彰")&&sentence.contains("的")) {
				sentence = sentence.trim().replaceAll("[？>]", "");
				int index = sentence.indexOf("关于表彰");
				int index2 = sentence.indexOf("的");
				sentence = sentence.substring(index,index2+3);
				Data.setWebTitle(sentence);
				try {
					sentence = sentence.substring(index+4,index2);
				}catch(Exception e) {
					continue;
				}
				if (sentence.length() > 50) {
					sentence = sentence.substring(0, 50);
				}
				Data.setTitle(sentence);
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		ConstructComETL road_BZXX_etl = new ConstructComETL("道路运输企业", "表彰",new BZXXDAO());
		Thread thread = new Thread(road_BZXX_etl);
		thread.start();
	}

}
