package cn.edu.nju.iip.dao;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.nju.iip.model.RawHtml;


public class RawHtmlDAO extends DAO{
	
	private static final Logger logger = LoggerFactory.getLogger(RawHtmlDAO.class);
	
	@Override
	public boolean saveData(RawHtml rawhtml) {
		try{
			begin();
			getSession().save(rawhtml);
			commit();
			return true;
		}catch(Exception e) {
			rollback();
			logger.error("saveRawHtml failed!",e);
		}
		return false;
	}
	
	
	
	public RawHtml getRawHtml(int id) {
		try{
			begin();
			Query q = getSession().createQuery("from RawHtml where id=:id");
			q.setInteger("id", id);
			commit();
			return (RawHtml)q.uniqueResult();
		}catch(Exception e) {
			rollback();
			logger.error("getRawHtml failed!",e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		RawHtmlDAO dao = new RawHtmlDAO();
		for(int i=0;i<10;i++) {
			RawHtml raw_html = dao.getRawHtml(1000006);
			System.out.println(raw_html.getUrl());
		}
		
	}



}
