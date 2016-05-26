package cn.edu.nju.iip.dao;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.JLXX;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.util.CommonUtil;

public class JLXXDAO extends DAO{
	
	private static final Logger logger = LoggerFactory.getLogger(JLXXDAO.class);
	private static CORPINFODAO dao = new CORPINFODAO();

	@Override
	public boolean saveData(RawHtml raw_html) {
		try {
			JLXX Data = new JLXX();
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setUnit(raw_html.getSource());
			Data.setcDate(new Date());// 录入时间
			Data.setuDate(new Date());
			Data.setCorp_Id(raw_html.getUnitName());
			Data.setData_Source(raw_html.getUrl());
			Data.setContent(raw_html.getContent());
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
		} catch (Exception e) {
			rollback();
			logger.error("JLXXDAO saveData failed!", e);
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
		String pdate = "";
		String unit = "";
		
		
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
		pdate = pdate.replaceAll("-", "/").replaceAll("年", "/").replaceAll("月", "/").replaceAll("日", "");
		Data.setpDate(pdate);
		if(!unit.equals("")) {
			Data.setUnit(unit);
		}
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
					int index2 = sentence.indexOf("的");
					try {
						sentence = sentence.substring(index,index2+3);
					}catch(Exception e) {
						continue;
					}
				}
				if (sentence.length() > 50) {
					sentence = sentence.substring(0, 50);
				}
				sentence = sentence.replace(".doc", "");
				Data.setContent(sentence);
				Data.setWebTitle(sentence);
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