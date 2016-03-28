package cn.edu.nju.iip.util;

import java.util.Set;
import redis.clients.jedis.Jedis;
import cn.edu.nju.iip.redis.JedisPoolUtils;


public class CommonUtil {


	public static Set<String> getUnitNameSet(String unitType) {
		Jedis jedis = JedisPoolUtils.getInstance().getJedis();
		Set<String> set = jedis.smembers(unitType);
		JedisPoolUtils.getInstance().returnRes(jedis);
		return set;
	}
	

}
