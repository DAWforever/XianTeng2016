package cn.edu.nju.iip.dao;

import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.HJQK;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.util.CommonUtil;
import cn.edu.nju.iip.util.MyContentExtractor;

/**
 * 公路水运建设市场从业企业获奖情况表DAO
 * @author wangqiang
 *
 */
public class HJQKDAO extends DAO {
	
	private static final Logger logger = LoggerFactory.getLogger(HJQKDAO.class);
	
	private static CORPINFODAO dao = new CORPINFODAO();

	
	@Override
	public boolean saveData(RawHtml raw_html) {
		try{
			HJQK Data = new HJQK();
			String pubUnit = MyContentExtractor.getPubUnit(raw_html.getHtml());//发布单位
			String pdate = MyContentExtractor.getPdate(raw_html.getHtml());//时间
			String awardName = MyContentExtractor.getAwardName(raw_html.getHtml());
			String year = MyContentExtractor.getAwardYear(raw_html.getHtml());
			String code = MyContentExtractor.getCode(raw_html.getHtml());
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setUnit(pubUnit);
			Data.setpDate(pdate);
			Data.setName(awardName);
			Data.setYear(year);
			Data.setCode(code);
			
			Data.setCdate(new Date());// 录入时间
			Data.setUdate(Data.getCdate());
			Data.setData_Source(raw_html.getUrl());
			Data.setCorp_Id(dao.fetchID(raw_html.getUnitName()));
			Data.setCorp_Name(raw_html.getUnitName());
			Data.setIndustry(raw_html.getIndustry());
			Data.setType(MyContentExtractor.getAwardType(awardName));
			Data.setType_Name(MyContentExtractor.getAwardLevel(awardName));
			
			Data.setWebContent(CommonUtil.getCleanContent(ContentExtractor.getContentByHtml(raw_html.getHtml())));
			Data.setWebLevel(raw_html.getType());
			Data.setWebName(raw_html.getSource());
			Data.setWebTitle(raw_html.getTitle().replaceAll("下一篇：", ""));
			
			if(!abstractContent(Data)){
				return false;
			}
			begin();
			getSession().save(Data);
			commit();
			return true;
		}catch(Exception e) {
			rollback();
			logger.error("HJQKDAO saveData failed!",e);
		}
		return false;
	}
	

	
	/**
	 * 正文摘要
	 * @param Data
	 */
	public boolean abstractContent(HJQK Data) {
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
		ConstructComETL road_HJQK_etl = new ConstructComETL("水运建设企业","获奖",new HJQKDAO());
		Thread thread = new Thread(road_HJQK_etl);
		thread.start();
	}

}