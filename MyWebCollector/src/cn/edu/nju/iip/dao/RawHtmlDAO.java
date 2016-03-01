package cn.edu.nju.iip.dao;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.nju.iip.model.RawHtml;


public class RawHtmlDAO extends DAO implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(RawHtmlDAO.class);
	
	private BlockingQueue<RawHtml> Queue;
	
	public RawHtmlDAO(BlockingQueue<RawHtml> Queue) {
		this.Queue = Queue;
	}
	public RawHtmlDAO(){
		
	}
	
	public void saveRawHtml(RawHtml rawhtml) {
		try{
			begin();
			getSession().save(rawhtml);
			commit();
		}catch(Exception e) {
			rollback();
			logger.error("saveRawHtml failed!",e);
		}
	}
	
	public void run() {
		logger.info("****************"+Thread.currentThread().getName()+" RawHtmlDAO线程start！****************");
		while(true) {
			try{
				RawHtml rawhtml = Queue.take();
				saveRawHtml(rawhtml);
			}catch (Exception e) {
				logger.info("RawHtmlDAO run() failed", e);
			}
		}
	}
	
	public void getRawHtml() {
		try{
			begin();
			Query q = getSession().createQuery("from RawHtml where content like '%重庆工业设备安装集团有限公司%'");
			List<RawHtml> list = q.list();
			for(RawHtml html:list) {
				logger.info("id="+html.getUrl());
			}
			commit();
		}catch(Exception e) {
			rollback();
			logger.error("saveRawHtml failed!",e);
		}
	}
	
	public static void main(String[] args) {
		
		RawHtmlDAO dao = new RawHtmlDAO();
		dao.getRawHtml();
	}

}
