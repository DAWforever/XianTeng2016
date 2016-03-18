package cn.edu.nju.iip.spider;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.nju.iip.dao.RawHtmlDAO;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.util.CommonUtil;


public class RawHtmlSpider extends BreadthCrawler{
	
	private static final Logger logger = LoggerFactory.getLogger(RawHtmlSpider.class);
	
	//private RawHtmlDAO rawHtmlDao = new RawHtmlDAO();
	
	private int count = 0;
	
	private HashMap<String,ArrayList<String>> map = CommonUtil.importGovUrl();
	private ArrayList<String> seed_url_list = map.get("seed_url_list");
	private ArrayList<String> content_regex_list = map.get("content_regex_list");
	private ArrayList<String> page_regex_list = map.get("page_regex_list");
	

	/**
	 * @param crawlPath
	 *            crawlPath is the path of the directory which maintains
	 *            information of this crawler
	 * @param autoParse
	 *            if autoParse is true,BreadthCrawler will auto extract links
	 *            which match regex rules from pag
	 */
	public RawHtmlSpider(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		for(String seed_url:seed_url_list) {
			this.addSeed(seed_url);
		}
		for(String page_regex:content_regex_list) {
			this.addRegex(page_regex);
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
		 if (isContentUrl(page)) {
	            Document doc = page.getDoc();
	            logger.info("content="+doc.text());
	            count++;
	     }
	}
	
	public static void main(String[] args) throws Exception {
		RawHtmlSpider crawler = new RawHtmlSpider("crawl", true);
        crawler.setThreads(50);
        crawler.setTopN(50000);
        crawler.start(500);
        logger.info("count="+crawler.count);
    }


}
