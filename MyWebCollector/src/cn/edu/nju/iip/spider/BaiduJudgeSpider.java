package cn.edu.nju.iip.spider;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.dao.JudgeDocDAO;
import cn.edu.nju.iip.model.JudgeDoc;
import cn.edu.nju.iip.util.CommonUtil;


public class BaiduJudgeSpider implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(BaiduJudgeSpider.class);
	
	private JudgeDocDAO JDAO = new JudgeDocDAO();
	
	private final String base_url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?resource_id=6899&query=失信被执行人名单&iname=";
	
	public JSONObject getRespondJson(String unitName) {
		JSONObject json = null;
		String url = base_url+unitName;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try{
			HttpGet get = new HttpGet(url);
			HttpResponse response = httpclient.execute(get);
			String result = EntityUtils.toString(response.getEntity());
			json = new JSONObject(result);
		} catch (Exception e) {
			logger.error("getRespondJson error", e);
		}
		return json;
	}
	
	public void parseJudgeDoc(JSONObject json) {
		try{
			JSONArray data = json.getJSONArray("data");
			JSONArray result = data.getJSONObject(0).getJSONArray("result");
			for(int i=0;i<result.length();i++) {
				JudgeDoc judgeDoc = new JudgeDoc();
				JSONObject doc = result.getJSONObject(i);
				judgeDoc.setCourtName(doc.getString("courtName"));
				judgeDoc.setCaseCode(doc.getString("caseCode"));
				judgeDoc.setCardNum(doc.getString("cardNum"));
				judgeDoc.setAreaName(doc.getString("areaName"));
				judgeDoc.setDuty(doc.getString("duty"));
				judgeDoc.setBusinessEntity(doc.getString("businessEntity"));
				judgeDoc.setPerformance(doc.getString("performance"));
				judgeDoc.setDisruptTypeName(doc.getString("disruptTypeName"));
				judgeDoc.setPublishDate(doc.getString("publishDate"));
				judgeDoc.setIname(doc.getString("iname"));
				JDAO.saveJudgeDoc(judgeDoc);
			}
		}catch (Exception e) {
			logger.error("parseJudgeDoc error", e);
		}
	}
	
	public void run() {
		logger.info("**************BaiduJudgeSpider thread start**************");
		List<String> unitNameList = CommonUtil.importUnitName();
		for(String name:unitNameList) {
			parseJudgeDoc(getRespondJson(name));
		}
		logger.info("**************BaiduJudgeSpider thread end**************");
	}
	
	public static JSONObject getRespondJsonTest() {
		JSONObject json = null;
		String url = "http://wenshu.court.gov.cn/List/ListContent";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try{
			HttpPost post = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Index", "1"));
			params.add(new BasicNameValuePair("Order", "法院层级"));
			params.add(new BasicNameValuePair("Page", "5"));
			params.add(new BasicNameValuePair("Param", "全文检索:重庆工业设备安装集团有限公司"));
			post.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
			HttpResponse response = httpclient.execute(post);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			logger.info(result.replace("\\", ""));
			json = new JSONObject(result);
		} catch (Exception e) {
			logger.error("getRespondJson error", e);
		}
		return json;
	}
	
	public static void main(String[] args){
//		BaiduJudgeSpider test = new BaiduJudgeSpider();
//		Thread thread = new Thread(test);
//		thread.start();
		getRespondJsonTest();
	}

}
