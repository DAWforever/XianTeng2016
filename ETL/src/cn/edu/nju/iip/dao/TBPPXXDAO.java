package cn.edu.nju.iip.dao;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.model.TBPPXX;
import cn.edu.nju.iip.util.CommonUtil;
import cn.edu.nju.iip.util.MyContentExtractor;

public class TBPPXXDAO extends DAO{
	
	private static final Logger logger = LoggerFactory.getLogger(TBPPXXDAO.class);
	private static CORPINFODAO dao = new CORPINFODAO();

	@Override
	public boolean saveData(RawHtml raw_html) {
		try {
			TBPPXX Data = new TBPPXX();
			String pdate = MyContentExtractor.getPdate(raw_html.getHtml());//时间
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setUnit(raw_html.getSource());
			Data.setpDate(pdate);
			
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
			logger.error("TBPPXXDAO saveData failed!", e);
		}
		return false;
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
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		ConstructComETL road_TBPPXX_etl = new ConstructComETL("公路建设企业","批评",new TBPPXXDAO());
		Thread thread = new Thread(road_TBPPXX_etl);
		thread.start();
	}

}