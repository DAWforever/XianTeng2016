package cn.edu.nju.iip.dao;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.util.CommonUtil;


public class RawHtmlDAO extends DAO implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(RawHtmlDAO.class);
	
	private BlockingQueue<RawHtml> Queue;
	
	int count = 0;
	
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
	
	@SuppressWarnings("unchecked")
	public List<RawHtml> getRawHtml(String type) {
		try{
			begin();
			Query q = getSession().createQuery("from RawHtml where content like:type");
			q.setString("type", type);
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
		List<String> unitNamelist = CommonUtil.importUnitName();
		List<RawHtml> biaozhangList = dao.getRawHtml("%表彰%");
		logger.info("表彰类信息共："+biaozhangList.size());
		int count1 = 0;
		for(RawHtml html:biaozhangList) {
			for(String name:unitNamelist) {
				if(html.getContent().contains(name)) {
					logger.info("name="+name+" url="+html.getUrl());
					count1++;
				}
			}
		}
		logger.info("包含从业企业的共计:"+count1+"条\n");
		
		List<RawHtml> piPingList = dao.getRawHtml("%批评%");
		logger.info("批评类信息共："+piPingList.size());
		int count2 = 0;
		for(RawHtml html:piPingList) {
			for(String name:unitNamelist) {
				if(html.getContent().contains(name)) {
					logger.info("name="+name+" url="+html.getUrl());
					count2++;
				}
			}
		}
		logger.info("包含从业企业的共计:"+count2+"\n");
		
		List<RawHtml> huojiangList = dao.getRawHtml("%获奖%");
		logger.info("获奖类信息共："+huojiangList.size());
		int count3 = 0;
		for(RawHtml html:huojiangList) {
			for(String name:unitNamelist) {
				if(html.getContent().contains(name)) {
					logger.info("name="+name+" url="+html.getUrl());
					count3++;
				}
			}
		}
		logger.info("包含从业企业的共计:"+count3);
	}

}
