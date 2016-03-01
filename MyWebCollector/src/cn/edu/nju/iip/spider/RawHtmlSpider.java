package cn.edu.nju.iip.spider;
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
	
	private RawHtmlDAO rawHtmlDao = new RawHtmlDAO();
	
	private int count = 0;
	
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
		for(int i=1;i<=13;i++) {
			this.addSeed("http://www.cq-l-tax.gov.cn/zwgk/ssgg/nsxyajgg/index_"+i+".html");
		}
		this.addSeed("http://www.cq-l-tax.gov.cn/zwgk/ssgg/nsxyajgg/");
	//	this.addRegex("http://www.cqgs.gov.cn/gzfw/gg/.*htm");
	//	this.addRegex("http://www.cqgs12315.cn/gzfw/gg/default.*htm");
		this.addRegex("http://www.cq-l-tax.gov.cn/zwgk/ssgg/nsxyajgg/\\d{5}.*html");
	}

	public void visit(Page page, CrawlDatums next) {
		String url = page.getUrl();
		 if (page.matchUrl("http://www.cq-l-tax.gov.cn/zwgk/ssgg/nsxyajgg/\\d{5}.*html")) {
	            Document doc = page.getDoc();
	            RawHtml rawhtml = new RawHtml();
	            rawhtml.setContent(doc.select("body").text());
	            rawhtml.setUrl(url);
	            rawhtml.setSource("重庆市地方税务局");
	            rawhtml.setCrawltime(CommonUtil.getTime());
	            rawHtmlDao.saveRawHtml(rawhtml);
	           // logger.info("content="+doc.select("body").text());
	            count++;
	     }
	}
	
	public static void main(String[] args) throws Exception {
		RawHtmlSpider crawler = new RawHtmlSpider("crawl", true);
        crawler.setThreads(50);
        crawler.setTopN(1000);
        crawler.start(25);
        logger.info("count="+crawler.count);
    }


}
