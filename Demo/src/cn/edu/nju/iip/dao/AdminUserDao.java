package cn.edu.nju.iip.dao;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.edu.nju.iip.model.AdminUser;

@Service
public class AdminUserDao extends DAO{
	
	private static final Logger logger = LoggerFactory.getLogger(AdminUserDao.class);
	
	public  boolean validate(AdminUser user) {
		logger.error("validate called");
		boolean flag = false;
		try{
			begin();
			Query query = getSession().createQuery("from AdminUser where username=:username and password=:password");
			query.setString("username", user.getUsername());
			query.setString("password", user.getPassword());
			if(query.uniqueResult()!=null) {
				flag = true;
			}
			commit();
		}catch(Exception e) {
			rollback();
			logger.error("validate failed!",e);
		}
		return flag;
	}
	
	public static void main(String[] args) {
	}

}
