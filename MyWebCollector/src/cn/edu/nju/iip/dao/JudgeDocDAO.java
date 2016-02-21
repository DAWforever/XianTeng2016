package cn.edu.nju.iip.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.nju.iip.model.JudgeDoc;

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

}
