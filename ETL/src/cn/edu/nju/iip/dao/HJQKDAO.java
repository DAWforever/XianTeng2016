package cn.edu.nju.iip.dao;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.HJQK;
import cn.edu.nju.iip.model.RawHtml;

/**
 * 公路水运建设市场从业企业获奖情况表DAO
 * @author wangqiang
 *
 */
public class HJQKDAO extends DAO {
	
	private static final Logger logger = LoggerFactory.getLogger(HJQKDAO.class);

	@Override
	public boolean saveData(RawHtml raw_html) {
		try{
			HJQK Data = new HJQK();
			Data.setpDate(raw_html.getCrawltime());
			Data.setCdate(new Date());
			Data.setContent(raw_html.getContent());
			Data.setIndustry(raw_html.getIndustry());
			Data.setUnit(raw_html.getSource());
			Data.setData_Source(raw_html.getUrl());
			Data.setCorp_Id(raw_html.getUnitName());
			Data.setType_Name(raw_html.getSource().contains("市")?"市级":"省级");
			extractField(Data);
			begin();
			getSession().save(Data);
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
	public void extractField(HJQK Data) {
		String content = Data.getContent();
		logger.info("content="+content);
		//add code here
	}
	
	public static void main(String[] args) {
		ConstructComETL road_HJQK_etl = new ConstructComETL("公路建设企业","获奖",new HJQKDAO());
		Thread thread = new Thread(road_HJQK_etl);
		thread.start();
	}

}
