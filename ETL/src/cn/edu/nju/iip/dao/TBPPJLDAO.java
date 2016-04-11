package cn.edu.nju.iip.dao;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.model.TBPPJL;

/**
 * 公路水运建设市场从业企业 通报批评表DAO
 * @author wangqiang
 *
 */
public class TBPPJLDAO extends DAO{
	
private static final Logger logger = LoggerFactory.getLogger(TBPPJLDAO.class);
	

    @Override
	public boolean saveData(RawHtml raw_html) {
		try{
			TBPPJL Data = new TBPPJL();
			Data.setpDate(raw_html.getCrawltime());
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
	public void extractField(TBPPJL Data) {
		String content = Data.getContent();
		//logger.info("content="+Data.getData_Source());
		//add code here
	}
	
	/**
	 * 正文摘要
	 * @param Data
	 */
	public void abstractContent(TBPPJL Data) {
		String content = Data.getContent();
		String[] sentences = content.split("[\\s。？]+");
		for (String sentence : sentences) {
			if (sentence.contains("关于")&&(sentence.contains("通知")||sentence.contains("通报"))) {
				if(sentence.contains("关于")) {
					int index = sentence.indexOf("关于");
					sentence = sentence.substring(index);
				}
				if (sentence.length() > 50) {
					sentence = sentence.substring(0, 50);
				}
				Data.setContent(sentence);
				logger.info("sentence="+sentence);
				return;
			}
		}
		Data.setContent(null);
	}
	
	public static void main(String[] args) {
		ConstructComETL road_HJQK_etl = new ConstructComETL("公路建设企业","批评",new TBPPJLDAO());
		Thread thread = new Thread(road_HJQK_etl);
		thread.start();
	}

}
