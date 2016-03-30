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
	public boolean saveData(RawHtml raw_html) {
		try{
			TBBZ BData = new TBBZ();
			BData.setIssue_Date(raw_html.getCrawltime());
			BData.setcDate(new Date());
			BData.setTitle(raw_html.getTitle());
			BData.setContent(raw_html.getContent());
			BData.setIndustry(raw_html.getIndustry());
			BData.setUnit(raw_html.getSource());
			BData.setData_Source(raw_html.getUrl());
			BData.setCorp_Id(raw_html.getUnitName());
			begin();
			getSession().save(BData);
			commit();
			return true;
		}catch(Exception e) {
			rollback();
			logger.error("TBBZDAO saveData failed!",e);
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<TBBZ> getBiaoZhangDataList() {
		List<TBBZ> list = null;
		try{
			begin();
			Query query = getSession().createQuery("from TBBZ where CORP_ID=:id");
			query.setString("id", "河南省公路工程局集团有限公司");
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
		List<TBBZ> list = dao.getBiaoZhangDataList();
		for(TBBZ bz:list) {
			//List<String> sentenceList = HanLP.extractSummary(bz.getContent(), 10);
		    //System.out.println("sentenceList="+sentenceList);
		    System.out.println("url="+bz.getData_Source());
		}
	}

}