package cn.edu.nju.iip.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.edu.nju.iip.model.JWNews;
import cn.edu.nju.iip.util.CommonUtil;


@Service
public class NewsDAO extends DAO{
	
	private static final Logger logger = LoggerFactory.getLogger(NewsDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<JWNews> getAllNews(String tag) {
		List<JWNews> list = new ArrayList<JWNews>();
		try{
			begin();
			Query q = getSession().createQuery("from JWNews where tag=:tag");
			q.setString("tag", tag);
			list = q.list();
			commit();
		}catch(Exception e) {
			rollback();
			logger.error("saveNews failed!",e);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<JWNews> getPosNews(String tag) {
		List<JWNews> list = new ArrayList<JWNews>();
		try{
			begin();
			Query q = getSession().createQuery("from JWNews where tag=:tag and sentiment=:sentiment");
			q.setString("tag", tag);
			q.setString("sentiment", "良好信息");
			list = q.list();
			commit();
		}catch(Exception e) {
			rollback();
			logger.error("getPosNews failed!",e);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<JWNews> getNegNews(String tag) {
		List<JWNews> list = new ArrayList<JWNews>();
		try{
			begin();
			Query q = getSession().createQuery("from JWNews where tag=:tag and sentiment=:sentiment");
			q.setString("tag", tag);
			q.setString("sentiment", "风险信息");
			list = q.list();
			commit();
		}catch(Exception e) {
			rollback();
			logger.error("getNegNews failed!",e);
		}
		return list;
	}
	
	
	public void updateSentiment(String sentiment,int id) {
		try{
			begin();
			Query query = getSession().createQuery("update JWNews set sentiment=:sentiment where id=:id");
			query.setInteger("id",id);
			query.setString("sentiment",sentiment);
			query.executeUpdate();
			commit();
		}catch (HibernateException e) {
			rollback();
			logger.info("NewsDAO-->updateSentiment",e);
		}
	}
	
	public void tagSentiment(JWNews news) {
		String sentiment = CommonUtil.getSentiment(news.getContent());
		updateSentiment(sentiment,news.getId());
	}
	
	public void delete() {
		try{
			begin();
			Query query = getSession().createQuery("delete JWNews where tag is null");
			query.executeUpdate();
			commit();
		}catch (HibernateException e) {
			rollback();
			logger.info("NewsDAO-->delete",e);
		}
	}
	
	public static void main(String[] args) {
	}

}
