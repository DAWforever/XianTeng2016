package cn.edu.nju.iip.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.dao.JudgeDocDAO;
import cn.edu.nju.iip.model.JudgeDoc;
import cn.edu.nju.iip.util.CommonUtil;

/**
 * 中国裁判文书网
 * 
 * @author mrpod2g
 * 
 */
public class JudgesOnlineSpider implements Runnable{

	private static final Logger logger = LoggerFactory
			.getLogger(JudgesOnlineSpider.class);

	private String unitName;

	private JudgeDocDAO dao = new JudgeDocDAO();
	
	private BlockingQueue<String> NameQueue;
	
	public JudgesOnlineSpider(BlockingQueue<String> NameQueue) {
		this.NameQueue = NameQueue;
	}
	
	public JudgesOnlineSpider(String unitName) {
		this.unitName = unitName;
	}


	public String getRespondJson(int index) {
		String result = null;
		String url = "http://wenshu.court.gov.cn/List/ListContent";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(url);
			post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Direction", "asc"));
			params.add(new BasicNameValuePair("Index", index + ""));
			params.add(new BasicNameValuePair("Order", "法院层级"));
			params.add(new BasicNameValuePair("Page", "20"));
			params.add(new BasicNameValuePair("Param", "全文检索:" + unitName));
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpclient.execute(post);
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			result = result.replace("\\", "").replace("n", "");
			int pos = result.indexOf("]");
			result = result.substring(1, pos + 1);
			logger.info("result="+result);
		} catch (Exception e) {
			logger.error("getRespondJson error", e);
		}
		return result;
	}

	public int getPageNum() {
		int num = 0;
		try {
			String json = getRespondJson(1);
			JSONArray array = new JSONArray(json);
			JSONObject jsonObject = array.getJSONObject(0);
			num = jsonObject.getInt("Cout") / 20 + 1;
		} catch (Exception e) {
			logger.error("getPageNum error", e);
		}
		return num;
	}

	public JudgeDoc getJudgeDoc(JSONObject jsonObject) {
		JudgeDoc judgeDoc = new JudgeDoc();
		try {
			String courtName = jsonObject.getString("法院名称");
			logger.info("courtName="+courtName);
			String caseCode = jsonObject.getString("案号");
			logger.info("caseCode="+caseCode);
			String iname = unitName;
			String publishDate = jsonObject.getString("裁判日期");
			logger.info("publishDate="+publishDate);
			String content = jsonObject.getString("DocCotet");
			String title = jsonObject.getString("案件名称");
			logger.info("title="+title);
			judgeDoc.setCaseCode(caseCode);
			judgeDoc.setCourtName(courtName);
			judgeDoc.setIname(iname);
			judgeDoc.setPublishDate(publishDate);
			judgeDoc.setContent(content);
			judgeDoc.setTitle(title);
		} catch (Exception e) {
			logger.error("getJudgeDoc error", e);
		}
		return judgeDoc;
	}

	public void SaveJudgeDocList(int index) {
		try {
			String json = getRespondJson(index);
			JSONArray array = new JSONArray(json);
			int size = array.length();
			for (int i = 1; i < size; i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				JudgeDoc doc = getJudgeDoc(jsonObject);
				//dao.saveJudgeDoc(doc);
			}
		} catch (Exception e) {
			logger.error("SaveJudgeDocList error", e);
		}
	}

	public void saveAllDoc() {
		int pageNum = getPageNum();
		for (int i = 1; i<=pageNum; i++) {
			SaveJudgeDocList(i);
		}
	}
	
	public void run() {
		try {
			unitName = NameQueue.take();
			saveAllDoc();
		} catch (InterruptedException e) {
			logger.error("run error", e);
		}
	}

	public static void main(String[] args) {
//		List<String> list = CommonUtil.importUnitName();
//		BlockingQueue<String> NameQueue = new LinkedBlockingQueue<String>();
//		NameQueue.addAll(list);
//		ExecutorService service = Executors.newCachedThreadPool();
//		for(int i=0;i<20;i++) {
//			JudgesOnlineSpider spider = new JudgesOnlineSpider(NameQueue);
//			service.execute(spider);
//		}
		JudgesOnlineSpider spider = new JudgesOnlineSpider("浙江东方市政园林工程有限公司");
		spider.saveAllDoc();
	}

	

}
