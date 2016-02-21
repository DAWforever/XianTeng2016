package cn.edu.nju.iip.dao;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.edu.nju.iip.model.JudgeDoc;

@Service
public class JudgeDocDAO extends DAO{
	
	private static final Logger logger = LoggerFactory.getLogger(JudgeDocDAO.class);
	
	public void saveJudgeDoc(JudgeDoc doc) {
		try{
			begin();
			getSession().save(doc);
			commit();
		}catch(Exception e) {
			rollback();
			logger.error("saveJudgeDoc failed!",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<JudgeDoc> getJudgeDoc(String unitName) {
		List<JudgeDoc> list = null;
		try{
			begin();
			Query query = getSession().createQuery("from JudgeDoc where iname=:iname");
			query.setString("iname", unitName);
			list = query.list();
			commit();
		}catch(Exception e) {
			rollback();
			logger.error("getJudgeDoc failed!",e);
		}
		return list;
	}

}
