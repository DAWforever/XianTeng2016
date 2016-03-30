package cn.edu.nju.iip.util;

import java.util.Calendar;
import java.util.Set;

import redis.clients.jedis.Jedis;
import cn.edu.nju.iip.redis.JedisPoolUtils;


public class CommonUtil {
	
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
	

}
