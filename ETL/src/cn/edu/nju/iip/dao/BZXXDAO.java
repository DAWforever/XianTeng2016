package cn.edu.nju.iip.dao;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.BZXX;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.util.CommonUtil;

public class BZXXDAO extends DAO {
	private static final Logger logger = LoggerFactory.getLogger(BZXXDAO.class);

	@Override
	public boolean saveData(RawHtml raw_html) {
		try {
			BZXX Data = new BZXX();
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
//			Data.setUnit(raw_html.getSource());
			Data.setcDate(new Date());// 录入时间
			Data.setuDate(Data.getcDate());
//			Data.setpDate(raw_html.getCrawltime());
			Data.setCorp_Id(raw_html.getUnitName());
			Data.setData_Source(raw_html.getUrl());
			Data.setContent(raw_html.getContent());
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

		String content = Data.getContent();

		String pdate = "";
		String unit = "";
		
		Matcher match =  null;

		Pattern pattern = Pattern.compile("([\u4e00-\u9fa5]{1,20}(会|室|厅|站|府|局|部|院|所|处))(\\s| | )+([0-9]{4}|(二...))年.{1,2}月.{1,3}日");
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
		
		Data.setpDate(pdate);
		Data.setUnit(unit);
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
			if (sentence.contains("关于表彰")) {
				sentence = sentence.trim().replaceAll("[？>]", "");
				int index = sentence.indexOf("关于表彰");
				sentence = sentence.substring(index);
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
