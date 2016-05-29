package cn.edu.nju.iip.dao;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.model.TBBZ;
import cn.edu.nju.iip.util.CommonUtil;
import cn.edu.nju.iip.util.MyContentExtractor;

/**
 * 公路水运建设市场从业企业 通报表彰表DAO
 * @author wangqiang
 *
 */
public class TBBZDAO extends DAO{
	
private static final Logger logger = LoggerFactory.getLogger(TBBZDAO.class);
private static CORPINFODAO dao = new CORPINFODAO();
	

    @Override
	public boolean saveData(RawHtml raw_html) {
		try{
			TBBZ Data = new TBBZ();
			String pubUnit = MyContentExtractor.getPubUnit(raw_html.getHtml());//发布单位
			String pdate = MyContentExtractor.getPdate(raw_html.getHtml());//表彰时间
			String code = MyContentExtractor.getCode(raw_html.getHtml());
			String year = MyContentExtractor.getAwardYear(raw_html.getHtml());
			Data.setUnit(pubUnit);
			Data.setIssue_Date(pdate);
			Data.setCode(code);
			Data.setYear(year);
			
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setcDate(new Date());// 录入时间
			Data.setuDate(Data.getcDate());
			Data.setIndustry(raw_html.getIndustry());
			Data.setData_Source(raw_html.getUrl());
			Data.setCorp_Id(dao.fetchID(raw_html.getUnitName()));		
			Data.setCorp_Name(raw_html.getUnitName());
			
			Data.setWebContent(CommonUtil.getCleanContent(ContentExtractor.getContentByHtml(raw_html.getHtml())));
			Data.setWebLevel(raw_html.getType());
			Data.setWebName(raw_html.getSource());
			Data.setWebTitle(raw_html.getTitle().replaceAll("下一篇：", ""));

			if(!abstractContent(Data)) {
				return false;
			}
			begin();
			getSession().save(Data);
			commit();
			return true;
		}catch(Exception e) {
			rollback();
			logger.error("TBBZDAO saveData failed!",e);
		}
		return false;
	}
    
	
	/**
	 * 正文摘要
	 * @param Data
	 */
	public boolean abstractContent(TBBZ Data) {
		String content = Data.getWebContent();
		String[] sentences = content.split("\\s+");
		for (String sentence : sentences) {
			if (sentence.contains("关于表彰")&&sentence.contains("的")) {
				sentence = sentence.trim().replaceAll("[？>]", "");
				int index = sentence.indexOf("关于表彰");
				int index2 = sentence.indexOf("的");
				try {
					sentence = sentence.substring(index+4,index2);
				}catch(Exception e) {
					continue;
				}
				Data.setTitle(sentence);
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		ConstructComETL road_TBBZ_etl = new ConstructComETL("公路建设企业","表彰",new TBBZDAO());
		Thread thread = new Thread(road_TBBZ_etl);
		thread.start();
	}

}
