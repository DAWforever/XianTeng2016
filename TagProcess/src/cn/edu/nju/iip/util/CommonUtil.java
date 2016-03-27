package cn.edu.nju.iip.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommonUtil {

	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);



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

	public static void main(String[] args) {

	}

}
