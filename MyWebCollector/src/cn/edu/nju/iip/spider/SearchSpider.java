package cn.edu.nju.iip.spider;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.hfut.dmic.contentextractor.News;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.nju.iip.BloomFilter.BloomFactory;
import cn.edu.nju.iip.dao.RawHtmlDAO;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.util.CommonUtil;
/**
 * 360新闻网站新闻抓取
 * @author wangqiang
 *
 */
public class SearchSpider extends BreadthCrawler {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchSpider.class);
	
	private RawHtmlDAO rawHtmlDAO = new RawHtmlDAO();
	
	private static BloomFactory bf = BloomFactory.getInstance();
	
	private int count = 0;

	/**
	 * @param crawlPath
	 *            crawlPath is the path of the directory which maintains
	 *            information of this crawler
	 * @param autoParse
	 *            if autoParse is true,BreadthCrawler will auto extract links
	 *            which match regex rules from pag
	 */
	public SearchSpider(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		List<String> consRoadUnitName = CommonUtil.importConsRoadUnitName(); 
		logger.info("consRoadUnitName="+consRoadUnitName.size()+"");
		for(String uniName:consRoadUnitName) {
			this.addSeed(new CrawlDatum("http://news.so.com/ns?q=\""+uniName+"\"&pn=1&tn=news&rank=pdate&j=0").putMetaData("unitName", uniName));
		}
		List<String> consShipUnitName = CommonUtil.importConsShipUnitName(); 
		logger.info("consShipUnitName="+consShipUnitName.size()+"");
		for(String uniName:consShipUnitName) {
			this.addSeed(new CrawlDatum("http://news.so.com/ns?q=\""+uniName+"\"&pn=1&tn=news&rank=pdate&j=0").putMetaData("unitName", uniName));
		}
//		List<String> transRoadUnitName = CommonUtil.importTransRoadUnitName(); 
//		logger.info("transRoadUnitName="+transRoadUnitName.size()+"");
//		for(String uniName:transRoadUnitName) {
//			this.addSeed(new CrawlDatum("http://news.so.com/ns?q=\""+uniName+"\"&pn=1&tn=news&rank=pdate&j=0").putMetaData("unitName", uniName));
//		}
		/* fetch url like http://news.hfut.edu.cn/show-xxxxxxhtml */
		this.addRegex("http://news.so.com/ns\\?q=.+&pn=\\d+&tn=news&rank=pdate&j=0");
	}

	@Override
	public void visit(Page page, CrawlDatums next) {
		Document doc = page.getDoc();
		try {
			Elements reslist = doc.select("li.res-list");
			for(Element res:reslist) {
				String unitName = page.getMetaData("unitName");//企业名称
				
				Element content = res.select("p.content").first();
				String news_abstract = content.text();
				if(news_abstract==null||!news_abstract.contains(unitName)) {
					continue;
				}
				
				RawHtml rawhtml = new RawHtml();
				
				Element news_title = res.select("a.news_title").first();
				String title = news_title.text();//标题
				String news_url = news_title.attr("href");//新闻url
				if(bf.contains(news_url)) {
					continue;
				}
				else {
					bf.add(news_url);
				}
				String html = CommonUtil.getHTML(news_url);
				if(html==null) continue;
				News news = ContentExtractor.getNewsByHtml(html);
				String nwes_content = news.getContent();
				
				Element sitename = res.select("span.sitename").first();
				String source = sitename.text();//来源
				
				//Element posttime = res.select("span.posttime").first();
				//Date pdate = CommonUtil.strToDateLong(posttime.attr("title"));
				rawhtml.setHtml(html);
				rawhtml.setTitle(title);
				rawhtml.setUrl(news_url);
				rawhtml.setContent(nwes_content);
				rawhtml.setSource(source);
				rawhtml.setCrawltime(new Date());
				rawhtml.setType("媒体评价类信息");
				rawHtmlDAO.saveRawHtml(rawhtml);
				count++;
			}
		}catch(Exception e) {
			logger.error("SearchSpider visit error",e);
		}
	}

	@Override
	public void afterVisit(Page page, CrawlDatums next) {
		super.afterVisit(page, next);
		String unitName = page.getMetaData("unitName");
		for (CrawlDatum data : next) {
			data.putMetaData("unitName", unitName);
		}
	}
	
	public static void stratSearchSpider() {
		try{
			logger.info("************360SearchSpider start************");
			SearchSpider crawler = new SearchSpider("crawl", true);
			crawler.setThreads(2);
			crawler.setTopN(50000);
			crawler.start(4);
			crawler.setRetry(1);
			bf.saveBloomFilter();
			logger.info("新增新闻: "+crawler.count);
			File file = new File("crawl");
			CommonUtil.deleteFile(file);
			logger.info("************360SearchSpider finish************");
		}catch(Exception e) {
			logger.error("stratSearchSpider run() failed", e);
		}
	}
	public static void main(String[] args) throws Exception {
		stratSearchSpider();
	}
}
