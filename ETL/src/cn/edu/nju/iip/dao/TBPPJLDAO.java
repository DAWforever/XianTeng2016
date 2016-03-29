package cn.edu.nju.iip.dao;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
