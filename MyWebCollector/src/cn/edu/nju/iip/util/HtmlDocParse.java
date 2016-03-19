package cn.edu.nju.iip.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HtmlDocParse {
	
	private static final Logger logger = LoggerFactory.getLogger(HtmlDocParse.class);

	// 目标网页的url
	private String url;
	// 目标网页的html
	private String html;

	private JavaDownloadFile downloader = new JavaDownloadFile();

	private String destinationDirectory = "D:\\doc";

	public HtmlDocParse(String url, String html) {
		this.url = url;
		this.html = html;
	}

	private List<String> getFileURList() {
		List<String> file_url_list = new ArrayList<String>();
		try {
			Document doc = Jsoup.parse(html);
			doc.setBaseUri(url);
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				String fileUrl = link.attr("abs:href");
				if (fileUrl.contains(".doc") || fileUrl.contains(".xls")) {
					file_url_list.add(fileUrl);
				}
			}
		} catch (Exception e) {
			logger.error("getFileURList error!",e);
		}
		return file_url_list;
	}

	public String readExcel(String destionationFilePath) {
		String content = "";
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(new File(destionationFilePath));
			int sheetNum = workbook.getNumberOfSheets();
			for (int i = 0; i < sheetNum; i++) {
				Sheet sheet = workbook.getSheet(i);
				if (sheet != null) {
					int rowCount = sheet.getRows();
					for (int j = 0; j < rowCount; j++) {
						Cell[] cells = sheet.getRow(j);
						if (cells != null) {
							for (int k = 0; k < cells.length; k++) {
								content = content + cells[k].getContents();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("readExcel error!",e);
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
		return content.replaceAll("[\\t\\d\\s]+", "");
	}

	// 读取xlsx
	public String readEXCEL2007(String file) {
		String content = "";
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
				if (null != workbook.getSheetAt(numSheets)) {
					XSSFSheet aSheet = workbook.getSheetAt(numSheets);
					for (int rowNumOfSheet = 0; rowNumOfSheet < aSheet.getLastRowNum(); rowNumOfSheet++) {
						if (null != aSheet.getRow(rowNumOfSheet)) {
							XSSFRow cells = aSheet.getRow(rowNumOfSheet);
							for (short cellNumOfRow = 0; cellNumOfRow <= cells.getLastCellNum(); cellNumOfRow++) {
								if (null != cells.getCell(cellNumOfRow)) {
									XSSFCell aCell = cells.getCell(cellNumOfRow);
									content = content+aCell.getStringCellValue();
								}
							}
						}
					}
				}
				workbook.close();
			}
		} catch (Exception e) {
			logger.error("readEXCEL2007 error!",e);
		}
		return content.replaceAll("[\\t\\d\\s]+", "");
	}

	public String readWORD(String destionationFilePath) {
		String content = "";
		WordExtractor wordExtractor = null;
		try {
			wordExtractor = new WordExtractor(new FileInputStream(new File(destionationFilePath)));
			content = wordExtractor.getText();
			wordExtractor.close();
		} catch (Exception e) {
			logger.error("readWORD error!",e);
		}
		return content.replaceAll("[\\t\\d\\s]+", "");
	}

	// 读取docx文件
	public String readWORD2007(String destionationFilePath) {
		String content = "";
		try {
			XWPFWordExtractor Extractor = new XWPFWordExtractor(POIXMLDocument.openPackage(destionationFilePath));
			content = Extractor.getText();
			Extractor.close();
		} catch (Exception e) {
			logger.error("readWORD2007 error!",e);
		}
		return content.replaceAll("[\\t\\d\\s]+", "");
	}

	public String getDocsContent() {
		String doc_content = "";
		List<String> file_url_list = getFileURList();
		try {
			for (String fileUrl : file_url_list) {
				String destionationFilePath = downloader.download(fileUrl,destinationDirectory);
				File file = new File(destionationFilePath);
				if(!file.exists()) {
					continue;
				}
				if (destionationFilePath.contains(".docx")) {
					doc_content = doc_content + "###" + destionationFilePath+ "###\n";
					doc_content = doc_content + readWORD2007(destionationFilePath)+"\n";
				}else if (destionationFilePath.contains(".xlsx")) {
					doc_content = doc_content + "###" + destionationFilePath+ "###\n";
					doc_content = doc_content + readEXCEL2007(destionationFilePath)+ "\n";
				}else if (destionationFilePath.contains(".xls")) {
					doc_content = doc_content + "###" + destionationFilePath+ "###\n";
					doc_content = doc_content + readExcel(destionationFilePath)+ "\n";
				}else if (destionationFilePath.contains(".doc")) {
					doc_content = doc_content + "###" + destionationFilePath+ "###\n";
					doc_content = doc_content + readWORD(destionationFilePath)+ "\n";
				}
			}
		} catch (Exception e) {
			logger.error("getDocsContent error!",e);
		}
		return doc_content;
	}

	public static void main(String[] args) {
	}
}
