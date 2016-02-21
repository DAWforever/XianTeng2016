package cn.edu.nju.iip.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qcloud.Common.TextSentiment;

public class CommonUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(CommonUtil.class);

	/**
	 * 
	 * @param content
	 * @return flag (1表示正面新闻 2表示负面新闻)
	 */
	public static int getSentiment(String content) {
		int flag = 0;
		try {
			JSONObject object = TextSentiment.TextSentimentAPI(content);
			logger.info(object.toString());
			double positive = object.getDouble("positive");
			double negative = object.getDouble("negative");
			if (positive > negative) {
				flag = 1;
			} else {
				flag = 2;
			}
		} catch (Exception e) {
			logger.error("getSentiment error", e);
		}
		return flag;
	}

	public static void SentimentApiExample(String text) {
		String url = "http://api.bosonnlp.com/sentiment/analysis?news";
		try{
			String body = new JSONArray(new String[]{text}).toString();
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.addHeader("Accept", "application/json");
			post.addHeader("X-Token", "OJa6_Tuc.4974.QchdpYA_0tzl");
			HttpEntity entity = new ByteArrayEntity(body.getBytes("UTF-8"));
			post.setEntity(entity);
			HttpResponse response = httpclient.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			logger.info(result);

		} catch (Exception e) {
			logger.error("SentimentApiExample error", e);
		}
	
	}

	public static void main(String[] args) {
		// logger.info(getSentiment("南京大学很好")+"");
		SentimentApiExample("本报8月7日讯(记者 马正拓)为严厉查处借用、挂靠资质等违法违规行为，青岛市市政工程管理处自5月份开始，本年度第一次对外地入青市政企业进行履职检查，发现19家施工企业、两家监理企业存在青岛分公司管理不到位、不配合检查等较严重问题，决定收回各自的《入青信用证》，这些企业中不乏“国字号”。 此次专项检查共对168家外地入青企业的近2000名驻青管理人员，进行了到岗情况核查，部分企业不配合检查、到岗人员不足。其中，黑龙江宇林建筑工程有限责任公司等9家施工企业、四川铁科建设监理有限公司等4家监理企业人员，未及时按要求落实履职检查有关工作，对此类企业提出预警告诫。 北京城建集团有限责任公司等19家施工企业、济南市建设监理有限公司、沈阳中盛瑞科工程咨询有限公司等两家监理企业存在青岛分公司管理不到位、不配合检查等较严重问题。依据本市有关规定，将收回这21家企业的《入青信用证》。记者注意到，个别企业此前就因出过安全事故被通报处理过。例如，中铁七局集团第五工程有限公司，在2013年12月22日，其负责的施工现场发生一起两人死亡事故，被收回入青信用证，停止在全市范围内工程投标报名，清出青岛市建筑市场。 链接19家施工企业名单 北京城建集团有限责任公司、中国建筑第八工程局有限公司、中国建筑一局(集团)有限公司、北京金港机场建设有限责任公司、广州市市政集团有限公司、中国华冶科工集团有限公司、山东一箭建设有限公司、山东中宏路桥建设有限公司、中铁七局集团有限公司、烟建集团有限公司、中铁七局集团第五工程有限公司、中铁十三局集团电务工程有限公司、中铁十四局集团第三工程有限公司、中铁十八局集团第二工程有限公司、厦门市政工程公司、中交第三航务工程局有限公司、中国化学工程第十三建设有限公司、福建龙辉建设工程有限公司、路桥华祥国际工程有限公司。 作者：马正拓 （来源：半岛网-半岛都市报） 本文来源：大众网-半岛都市报");
	}

}
