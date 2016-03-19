package cn.edu.nju.iip.spider;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.nju.iip.BloomFilter.BloomFactory;
import cn.edu.nju.iip.dao.RawHtmlDAO;
import cn.edu.nju.iip.model.Url;
import cn.edu.nju.iip.util.CommonUtil;


public class RawHtmlSpider extends BreadthCrawler implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(RawHtmlSpider.class);
	
	private BlockingQueue<Page> PageQueue;
	
	private static BloomFactory bf = BloomFactory.getInstance();

	private  Set<String> seed_set = new HashSet<String>();
	
	private int count = 0;
	
	private HashMap<String,ArrayList<?>> map = CommonUtil.importGovUrl();
	@SuppressWarnings("unchecked")
	private ArrayList<Url> seed_url_list = (ArrayList<Url>) map.get("seed_url_list");
	@SuppressWarnings("unchecked")
	private ArrayList<String> content_regex_list = (ArrayList<String>) map.get("content_regex_list");
	@SuppressWarnings("unchecked")
	private ArrayList<String> page_regex_list = (ArrayList<String>) map.get("page_regex_list");
	

	/**
	 * @param crawlPath
	 *            crawlPath is the path of the directory which maintains
	 *            information of this crawler
	 * @param autoParse
	 *            if autoParse is true,BreadthCrawler will auto extract links
	 *            which match regex rules from pag
	 */
	public RawHtmlSpider(String crawlPath, boolean autoParse,BlockingQueue<Page> PageQueue) {
		super(crawlPath, autoParse);
		this.PageQueue = PageQueue;
		for(Url seed_url:seed_url_list) {
			this.addSeed(new CrawlDatum(seed_url.getLink()).putMetaData("source",seed_url.getWebname()).putMetaData("type", seed_url.getCategory()));
			seed_set.add(seed_url.getLink());
		}
		for(String content_regex:content_regex_list) {
			this.addRegex(content_regex);
		}
		for(String page_regex:page_regex_list) {
			this.addRegex(page_regex);
		}
	}
	
	public boolean isContentUrl(Page page) {
		boolean flag = false;
		for(String content_regex:content_regex_list) {
			if(page.matchUrl(content_regex)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public void visit(Page page, CrawlDatums next) {
		String url = page.getUrl();
		//若bloom过滤器里已存在则跳过
		if (bf.contains(url)) {
			return;
		} 
		else {
			//不是种子url就加入bloom过滤器
			if (!seed_set.contains(url)) {
				bf.add(url);
			}
			//是正文url就将页面本地持久化
			if (isContentUrl(page)) {
				 try{
					 PageQueue.put(page);
			         count++;
				 }catch(Exception e) {
					 logger.info("visit failed", e);
				 }
		     }
		}
	}
	
	public void afterVisit(Page page, CrawlDatums next) {
		super.afterVisit(page, next);
		String source = page.getMetaData("source");
		String type = page.getMetaData("type");
		for (CrawlDatum data : next) {
			data.putMetaData("source", source);
			data.putMetaData("type", type);
		}
	}
	public void run() {
		try {
			this.setThreads(50);
			this.setTopN(50000);
			this.start(500);
			File file = new File("crawl");
			CommonUtil.deleteFile(file);
			bf.saveBloomFilter();
			logger.info("count="+this.count);
		}catch(Exception e) {
			 logger.info("RawHtmlSpider run failed", e);
		}
	}
	public static void main(String[] args) throws Exception {
		BlockingQueue<Page> PageQueue = new LinkedBlockingQueue<Page>();
		RawHtmlSpider crawler = new RawHtmlSpider("crawl",true,PageQueue);
        ExecutorService service = Executors.newCachedThreadPool();
        for(int i=0;i<10;i++) {
        	RawHtmlDAO RD = new RawHtmlDAO(PageQueue);
        	service.execute(RD);
        }
        service.execute(crawler);
    }
}
