package cn.edu.nju.iip.dao;

import java.util.Date;

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
			logger.info(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setUnit(raw_html.getSource());
			Data.setcDate(new Date());// 录入时间
			Data.setpDate(raw_html.getCrawltime());
			Data.setCorp_Id(raw_html.getUnitName());
			Data.setData_Source(raw_html.getUrl());
			Data.setContent(raw_html.getContent());
			if(!abstractContent(Data)) {
				return false;
			}
//			begin();
//			getSession().save(Data);
//			commit();
			return true;
		} catch (Exception e) {
			rollback();
			logger.error("TBBZDAO saveData failed!", e);
		}
		return false;
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
