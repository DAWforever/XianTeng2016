package nju.iip.dao;

import java.util.ArrayList;
import java.util.List;

import nju.iip.dto.JWNews;
import nju.iip.util.CommonUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class NewsDAO extends DAO{
	
	private static final Logger logger = LoggerFactory.getLogger(NewsDAO.class);
	
	@SuppressWarnings("unchecked")
	public List<JWNews> getNews(String tag) {
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
	public List<JWNews> getAllNews() {
		List<JWNews> list = new ArrayList<JWNews>();
		try{
			begin();
			Query query = getSession().createQuery("from JWNews j where j.sentiment=0");
			list = query.list();
			commit();
		}catch (HibernateException e) {
			rollback();
			logger.info("NewsDAO-->getAllNews",e);
		}
		return list;
	}
	
	public void updateSentiment(int flag,int id) {
		try{
			begin();
			Query query = getSession().createQuery("update JWNews set sentiment=:flag where id=:id");
			query.setInteger("id",id);
			query.setInteger("flag",flag);
			query.executeUpdate();
			commit();
		}catch (HibernateException e) {
			rollback();
			logger.info("NewsDAO-->updateSentiment",e);
		}
	}
	
	public void tagSentiment(JWNews news) {
		int flag = CommonUtil.getSentiment(news.getContent());
		updateSentiment(flag,news.getId());
	}
	
	public static void main(String[] args) {
		NewsDAO newsDAO = new NewsDAO();
		List<JWNews> list = newsDAO.getAllNews();
		for(JWNews news:list) {
			newsDAO.tagSentiment(news);
		}
	}

}
