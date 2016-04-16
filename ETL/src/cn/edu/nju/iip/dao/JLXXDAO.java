package cn.edu.nju.iip.dao;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.JLXX;
import cn.edu.nju.iip.model.RawHtml;

public class JLXXDAO extends DAO{
	
	private static final Logger logger = LoggerFactory.getLogger(JLXXDAO.class);

	@Override
	public boolean saveData(RawHtml raw_html) {
		try {
			JLXX Data = new JLXX();
			Data.setUnit(raw_html.getSource());
			Data.setcDate(new Date());// 录入时间
			Data.setpDate(raw_html.getCrawltime());
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
			logger.error("TBBZDAO saveData failed!", e);
		}
		return false;
	}
	
	/**
	 * 抽取正文字段
	 * @param Data
	 */
	public void extractField(JLXX Data) {
		String content = Data.getContent();
		
		String name = "";
		Matcher match =  null;
		
		Pattern award = Pattern.compile("(国家|中国|全国|河北省|山西省|辽宁省|吉林省|黑龙江省|江苏省|浙江省|安徽省|福建省|江西省|山东省|河南省|湖北省|湖南省|广东省|海南省|四川省|贵州省|云南省|陕西省|甘肃省|青海省|台湾省|内蒙古自治区|广西壮族自治区|西藏自治区|宁夏回族自治区|新疆维吾尔自治区|北京市|天津市|上海市|重庆市|香港特别行政区|澳门特别行政区|[0-9]{4}年度)([\u4e00-\u9fa5“”]*?)(奖)");
		match = award.matcher(content);
		
		while(match.find()){
			String findString =  match.group();
			if (findString.contains("年度")) {
				findString = findString.substring(6);
			}
			if (findString.length() < 25) {
				name = findString;
				break;
			}
		}
		Data.setName(name);		
		
	}
	
	/**
	 * 抽取正文字段
	 * @param Data
	 */
	public boolean abstractContent(JLXX Data) {
		String content = Data.getContent();
		String[] sentences = content.split("[\\s。？]+");
		for (String sentence : sentences) {
			if (sentence.contains("奖")&&(sentence.contains("关于")||sentence.contains("名单")||sentence.contains("决定")||sentence.contains("通知"))) {
				if(sentence.contains("关于")) {
					int index = sentence.indexOf("关于");
					sentence = sentence.substring(index);
				}
				if (sentence.length() > 50) {
					sentence = sentence.substring(0, 50);
				}
				sentence = sentence.replace(".doc", "");
//				Data.setName(sentence);
//				logger.info("sentence="+sentence);
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		ConstructComETL road_JLXX_etl = new ConstructComETL("道路运输企业", "获奖",new JLXXDAO());
		Thread thread = new Thread(road_JLXX_etl);
		thread.start();
	}

}