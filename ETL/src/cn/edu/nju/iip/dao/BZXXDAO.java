package cn.edu.nju.iip.dao;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.BZXX;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.util.CommonUtil;
import cn.edu.nju.iip.util.MyContentExtractor;

public class BZXXDAO extends DAO {
	private static final Logger logger = LoggerFactory.getLogger(BZXXDAO.class);
	
	private static CORPINFODAO dao = new CORPINFODAO();

	@Override
	public boolean saveData(RawHtml raw_html) {
		try {
			BZXX Data = new BZXX();
			String pubUnit = MyContentExtractor.getPubUnit(raw_html.getHtml());//发布单位
			if(pubUnit.equals("")) pubUnit = raw_html.getSource();
			String pdate = MyContentExtractor.getPdate(raw_html.getHtml());//表彰时间
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setUnit(pubUnit);
			Data.setpDate(pdate);
			Data.setcDate(new Date());// 录入时间
			Data.setuDate(Data.getcDate());
			
			Data.setCorp_Id(dao.fetchID(raw_html.getUnitName()));
			Data.setCorp_Name(raw_html.getUnitName());
			Data.setData_Source(raw_html.getUrl());
			
			Data.setWebContent(CommonUtil.getCleanContent(ContentExtractor.getContentByHtml(raw_html.getHtml())));
			Data.setWebLevel(raw_html.getType());
			Data.setWebName(raw_html.getSource());
			Data.setWebTitle(raw_html.getTitle());
			
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
	 * 
	 * @param Data
	 */
	public boolean abstractContent(BZXX Data) {
		String content = Data.getWebContent();
		String[] sentences = content.split("\\s+");
		for (String sentence : sentences) {
			if (sentence.contains("关于表彰")&&sentence.contains("的")) {
				sentence = sentence.trim().replaceAll("[？>]", "");
				int index = sentence.indexOf("关于表彰");
				int index2 = sentence.indexOf("的");
				try {
					sentence = sentence.substring(index+4,index2);
				}catch(Exception e) {
					continue;
				}
				if (sentence.length() > 50) {
					sentence = sentence.substring(0, 50);
				}
				Data.setBz_Name(sentence);
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
