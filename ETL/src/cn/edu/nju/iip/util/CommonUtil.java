package cn.edu.nju.iip.util;

import java.util.Calendar;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import cn.edu.nju.iip.redis.JedisPoolUtils;


public class CommonUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	public static final int one_day_millseconds = 24 * 60 * 60 * 1000;


	public static Set<String> getUnitNameSet(String unitType) {
		Jedis jedis = JedisPoolUtils.getInstance().getJedis();
		Set<String> set = jedis.smembers("Taglib:"+unitType);
		JedisPoolUtils.getInstance().returnRes(jedis);
		return set;
	}
	
	public static long getDelayTime() {
		Calendar c = Calendar.getInstance();
		long currentTime = c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(Config.getValue("start_hour")));
	    c.set(Calendar.MINUTE, Integer.valueOf(Config.getValue("start_minites")));
	    c.set(Calendar.SECOND, 0);
	    long executeTime = c.getTimeInMillis();
		return executeTime - currentTime < 0 ? (executeTime - currentTime + one_day_millseconds)
	            : (executeTime - currentTime);
	}
	
	/**
	 * 抽取附件文件名
	 * @param attachment
	 * @return
	 */
	public static String getAttachFileName(String attachment) {
		String fileName = "";
		try{
			if(attachment==null) return null;
			Pattern pattern = Pattern.compile("###(.*)###");
			Matcher matcher = pattern.matcher(attachment);
			boolean flag = true;
			while(matcher.find()) {
				String str = matcher.group(1);
				int index = str.lastIndexOf("/");
				str = str.substring(index+1);
				if(flag) {
					flag = false;
				}
				else {
					str = "#"+str;
				}
				fileName = fileName+str;
			}
		}catch(Exception e) {
			logger.error("CommonUtil.getAttachFileName() failed!",e);
		}
		return fileName;
	}
	
	/**
	 * 正文无效字符清除如&nbsp
	 * @param content
	 * @return
	 */
	public static String getCleanContent(String content) {
		return content.replace((char)160, ' ').replace((char)12288, ' ').replaceAll("\\s+", " ");
	}
	

}
