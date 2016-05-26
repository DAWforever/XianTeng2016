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
import cn.edu.nju.iip.model.TBPPXX;
import cn.edu.nju.iip.util.CommonUtil;

public class TBPPXXDAO extends DAO{
	
	private static final Logger logger = LoggerFactory.getLogger(TBPPXXDAO.class);
	private static CORPINFODAO dao = new CORPINFODAO();

	@Override
	public boolean saveData(RawHtml raw_html) {
		try {
			TBPPXX Data = new TBPPXX();
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setcDate(new Date());// 录入时间
			Data.setuDate(Data.getcDate());
			Data.setData_Source(raw_html.getUrl());
			Data.setCorp_Id(dao.fetchID(raw_html.getUnitName()));
			Data.setCorp_Name(raw_html.getUnitName());
			Data.setWebContent(raw_html.getContent());
			Data.setWebLevel(raw_html.getType());
			Data.setWebName(raw_html.getSource());
			Data.setUnit(raw_html.getSource());
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
			logger.error("TBPPXXDAO saveData failed!", e);
		}
		return false;
	}
	
	/**
	 * 抽取正文字段
	 * @param Data
	 */
	public void extractField(TBPPXX Data) {
		String content = Data.getWebContent();

		String pdate = "";
		
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
		Data.setpDate(pdate);
		
		System.out.println(Data.getpDate());
		
	}

	/**
	 * 抽取正文字段
	 * @param Data
	 */
	public boolean abstractContent(TBPPXX Data) {
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
				Data.setContent(sentence);
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		ConstructComETL road_TBPPXX_etl = new ConstructComETL("道路运输企业","批评",new TBPPXXDAO());
		Thread thread = new Thread(road_TBPPXX_etl);
		thread.start();
	}

}