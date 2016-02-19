package nju.iip.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qcloud.Common.TextSentiment;

public class CommonUtil {

	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	/**
	 * 
	 * @param content
	 * @return flag (1表示正面新闻 2表示负面新闻)
	 */
	public static int getSentiment(String content) {
		int flag = 0;
		try{
			JSONObject object = TextSentiment.TextSentimentAPI(content);
			logger.info(object.toString());
			double positive = object.getDouble("positive");
			double negative = object.getDouble("negative");
			if(positive>negative) {
				flag = 1;
			}
			else {
				flag = 2;
			}
		}catch(Exception e) {
			logger.error("getSentiment error",e);
		}
		return flag;
	}


	public static void main(String[] args) {
		logger.info(getSentiment("南京大学很好")+"");
	}

}
