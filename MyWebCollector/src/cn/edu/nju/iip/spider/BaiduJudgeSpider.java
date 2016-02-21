package cn.edu.nju.iip.spider;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
	
	public static void main(String[] args){
		BaiduJudgeSpider test = new BaiduJudgeSpider();
		Thread thread = new Thread(test);
		thread.start();
	}

}
