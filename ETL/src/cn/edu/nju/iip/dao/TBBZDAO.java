package cn.edu.nju.iip.dao;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.model.TBBZ;

/**
 * 公路水运建设市场从业企业 通报表彰表DAO
 * @author wangqiang
 *
 */
public class TBBZDAO extends DAO{
	
private static final Logger logger = LoggerFactory.getLogger(TBBZDAO.class);
	

    @Override
	public boolean saveData(RawHtml raw_html) {
		try{
			TBBZ Data = new TBBZ();
			Data.setIssue_Date(raw_html.getCrawltime());
			Data.setcDate(new Date());
			Data.setTitle(raw_html.getTitle());
			Data.setContent(raw_html.getContent());
			Data.setIndustry(raw_html.getIndustry());
			Data.setUnit(raw_html.getSource());
			Data.setData_Source(raw_html.getUrl());
			Data.setCorp_Id(raw_html.getUnitName());
			extractField(Data);
			abstractContent(Data);
			begin();
			//getSession().save(Data);
			commit();
			return true;
		}catch(Exception e) {
			rollback();
			logger.error("TBBZDAO saveData failed!",e);
		}
		return false;
	}
    
    
    /**
	 * 抽取正文字段
	 * @param Data
	 */
	public void extractField(TBBZ Data) {
		String content = Data.getContent();
		
		//add code here
	}
	
	/**
	 * 正文摘要
	 * @param Data
	 */
	public void abstractContent(TBBZ Data) {
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
				Data.setContent(sentence);
				logger.info("content="+Data.getContent());
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		ConstructComETL road_TBBZ_etl = new ConstructComETL("公路建设企业","表彰",new TBBZDAO());
		Thread thread = new Thread(road_TBBZ_etl);
		thread.start();
	}

}
