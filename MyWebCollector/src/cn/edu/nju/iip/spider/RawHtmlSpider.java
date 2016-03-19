package cn.edu.nju.iip.spider;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.nju.iip.BloomFilter.BloomFactory;
import cn.edu.nju.iip.dao.RawHtmlDAO;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.model.Url;
import cn.edu.nju.iip.util.CommonUtil;
import cn.edu.nju.iip.util.HtmlDocParse;


public class RawHtmlSpider extends BreadthCrawler {
	
	private static final Logger logger = LoggerFactory.getLogger(RawHtmlSpider.class);
	
	
	private static BloomFactory bf = BloomFactory.getInstance();

	private  Set<String> seed_set = new HashSet<String>();
	
	private RawHtmlDAO dao = new RawHtmlDAO();
	
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
	public RawHtmlSpider(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		logger.info("种子URL共:"+seed_url_list.size()+"个");
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
					    RawHtml rawHtml = new RawHtml();
					    HtmlDocParse docParse = new HtmlDocParse(page.getUrl(),page.getHtml());
						String attachment = docParse.getDocsContent();
						rawHtml.setAttachment(attachment);
						rawHtml.setContent(page.getDoc().text()+attachment);
						rawHtml.setSource(page.getMetaData("source"));
						rawHtml.setUrl(page.getUrl());
						rawHtml.setType(page.getMetaData("type"));
						rawHtml.setCrawltime(new Date());
						dao.saveRawHtml(rawHtml);
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
	
	
	public static void main(String[] args) throws Exception {
		RawHtmlSpider crawler = new RawHtmlSpider("crawl",true);
		crawler.setThreads(50);
		crawler.setTopN(50000);
		crawler.start(1000);
		bf.saveBloomFilter();
		logger.info("count="+crawler.count);
    }
}
