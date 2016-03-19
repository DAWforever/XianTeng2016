package cn.edu.nju.iip.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	private static String save_path = "D:\\docs\\";

	public static String ReadDoc(String url, String html) {
		// url是包含附件的网页url
		// path是保存附件的路径，例如"D://"或是"C://downloads"
		// 返回值为arraylist类型的附件内容
		String res = "";
		try {
			ArrayList<String> files = downLoadFiles(url, html);
			for (String file : files) {
				res = res+"\n"+"#####"+file+"#####";
				String type = file.substring(file.lastIndexOf(".") + 1);
				if (type.equals("doc"))
					res += readWORD(save_path + file);
				else if (type.equals("docx"))
					res += readWORD2007(save_path + file);
				else if (type.equals("xls"))
					res += readEXCEL(save_path + file);
				else if (type.equals("xlsx"))
					res += readEXCEL2007(save_path + file);
			}
		} catch (Exception e) {
			logger.error("ReadDoc error!", e);
		}
		return res;
	}

	public static String readWORD(String file) throws Exception {
		String returnStr = "";
		WordExtractor wordExtractor = null;
		try {
			wordExtractor = new WordExtractor(new FileInputStream(new File(file)));
			returnStr = wordExtractor.getText();
		} catch (IOException e) {
			logger.error("readWORD error!", e);
		}finally {
			if(wordExtractor!=null) {
				wordExtractor.close();
			}
		}
		
		return returnStr;
	}

	// 读取docx文件
	@SuppressWarnings("resource")
	public static String readWORD2007(String file) throws Exception {
		return new XWPFWordExtractor(POIXMLDocument.openPackage(file))
				.getText();
	}

	// 读取xls
	@SuppressWarnings("deprecation")
	public static String readEXCEL(String file) throws IOException {
		StringBuilder content = new StringBuilder();
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
		for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
			if (null != workbook.getSheetAt(numSheets)) {
				HSSFSheet aSheet = workbook.getSheetAt(numSheets);
				for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet
						.getLastRowNum(); rowNumOfSheet++) {
					if (null != aSheet.getRow(rowNumOfSheet)) {
						HSSFRow aRow = aSheet.getRow(rowNumOfSheet);
						for (short cellNumOfRow = 0; cellNumOfRow <= aRow
								.getLastCellNum(); cellNumOfRow++) {
							if (null != aRow.getCell(cellNumOfRow)) {
								HSSFCell aCell = aRow.getCell(cellNumOfRow);
								if (convertCell(aCell).length() > 0) {
									content.append(convertCell(aCell));
								}
							}
							content.append("\n");
						}
					}
				}
			}
		}
		workbook.close();
		return content.toString();
	}

	// 读取xlsx
	public static String readEXCEL2007(String file) throws IOException {
		StringBuilder content = new StringBuilder();
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
			if (null != workbook.getSheetAt(numSheets)) {
				XSSFSheet aSheet = workbook.getSheetAt(numSheets);
				for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet
						.getLastRowNum(); rowNumOfSheet++) {
					if (null != aSheet.getRow(rowNumOfSheet)) {
						XSSFRow aRow = aSheet.getRow(rowNumOfSheet);
						for (short cellNumOfRow = 0; cellNumOfRow <= aRow
								.getLastCellNum(); cellNumOfRow++) {
							if (null != aRow.getCell(cellNumOfRow)) {
								XSSFCell aCell = aRow.getCell(cellNumOfRow);
								if (convertCell(aCell).length() > 0) {
									content.append(convertCell(aCell));
								}
							}
							content.append("\n");
						}
					}
				}
			}
		}
		workbook.close();
		return content.toString();
	}

	private static String convertCell(Cell cell) {
		NumberFormat formater = NumberFormat.getInstance();
		formater.setGroupingUsed(false);
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}

		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			cellValue = formater.format(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_STRING:
			cellValue = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			cellValue = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			cellValue = Boolean.valueOf(cell.getBooleanCellValue()).toString();
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			cellValue = String.valueOf(cell.getErrorCellValue());
			break;
		default:
			cellValue = "";
		}
		return cellValue.trim();
	}

	public static ArrayList<String> downLoadFiles(String url, String html) {
		ArrayList<String> files = new ArrayList<String>();
		String typeList = "doc, xls, txt, docx, xlsx";
		try {
			List<String> list = getAnchorTagUrls(url, html);
			for (String str : list) {
				String type = str.substring(str.lastIndexOf(".") + 1,
						str.length());
				if (typeList.contains(type)) {
					String file = str.substring(str.lastIndexOf("/") + 1,
							str.length());
					files.add(file);
					downLoadFromUrl(str, file, type);
				}
			}
		} catch (Exception e) {
			logger.error("downLoadFiles error!", e);
		}
		return files;
	}

	public static List<String> getAnchorTagUrls(String source, String html) {
		if (html == null) {
			return null;
		}
		ArrayList<String> list = new ArrayList<String>();
		try {
			int index = 0;
			while (index != -1) {
				index = html.toLowerCase().indexOf("<a ", index);
				if (index != -1) {
					int end = html.indexOf(">", index);
					String str = html.substring(index, end == -1 ? html.length()
							: end);
					str = str.replaceAll("\\s*=\\s*", "=");
					if (str.toLowerCase().matches("^<a.*href\\s*=\\s*[\'|\"]?.*")) {
						int hrefIndex = str.toLowerCase().indexOf("href=");
						int leadingQuotesIndex = -1;
						if ((leadingQuotesIndex = str.indexOf("\"", hrefIndex
								+ "href=".length())) != -1) {
							int TrailingQuotesIndex = str.indexOf("\"",
									leadingQuotesIndex + 1);
							TrailingQuotesIndex = TrailingQuotesIndex == -1 ? str
									.length() : TrailingQuotesIndex;
							str = str.substring(leadingQuotesIndex + 1,
									TrailingQuotesIndex);
							str = urlHandler(str, source);
							list.add(str);
							index += "<a ".length();
							continue;
						}
						if ((leadingQuotesIndex = str.indexOf("\'", hrefIndex
								+ "href=".length())) != -1) {
							int TrailingQuotesIndex = str.indexOf("\'",
									leadingQuotesIndex + 1);
							TrailingQuotesIndex = TrailingQuotesIndex == -1 ? str
									.length() : TrailingQuotesIndex;
							str = str.substring(leadingQuotesIndex + 1,
									TrailingQuotesIndex);
							str = urlHandler(str, source);
							list.add(str);
							index += "<a ".length();
							continue;
						}
						int whitespaceIndex = str.indexOf(" ",
								hrefIndex + "href=".length());
						whitespaceIndex = whitespaceIndex == -1 ? str.length()
								: whitespaceIndex;
						str = str.substring(hrefIndex + "href=".length(),
								whitespaceIndex);
						str = urlHandler(str, source);
						list.add(str);
					}
					index += "<a ".length();
				}
			}
		} catch (Exception e) {
			logger.error("getAnchorTagUrls error!", e);
		}
		return list;
	}

	private static String urlHandler(String link, String source) {
		try {
			if (link == null)
				return null;
			link = link.trim();
			if (link.toLowerCase().startsWith("http://")
					|| link.toLowerCase().startsWith("https://")) {
				return link;
			}
			String pare = source.trim();
			if (!link.startsWith("/")) {
				if (pare.endsWith("/")) {
					return pare + link;
				}

				if (source.lastIndexOf("/") == source.indexOf("//") + 1
						|| source.lastIndexOf("/") == source.indexOf("//") + 2) {
					return pare + "/" + link;
				} else {
					int lastSeparatorIndex = source.lastIndexOf("/");
					return source.substring(0, lastSeparatorIndex + 1) + link;
				}
			} else {
				if (source.lastIndexOf("/") == source.indexOf("//") + 1
						|| source.lastIndexOf("/") == source.indexOf("//") + 2) {
					return pare + link;
				} else {
					return source.substring(0,
							source.indexOf("/", source.indexOf("//") + 3))
							+ link;
				}
			}
		} catch (Exception e) {
			logger.error("urlHandler error!", e);
		}
		return null;
	}

	public static void downLoadFromUrl(String urlStr, String fileName,
			String type) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3 * 1000);
			conn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			InputStream inputStream = conn.getInputStream();
			byte[] getData = readInputStream(inputStream);
			File saveDir = new File(save_path);
			if (!saveDir.exists()) {
				saveDir.mkdir();
			}
			File file = new File(saveDir + File.separator + fileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(getData);
			if (fos != null) {
				fos.close();
			}
			System.out.println("info:" + url + " 已下载");
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (Exception e) {
			logger.error("downLoadFromUrl error!", e);
		}
	}

	/**
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static byte[] readInputStream(InputStream inputStream) {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			while ((len = inputStream.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.close();
		} catch (Exception e) {
			logger.error("readInputStream error!", e);
		}
		return bos.toByteArray();
	}

	public static void main(String[] args) throws Exception {
		String url = "http://www.fjjs.gov.cn/xxgk/gs/201404/t20140429_94741.htm";
		String html = Jsoup.connect(url).get().toString();
		String result = ReadDoc(url, html);
		System.out.println(result);
	}

}
