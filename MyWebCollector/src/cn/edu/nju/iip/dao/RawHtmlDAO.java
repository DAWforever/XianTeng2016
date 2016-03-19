package cn.edu.nju.iip.dao;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.nju.iip.model.RawHtml;


public class RawHtmlDAO extends DAO implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(RawHtmlDAO.class);
	
	private BlockingQueue<Page> Queue;
	
	public RawHtmlDAO(BlockingQueue<Page> Queue) {
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
				Page page = Queue.take();
				RawHtml rawHtml = new RawHtml();
				//String attachment = FileUtil.ReadDoc(page.getUrl(), page.getHtml()).replaceAll("[\\n\\t\\s]", "");
				String attachment = "";
				rawHtml.setAttachment(attachment);
				rawHtml.setContent(page.getDoc().text()+attachment);
				rawHtml.setSource(page.getMetaData("source"));
				rawHtml.setUrl(page.getUrl());
				rawHtml.setType(page.getMetaData("type"));
				rawHtml.setCrawltime(new Date());
				saveRawHtml(rawHtml);
			}catch (Exception e) {
				logger.info("RawHtmlDAO run() failed", e);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<RawHtml> getRawHtml(String type) {
		try{
			begin();
			Query q = getSession().createQuery("from RawHtml");
			List<RawHtml> list = q.list();
			logger.info("size="+list.size());
			commit();
			return list;
		}catch(Exception e) {
			rollback();
			logger.error("getRawHtml failed!",e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		RawHtmlDAO dao = new RawHtmlDAO();
		dao.getRawHtml("");
	}

}
