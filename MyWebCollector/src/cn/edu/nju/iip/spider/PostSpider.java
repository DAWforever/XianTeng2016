package cn.edu.nju.iip.spider;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.dao.RawHtmlDAO;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.util.CommonUtil;


/**
 * Post方式爬虫
 * @author mrpod2g
 *
 */
public class PostSpider {
	
	private static final Logger logger = LoggerFactory.getLogger(PostSpider.class);
	
	private static final String url = "http://ginfo.mohurd.gov.cn/";
	
	private List<NameValuePair> params;
	
	private CloseableHttpClient httpclient = HttpClients.createDefault();
	
	private RawHtmlDAO dao = new RawHtmlDAO();
	
	private int index ;
	
	public void setParams() {
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$lbtPageDown"));
		params.add(new BasicNameValuePair("__EVENTVALIDATION", "/wEWEAKE65r+AwLe+OO6CQLglpvOCAL1vp+VBgL0p6+GCQKBjLy2DQL9sOihAgL9sNyhAgL9n+COAgK4xMTxDAKxq5vyDAKF5LqzCALdj /udCwKdwfHgDgLt/YHUCAKx3o+TDOGxCMD5kt1+7vW8cgtB9bk/VVsPpQJrNG7vdaMRtHXt"));
		params.add(new BasicNameValuePair("__VIEWSTATE", "/wEPaA8FDzhkMzQxYmY4OTBjNzFhOGSomVBngtwQkEGFqfwRDlmm7hys/UEX9h+T64QsNjhOhQ=="));
		params.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", "90059987"));
	}
	
	public String getRespondJson() {
		String result = null;
		try {
			setParams();
			HttpPost post = new HttpPost(url);
			post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0");
			post.addHeader("Cookie","ASP.NET_SessionId=otbyow1d4d33hw0d1k05auem; X-Mapping-ghkoafom=E31BE4315D90FF440DD461F9077E6745; _gscu_554630632 =56800981qojx1g12; _gscs_554630632=56800981qxbht812|pv:2; _gscbrs_554630632=1");
			params.add(new BasicNameValuePair("ctl00$HFPageIndex", index+""));
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpclient.execute(post);
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			//logger.info("result="+result);
		} catch (Exception e) {
			logger.error("getRespondJson error", e);
		}
		return result;
	}
	
	public void paraseHtml(String html) {
		Pattern patter = Pattern.compile("http://www.mohurd.gov.cn/wjfb/\\d+/t\\d+_\\d+.html");
		Matcher matcher = patter.matcher(html);
		if(!matcher.find()) {
			logger.info("未找到URL! index="+index);
		}
		while(matcher.find()){
			String url = matcher.group();
			logger.info("url="+matcher.group());
			saveRawData(url);
		}
	}
	
	public void saveRawData(String url) {
		try{
			Document doc = Jsoup.connect(url).get();
			RawHtml rawHtml = new RawHtml();
			String content = doc.select("body").text();
			logger.info("content="+doc.select("body").text());
			String source = "中华人民共和国住房和城乡建设部";
			String type = "政府监管";
			rawHtml.setContent(content);
			rawHtml.setSource(source);
			rawHtml.setUrl(url);
			//rawHtml.setCrawltime(new Date());
			rawHtml.setType(type);
			dao.saveRawHtml(rawHtml);
		}catch(Exception e) {
			logger.error("saveRawData error", e);
		}
	}
	
	
	public static void main(String[] args) {
		PostSpider spider = new PostSpider();
		for(int i=1;i<=200;i++) {
			spider.index = i;
			String html = spider.getRespondJson();
			spider.paraseHtml(html);
		}
	}

}
