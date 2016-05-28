package cn.edu.nju.iip.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CORPINFODAO extends DAO{
	
	private static final Logger logger = LoggerFactory.getLogger(CORPINFODAO.class);
	
	@SuppressWarnings("unchecked")
	public List<CORPINFO> getData(String type) {
		List<CORPINFO> result = new ArrayList<>();
		try{
			begin();
			Query q = getSession().createQuery("from CORPINFO where corp_type=:type");
			q.setString("type", type);
			commit();
			Iterator<CORPINFO> it = q.iterate();
			while(it.hasNext()){
				CORPINFO temp = it.next();
				//System.out.println(temp.getCorp_id() +"\t" + temp.getCorp_name() + "\t" +temp.getCorp_type());
				result.add(temp);
			}
			return result;
		}catch(Exception e) {
			rollback();
			logger.error("failed!",e);
		}
		return null;
	}
	
	public String fetchID(String corp_name){
		try{
			begin();
			Query q = getSession().createQuery("from CORPINFO where corp_name=:name");
			q.setString("name", corp_name);
			commit();
			CORPINFO corp = (CORPINFO) q.uniqueResult();
			return corp.getCorp_id();
		}catch(Exception e) {
			rollback();
			logger.error("failed!",e);
		}
		return null;
	}
	
	public String getShortNames(String corp_name){
		try{
			begin();
			Query q = getSession().createQuery("from CORPINFO where corp_name=:name");
			q.setString("name", corp_name);
			commit();
			CORPINFO corp = (CORPINFO) q.uniqueResult();
			if(corp==null) return null;
			else
			return corp.getCorp_shortNames();
		}catch(Exception e) {
			rollback();
			logger.error("failed!",e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		CORPINFODAO dao = new CORPINFODAO();
		//ArrayList<CORPINFO> list = (ArrayList<CORPINFO>) dao.getData("3");
		//System.out.println(list.size());
		
		System.out.println(dao.getShortNames("水利工程有限公司"));
	}



}
