package cn.edu.nju.iip.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import cn.edu.nju.iip.dao.CORPINFO;
import cn.edu.nju.iip.dao.CORPINFODAO;
import cn.edu.nju.iip.redis.JedisPoolUtils;


public class CommonUtil {

	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	public static final int one_day_millseconds = 24 * 60 * 60 * 1000;


	/**
	 * 公路建设从业企业数据导入
	 * @return
	 */
	public static List<String> importConsRoadUnitName() {
		Workbook workbook = null;
		List<String> list = new ArrayList<String>();
		try {
			workbook = Workbook.getWorkbook(new File(System
					.getProperty("user.dir") + "/resources/公路企业基本信息.xls"));
		} catch (Exception e) {
			logger.error("importFromXls error!", e);
		}
		Sheet sheet = workbook.getSheet(0);
		int rowCount = sheet.getRows();
		for (int i = 1; i < rowCount; i++) {
			String UnitName = sheet.getCell(2, i).getContents().trim();
			if (UnitName.contains("公司")) {
				list.add(UnitName);
			}
		}
		return list;
	}
	
	/**
	 * 水运建设从业企业数据导入
	 * @return
	 */
	public static List<String> importConsShipUnitName() {
		Workbook workbook = null;
		List<String> list = new ArrayList<String>();
		try {
			workbook = Workbook.getWorkbook(new File(System
					.getProperty("user.dir") + "/resources/水运企业基本信息.xls"));
		} catch (Exception e) {
			logger.error("importConsShipUnitName error!", e);
		}
		Sheet sheet = workbook.getSheet(0);
		int rowCount = sheet.getRows();
		for (int i = 1; i < rowCount; i++) {
			String UnitName = sheet.getCell(1, i).getContents().trim();
			list.add(UnitName);
		}
		return list;
	}
	
	/**
	 * 道路运输从业企业数据导入
	 * @return
	 */
	public static List<String> importTransRoadUnitName() {
		Workbook workbook = null;
		List<String> list = new ArrayList<String>();
		try {
			workbook = Workbook.getWorkbook(new File(System
					.getProperty("user.dir") + "/resources/道路运输从业企业.xls"));
		} catch (Exception e) {
			logger.error("importConsShipUnitName error!", e);
		}
		Sheet sheet = workbook.getSheet(0);
		int rowCount = sheet.getRows();
		for (int i = 1; i < rowCount; i++) {
			String UnitName = sheet.getCell(2, i).getContents().trim();
			if (UnitName.contains("公司")) {
				list.add(UnitName);
			}
		}
		return list;
	}
	
	/**
	 * 公路建设从业企业数据导入(新)
	 * @return
	 */
	public static List<String> importConsRoadUnitName2() {
		List<String> list = new ArrayList<String>();
		CORPINFODAO dao = new CORPINFODAO();
		List<CORPINFO> corp_list = dao.getData("1");
		for (CORPINFO corp : corp_list){
			list.add(corp.getCorp_name());
		}
		return list;
	}
	
	/**
	 * 水运建设从业企业数据导入(新)
	 * @return
	 */
	public static List<String> importConsShipUnitName2() {
		List<String> list = new ArrayList<String>();
		CORPINFODAO dao = new CORPINFODAO();
		List<CORPINFO> corp_list = dao.getData("2");
		for (CORPINFO corp : corp_list){
			list.add(corp.getCorp_name());
		}
		return list;
	}
	
	/**
	 * 道路运输从业企业数据导入(新)
	 * @return
	 */
	public static List<String> importTransRoadUnitName2() {
		List<String> list = new ArrayList<String>();
		CORPINFODAO dao = new CORPINFODAO();
		List<CORPINFO> corp_list = dao.getData("3");
		for (CORPINFO corp : corp_list){
			list.add(corp.getCorp_name());
		}
		return list;
	}
	
	/**
	 * 将所有标签存入redis标签库
	 */
	public static void saveAllTagsToRedis() {
		Jedis jedis = JedisPoolUtils.getInstance().getJedis();
		List<String> ConsShipUnitNames = importConsShipUnitName2();
		logger.info("ConsShipUnitNames size="+ConsShipUnitNames.size());
		for(String unitName:ConsShipUnitNames) {
			jedis.sadd("Taglib:水运建设企业", unitName);
		}
		List<String> ConsRoadUnitName = importConsRoadUnitName2();
		logger.info("ConsRoadUnitName size="+ConsRoadUnitName.size());
		for(String unitName:ConsRoadUnitName) {
			jedis.sadd("Taglib:公路建设企业", unitName);
		}
		List<String> TransRoadUnitName = importTransRoadUnitName2();
		logger.info("TransRoadUnitName size="+TransRoadUnitName.size());
		for(String unitName:TransRoadUnitName) {
			jedis.sadd("Taglib:道路运输企业", unitName);
		}
		saveCreditTag(jedis);
		JedisPoolUtils.getInstance().returnRes(jedis);
	}
	
	public static void saveCreditTag(Jedis jedis) {
		jedis.sadd("Taglib:信用相关标签", "表彰");
		jedis.sadd("Taglib:信用相关标签", "批评");
		jedis.sadd("Taglib:信用相关标签", "获奖");
	}
	
	public static Set<String> getUnitNameSet(String tag_type) {
		Jedis jedis = JedisPoolUtils.getInstance().getJedis();
		Set<String> set = jedis.smembers(tag_type);
		JedisPoolUtils.getInstance().returnRes(jedis);
		return set;
	}
	
	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getTime() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String time = dateFormat.format(now);
		return time;
	}
	
	public static Date parseDate(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
		Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			logger.error("parseDate error!", e);
		}  
		return date;
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

	public static void main(String[] args) {
		//logger.info(getUnitNameSet("水运建设企业").toString());
		//saveAllTagsToRedis();
		List<String> road_list = importConsRoadUnitName2();
		List<String> ship_list = importConsShipUnitName2();
		List<String> train_list = importTransRoadUnitName2();
		
		for (String temp : road_list)
			System.out.println(temp);
		
		System.out.println("***********************");
		
		for (String temp : ship_list)
			System.out.println(temp);
		
		System.out.println("***********************");
		
		for (String temp : train_list)
			System.out.println(temp);
		
		System.out.println(road_list.size() + "\t" + ship_list.size() + "\t" + train_list.size());
	}

}
