package cn.edu.nju.iip.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.model.Url;

public class CommonUtil {

	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	public static final int one_day_millseconds = 24 * 60 * 60 * 1000;

	/**
	 * 从excel导入url信息
	 */
	public static List<Url> importFromXls() {
		Workbook workbook = null;
		List<Url> url_list = new ArrayList<Url>();
		try {
			workbook = Workbook.getWorkbook(new File(System
					.getProperty("user.dir") + "/resources/媒体评价类数据源.xls"));
		} catch (Exception e) {
			logger.error("importFromXls error!", e);
		}
		Sheet sheet = workbook.getSheet(0);
		int rowCount = sheet.getRows();
		for (int i = 1; i < rowCount; i++) {
			Url Url = new Url();
			String webname = sheet.getCell(0, i).getContents().trim();
			String link = sheet.getCell(3, i).getContents().trim();
			Url.setLink(link);
			Url.setWebname(webname);
			Url.setCategory("媒体评价类信息");
			url_list.add(Url);
		}
		workbook.close();
		return url_list;
	}

	/**
	 * 抽取待爬取url的主域名
	 * 
	 * @return
	 */
	public static String extractSourceUrl(String url) {
		String source_url = null;
		try{
			if(url.contains(".com")) {
				int index = url.indexOf(".com");
				source_url = url.substring(0,index+4);
			}
			else if(url.contains(".cn")) {
				int index = url.indexOf(".cn");
				source_url = url.substring(0,index+3);
			}
			else if(url.contains(".net")) {
				int index = url.indexOf(".net");
				source_url = url.substring(0,index+4);
			}
			else if(url.contains(".org")) {
				int index = url.indexOf(".org");
				source_url = url.substring(0,index+4);
			}
		}catch(Exception e) {
			logger.error("extractSourceUrl error!", e);
		}
		return source_url;
	}

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
	
	public static List<String> importUrl() {
		Workbook workbook = null;
		List<String> list = new ArrayList<String>();
		try {
			workbook = Workbook.getWorkbook(new File(System
					.getProperty("user.dir") + "/resources/媒体评价类数据源.xls"));
		} catch (Exception e) {
			logger.error("importFromXls error!", e);
		}
		Sheet sheet = workbook.getSheet(0);
		int rowCount = sheet.getRows();
		for (int i = 1; i < rowCount; i++) {
			String url = sheet.getCell(3, i).getContents().trim();
			list.add(url);
		}
		return list;
	}
	
	public static HashMap<String,ArrayList<?>> importGovUrl() {
		Workbook workbook = null;
		HashMap<String,ArrayList<?>> map = new HashMap<String,ArrayList<?>>();
		ArrayList<Url> seed_url_list = new ArrayList<Url>();
		ArrayList<String> content_regex_list = new ArrayList<String>();
		ArrayList<String> page_regex_list = new ArrayList<String>();
		try {
			workbook = Workbook.getWorkbook(new File(System
					.getProperty("user.dir") + "/resources/政府监管类数据源.xls"));
		} catch (Exception e) {
			logger.error("importGovUrl error!", e);
		}
		Sheet sheet = workbook.getSheet(0);
		int rowCount = sheet.getRows();
		for (int i = 1; i < rowCount; i++) {
			Url Url = new Url();
			String seed_url = sheet.getCell(2, i).getContents().trim();
			String webName = sheet.getCell(0, i).getContents().trim();
			Url.setLink(seed_url);
			Url.setWebname(webName);
			String content_regex = sheet.getCell(4, i).getContents().trim().replace("\\\\", "\\");
			String page_regex = sheet.getCell(5, i).getContents().trim().replace("\\\\", "\\");
			String type = sheet.getCell(6, i).getContents().trim();
			Url.setCategory(type);
			seed_url_list.add(Url);
			content_regex_list.add(content_regex);
			page_regex_list.add(page_regex);
		}
		map.put("seed_url_list", seed_url_list);
		map.put("content_regex_list", content_regex_list);
		map.put("page_regex_list", page_regex_list);
		return map;
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

	public static void deleteFile(File file) {
		try{
			if (file.exists()) { // 判断文件是否存在
				if (file.isFile()) { // 判断是否是文件
					file.delete(); // delete()方法 你应该知道 是删除的意思;
				} else if (file.isDirectory()) { // 否则如果它是一个目录
					File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
					for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
						deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
					}
				}
				file.delete();
			} else {
				logger.info("所删除的文件不存在！");
			}
		}catch(Exception e) {
			logger.error("deleteFile error",e);
		}
		
	}

	public static void readTxt() {
		List<String> list = importUrl();
		try {
			WritableWorkbook book = Workbook.createWorkbook(new File(
					"C:/Users/wangqiang/Desktop/test.xls"));
			WritableSheet sheet = book.createSheet("第一页", 0);
			BufferedReader in = new BufferedReader(new FileReader("resources/url.txt"));
			String s = in.readLine();
			int n = 0;
			while (s != null) {
				if (s.length() > 0) {
					String[] str = s.split(" ");
					String url = "http://"+str[1];
					if(url.endsWith("/")){
						url = url.substring(0,url.length()-1);
					}
					if(!list.contains(url)) {
						String[] names = str[0].split("--");
						String webname = names[0];
						String press  = names[1];
					    sheet.addCell(new Label(0, n, webname));
					    sheet.addCell(new Label(1, n, "新闻网站"));
					    sheet.addCell(new Label(2, n, press));
					    sheet.addCell(new Label(3, n, url));
					    n++;
					}
				}
				s = in.readLine();
			}
			logger.info("n=" + n);
			in.close();
			// 写入数据
			book.write();
			// 关闭文件
			book.close();
		} catch (Exception e) {
			logger.error("readTxt error", e);
		}
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
	
	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}
	
	public static String getHTML(String url) {
		String html = null;
		try{
			Document doc = Jsoup.connect(url)
					.userAgent("Mozilla")
					.timeout(5000)
					.get();
			html = doc.html();
		}catch(Exception e) {
			logger.info("getHTML error",e);
		}
		return html;
	}
	
	public static void main(String[] args) {
		List<String> list = importUrl();
		for(String url:list) {
			logger.info(extractSourceUrl(url));
		}
		

	}

}
