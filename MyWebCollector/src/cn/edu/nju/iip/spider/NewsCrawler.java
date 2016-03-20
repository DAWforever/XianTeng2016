package cn.edu.nju.iip.spider;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.hfut.dmic.contentextractor.News;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.nju.iip.BloomFilter.BloomFactory;
import cn.edu.nju.iip.dao.NewsDAO;
import cn.edu.nju.iip.dao.RawHtmlDAO;
import cn.edu.nju.iip.model.JWNews;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.model.Url;
import cn.edu.nju.iip.util.CommonUtil;

/**
 * Crawling news from hfut news
 * 
 * @author hu
 */
public class NewsCrawler extends BreadthCrawler {

	private static final Logger logger = LoggerFactory.getLogger(NewsCrawler.class);

	private static BloomFactory bf = BloomFactory.getInstance();

	private static Set<String> seed_set = new HashSet<String>();
	
	private RawHtmlDAO Dao = new RawHtmlDAO();
	
	private static int count;

	/**
	 * @param crawlPath
	 *            crawlPath is the path of the directory which maintains
	 *            information of this crawler
	 * @param autoParse
	 *            if autoParse is true,BreadthCrawler will auto extract links
	 *            which match regex rules from pag
	 */
	public NewsCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		count = 0;
		List<Url> list = CommonUtil.importFromXls();
		logger.info("种子URL共:"+list.size()+"个");
		for (Url url : list) {
			this.addSeed(new CrawlDatum(url.getLink()).putMetaData("source",url.getWebname()).putMetaData("type", url.getCategory()));
			this.addRegex(CommonUtil.extractSourceUrl(url.getLink())+".*");
			logger.info(CommonUtil.extractSourceUrl(url.getLink())+".*");
			seed_set.add(url.getLink());
		}
	}

	public void visit(Page page, CrawlDatums next) {
		String url = page.getUrl();
		if (bf.contains(url)) {
			return;
		} else {
			if (!seed_set.contains(url)) {
				bf.add(url);
				try {
					News news = ContentExtractor.getNewsByHtml(page.getHtml());
					RawHtml rawHtml = new RawHtml();
					rawHtml.setContent(news.getTitle()+"#"+news.getContent());
					rawHtml.setSource(page.getMetaData("source"));
					rawHtml.setUrl(url);
					rawHtml.setCrawltime(new Date());
					rawHtml.setType(page.getMetaData("type"));
					rawHtml.setAttachment("");
					Dao.saveRawHtml(rawHtml);
					count++;
				} catch (Exception e) {
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

	public static void startNewsCrawler() {
		while (true) {
			logger.info("*************NewsCrawler begin*********************");
			NewsCrawler crawler = new NewsCrawler("crawl", true);
			try {
				crawler.setThreads(50);
				crawler.setTopN(500000);
				crawler.setResumable(true);
				crawler.start(3);
				File file = new File("crawl");
				CommonUtil.deleteFile(file);
				bf.saveBloomFilter();
				logger.info("新增新闻:"+count);
				logger.info("*************NewsCrawler finish*********************");
				logger.info("*************start sleep*********************");
				Thread.sleep(12 * 60 * 60 * 1000);
			} catch (Exception e) {
				logger.info("NewsCrawler run() error", e);
			}
		}

	}

	public static void main(String[] args) throws Exception {
		startNewsCrawler();
	}

}
