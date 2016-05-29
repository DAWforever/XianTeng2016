package cn.edu.nju.iip.dao;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.JLXX;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.util.CommonUtil;
import cn.edu.nju.iip.util.MyContentExtractor;

public class JLXXDAO extends DAO{
	
	private static final Logger logger = LoggerFactory.getLogger(JLXXDAO.class);
	private static CORPINFODAO dao = new CORPINFODAO();

	@Override
	public boolean saveData(RawHtml raw_html) {
		try {
			JLXX Data = new JLXX();
			String pubUnit = MyContentExtractor.getPubUnit(raw_html.getHtml());//发布单位
			String pdate = MyContentExtractor.getPdate(raw_html.getHtml());//时间
			String awardName = MyContentExtractor.getAwardName(raw_html.getHtml());
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setUnit(pubUnit);
			Data.setpDate(pdate);
			Data.setName(awardName);
			
			Data.setcDate(new Date());// 录入时间
			Data.setuDate(Data.getcDate());
			Data.setData_Source(raw_html.getUrl());
			Data.setCorp_Id(dao.fetchID(raw_html.getUnitName()));
			Data.setCorp_Name(raw_html.getUnitName());
			
			Data.setWebContent(CommonUtil.getCleanContent(ContentExtractor.getContentByHtml(raw_html.getHtml())));
			Data.setWebLevel(raw_html.getType());
			Data.setWebName(raw_html.getSource());
			Data.setWebTitle(raw_html.getTitle().replaceAll("下一篇：", ""));
			
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
	public boolean abstractContent(JLXX Data) {
		String content = Data.getWebContent();
		String[] sentences = content.split("[\\s。？]+");
		for (String sentence : sentences) {
			if (sentence.contains("奖")&&(sentence.contains("关于")||sentence.contains("名单")||sentence.contains("决定")||sentence.contains("通知"))) {
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