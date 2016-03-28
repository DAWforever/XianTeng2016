package cn.edu.nju.iip.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public void saveData(RawHtml raw_html) {
		try{
			TBBZ BData = new TBBZ();
			BData.setIssue_Date(raw_html.getCrawltime());
			BData.setcDate(new Date());
			BData.setTitle(raw_html.getTitle());
			int length = raw_html.getContent().length();
			BData.setContent(raw_html.getContent().substring(0,length>1000?1000:length));
			BData.setIndustry(raw_html.getIndustry());
			BData.setUnit(raw_html.getSource());
			BData.setData_Source(raw_html.getUrl());
			BData.setCorp_Id(raw_html.getUnitName());
			begin();
			getSession().save(BData);
			commit();
		}catch(Exception e) {
			rollback();
			logger.error("TBBZDAO saveData failed!",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<TBBZ> getBiaoZhangDataList(String unitName) {
		List<TBBZ> list = null;
		try{
			begin();
			Query query = getSession().createQuery("from BiaoZhangData where Corp_Name=:Corp_Name");
			query.setString("Corp_Name", unitName);
			list = query.list();
			commit();
		}catch(Exception e) {
			rollback();
			logger.error("TBBZDAO getBiaoZhangDataList failed!",e);
		}
		return list;
	}
	
	public static void main(String[] args) {
		TBBZDAO dao = new TBBZDAO();
		logger.info(dao.getBiaoZhangDataList("重庆工业设备安装集团有限公司").size()+"");
	}

}
