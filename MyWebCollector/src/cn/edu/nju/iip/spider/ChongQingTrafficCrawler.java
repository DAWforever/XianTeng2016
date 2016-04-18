package cn.edu.nju.iip.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.hfut.dmic.contentextractor.News;
import cn.edu.nju.iip.dao.NewsDAO;
import cn.edu.nju.iip.model.JWNews;


/**
 * 重庆交委新闻爬取
 * @author gaoyang
 *
 */
public class ChongQingTrafficCrawler implements Runnable{

	private static final Logger logger = LoggerFactory.getLogger(ChongQingTrafficCrawler.class);
	
	private static NewsDAO newsDAO = new NewsDAO();
	
	private String getHTML(String url) {
		String html = null;
		try{
			Document doc = Jsoup.connect(url)
					.userAgent("Mozilla")
					.timeout(3000)
					.get();
			html = doc.html();
		}catch(Exception e) {
			logger.info("getHTML error",e);
		}
		return html;
	}
	
	public void getUrlList(String source_url) {
		try{
			String source = "重庆交通委员会";		
			String html = getHTML(source_url);
			if(html==null) return;
			Document doc = Jsoup.parse(html);
			Elements results = doc.select("div.trafficNews").select("li");
			for(Element result:results) {
				String link = "http://www.cqjt.gov.cn"+result.select("a").attr("href");
				String title = result.select("a").text();
				logger.info("fetch URL: "+link);
				String newsHtml = getHTML(link);
				if(newsHtml != null) {
					News news = ContentExtractor.getNewsByHtml(newsHtml);
					JWNews jwnews = new JWNews();
					jwnews.setContent(news.getContent().trim());
					jwnews.setUrl(link);
					jwnews.setSource(source);
					jwnews.setTitle(title);	
					newsDAO.saveNews(jwnews);
				}
				//UrlQueue.put(url);
			}
		}catch(Exception e) {
			logger.info("getUrlList error",e);
		}
	}
	
	@Override
	public void run() {
		logger.info("**************ChongQingTrafficSpider thread start**************");
		
		String source_url = "http://www.cqjt.gov.cn/openCatalogList/01010901_1.html";
		getUrlList(source_url);
		
		logger.info("**************ChongQingTrafficSpider thread end**************");	
	}
	
	public static void main(String[] args) {
		ChongQingTrafficCrawler test = new ChongQingTrafficCrawler();
		new Thread(test).start();
	}
	
}
