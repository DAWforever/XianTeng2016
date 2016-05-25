package cn.edu.nju.iip.dao;

import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.etl.ConstructComETL;
import cn.edu.nju.iip.model.HJQK;
import cn.edu.nju.iip.model.RawHtml;
import cn.edu.nju.iip.util.CommonUtil;

/**
 * 公路水运建设市场从业企业获奖情况表DAO
 * @author wangqiang
 *
 */
public class HJQKDAO extends DAO {
	
	private static final Logger logger = LoggerFactory.getLogger(HJQKDAO.class);
	
	private static CORPINFODAO dao = new CORPINFODAO();

	
	@Override
	public boolean saveData(RawHtml raw_html) {
		try{
			HJQK Data = new HJQK();
//			Data.setpDate(raw_html.getCrawltime());
			Data.setFileName(CommonUtil.getAttachFileName(raw_html.getAttachment()));
			Data.setCdate(new Date());
			Data.setUdate(new Date());
			Data.setContent(raw_html.getContent());
			Data.setIndustry(raw_html.getIndustry());
			Data.setUnit(raw_html.getSource());
			Data.setData_Source(raw_html.getUrl());
			Data.setCorp_Id(dao.fetchID(raw_html.getUnitName()));
			Data.setCorp_Name(raw_html.getUnitName());
			Data.setType(raw_html.getType());
			Data.setType_Name(raw_html.getSource().contains("市")?"市级":"省级");
			extractField(Data);
			if(!abstractContent(Data)){
				return false;
			}
			begin();
			getSession().save(Data);
			commit();
			return true;
		}catch(Exception e) {
			rollback();
			logger.error("HJQKDAO saveData failed!",e);
		}
		return false;
	}
	
	/**
	 * 抽取正文字段
	 * @param Data
	 */
	public void extractField(HJQK Data) {

		String content = Data.getContent();
		
		String year = "";
		String name = "";
		String code = "";
		String pdate = "";
		String unit = "";
		
		Matcher match =  null;
		
		Pattern yearPattern = Pattern.compile("([0-9]{4}(年度))|(〔[0-9]{4}〕)");
		match = yearPattern.matcher(content);
		
		if(match.find()){
			String findString =  match.group();			
			year = findString.replace("〔", "").replace("〕","").replace("年度", "");							
		}
		
		Pattern award = Pattern.compile("(国家|中国|全国|河北省|山西省|辽宁省|吉林省|黑龙江省|江苏省|浙江省|安徽省|福建省|江西省|山东省|河南省|湖北省|湖南省|广东省|海南省|四川省|贵州省|云南省|陕西省|甘肃省|青海省|台湾省|内蒙古自治区|广西壮族自治区|西藏自治区|宁夏回族自治区|新疆维吾尔自治区|北京市|天津市|上海市|重庆市|香港特别行政区|澳门特别行政区|[0-9]{4}年度)([\u4e00-\u9fa5“”]*?)(奖)");
		match = award.matcher(content);
		
		while(match.find()){
			String findString =  match.group();
			if (findString.contains("年度")) {
				findString = findString.substring(6);
			}
			if (findString.length() < 25) {
				name = findString;
				break;
			}
		}
		
		Pattern codePattern = Pattern.compile("([\u4e00-\u9fa5]{2,6})(［|〔|（|\\[|\\(|【)[0-9]{4}(］|）|\\)|\\]|】|〕)(.?[0-9]{1,4}.?)(号?)");
		match = codePattern.matcher(content);
		
		if(match.find()){
			code =  match.group();			
		}
		
		Data.setYear(year);
		Data.setName(name);
		Data.setCode(code);
//		if(name.contains("国家") || name.contains("中国")|| name.contains("全国") ){
//			Data.setType("2");
//			Data.setType_Name("国家级");
//		}else if(name.contains("省")){
//			Data.setType("3");
//			Data.setType_Name("省级");
//		}else if(name.contains("市")){
//			Data.setType("4");
//			Data.setType_Name("市级");
//		}else{
//			Data.setType("9");
//			Data.setType_Name("其它");
//		}
		
		
		Pattern pattern = Pattern.compile("([\u4e00-\u9fa5]{1,20}(会|室|厅|站|府|局|部|院|所|处))(\\s| | )+([0-9]{4}|(二...))年.{1,2}月.{1,3}日");
		match = pattern.matcher(content);
		
		String str = "";
		while(match.find()){
			str = match.group();
		}
		
		if(str.length() < 10){
			Pattern datePattern = Pattern.compile("([0-9]{4}-[0-9]{2}-[0-9]{2})|(([0-9]{4}|(二...))年.{1,2}月.{1,3}日)");
			match = datePattern.matcher(content);
			if(match.find()){
				pdate = match.group();
			}		
		}else{

			Pattern datePattern = Pattern.compile("([0-9]{4}|(二...))年.{1,2}月.{1,3}日");
			match = datePattern.matcher(str);
			if(match.find()){
				pdate = match.group();
			}
			unit = str.replace(pdate, "").trim();
		}
		
		Data.setpDate(pdate);
		if(!unit.equals("")) {
			System.out.println("unit="+unit);
			Data.setUnit(unit);
		}
	}
	
	
	/**
	 * 正文摘要
	 * @param Data
	 */
	public boolean abstractContent(HJQK Data) {
		String content = Data.getContent();
		String[] sentences = content.split("[\\s。？]+");
		for (String sentence : sentences) {
			if (sentence.contains("奖")&&(sentence.contains("关于")||sentence.contains("名单")||sentence.contains("决定")||sentence.contains("通知"))) {
				if(sentence.contains("关于")) {
					int index = sentence.indexOf("关于");
					sentence = sentence.substring(index);
				}
				if (sentence.length() > 50) {
					sentence = sentence.substring(0, 50);
				}
				sentence = sentence.replace(".doc", "");
				Data.setContent(sentence);
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		ConstructComETL road_HJQK_etl = new ConstructComETL("公路建设企业","获奖",new HJQKDAO());
		Thread thread = new Thread(road_HJQK_etl);
		thread.start();
	}

}
