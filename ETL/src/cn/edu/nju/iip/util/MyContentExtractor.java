package cn.edu.nju.iip.util;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网页正文抽取类
 * @author  
 *
 */
public class MyContentExtractor {
	
	private static final Logger logger = LoggerFactory.getLogger(MyContentExtractor.class);
	
	/**
	 * 抽取发布时间
	 * @return
	 */
	public static String getPdate(String html) {
		String pdate = null; 
		try{
			pdate = ContentExtractor.getNewsByHtml(html).getTime();
		}catch(Exception e) {
			logger.info("getPdate error");
		}
		if(pdate==null) {
			return getMyPdate(html);
		}
		else{
			if(pdate.length()>"2010-07-12".length()) {
				pdate = pdate.split(" ")[0];
				pdate = pdate.replaceAll("-", "/");
				return pdate;
			}
			else {
				return getMyPdate(html);
//				String[] str = pdate.split("-");
//				int year = Integer.valueOf(str[0]);
//				int mon = Integer.valueOf(str[1]);
//				int day = Integer.valueOf(str[2]);
//				if(year>2030||year<2000||mon>12||day>31) {
//					return getMyPdate(html);
//				}
//				else {
//					return pdate;
//				}
			}
		}
	}
	
	public static String getMyPdate(String html) {
		String content = Jsoup.parse(html).text();
		
		String pdate = "";
		
		Pattern datePattern = Pattern.compile("((20)[0-9]{2}(-|/|-)[0-9]{1,2}(-|-|/)[0-9]{1,2})");			
		
		Matcher match = datePattern.matcher(content);
		
		if(match.find()){
			pdate = match.group();
		}else{
			Pattern datePattern2 = Pattern.compile("(([0-9]{4})年[0-9]{1,2}月[0-9]{1,2}日)");
			match = datePattern2.matcher(content);
			if(match.find()){
				pdate = match.group();
			}		
		}
		
		
		String year = "";
		String month = "";
		String day = "";
		
		int index = 0;
		
		while(index < pdate.length()){
			if(pdate.charAt(index) <= '9' && pdate.charAt(index) >= '0'){
				year += pdate.charAt(index);
				index ++;
			}else{
				break;
			}
		}
		
		index ++;
		
		while(index < pdate.length()){
			if(pdate.charAt(index) <= '9' && pdate.charAt(index) >= '0'){
				month += pdate.charAt(index);
				index ++;
			}else{
				break;
			}
		}
		
		index++;
		
		while(index < pdate.length()){
			if(pdate.charAt(index) <= '9' && pdate.charAt(index) >= '0'){
				day += pdate.charAt(index);
				index ++;
			}else{
				break;
			}
		}
		try{
			if(Integer.parseInt(year) <= 2030 && 
					   Integer.parseInt(year) >= 1990 && 
			     	   Integer.parseInt(month) >= 1 && 
			     	   Integer.parseInt(month) <= 12 &&	
			     	   Integer.parseInt(day) >= 1 &&
			     	   Integer.parseInt(day) <= 31
					){
						pdate = year+"/"+month+"/"+day;
					}else{
						pdate = "";
					}
		}catch(Exception e) {
			pdate = "";
		}
		if(pdate!=null) {
			pdate = pdate.replaceAll("-", "/").replaceAll("年", "/").replaceAll("月", "/").replaceAll("日", "");
		}
		return pdate;
	}
	
	/**
	 * 抽取发布单位
	 * @return
	 */
	public static String getPubUnit(String html) {
		String content = Jsoup.parse(html).text();
		String unit = "";
		String date = "";
		
		Pattern pattern = Pattern.compile("([\u4e00-\u9fa5]{1,20}(会|室|厅|站|府|局|部|院|所|处))(\\s| | )+([0-9]{4}|(二...))年.{1,2}月.{1,3}日");
		Matcher match = pattern.matcher(content);
		
		String str = "";		
		while(match.find()){
			str = match.group();
		}
		
		if(str.length() < 10){
			Pattern datePattern1 = Pattern.compile("([0-9]{4}-[0-9]{2}-[0-9]{2})|(([0-9]{4}|(二...))年.{1,2}月.{1,3}日)");
			match = datePattern1.matcher(content);
			if(match.find()){
				date = match.group();
			}		
		}else{
			Pattern datePattern1 = Pattern.compile("([0-9]{4}|(二...))年.{1,2}月.{1,3}日");
			match = datePattern1.matcher(str);
			if(match.find()){
				date = match.group();
			}
			unit = str.replace(date, "").trim();
		}	
		
		if(!unit.equals("")) {
			Pattern unitPattern = Pattern.compile("([\u4e00-\u9fa5])+");
			match = unitPattern.matcher(unit);			
			if(match.find()){
				unit = match.group();			
			}
			if(unit.length() > 25){
				unit = "";
			}
		}
		return unit;
	}
	
	/**
	 * 抽取文号
	 * @return
	 */
	public static String getCode(String html) {
		
		String content = Jsoup.parse(html).text();
		String code = "";
		
		Pattern codePattern = Pattern.compile("([\u4e00-\u9fa5]{2,6})(［|〔|（|\\[|\\(|【)[0-9]{4}(］|）|\\)|\\]|】|〕)(.?[0-9]{1,4}.?)(号?)");
		Matcher match = codePattern.matcher(content);
		
		if(match.find()){
			code =  match.group();			
		}
		
		return code;
	}
	
	/**
	 * 抽取奖项名称
	 * @return
	 */
	public static String getAwardName(String html) {
		
		String content = Jsoup.parse(html).text();
		String awardName = "";
		
		Pattern award = Pattern.compile("(国家|中国|全国|河北省|山西省|辽宁省|吉林省|黑龙江省|江苏省|浙江省|安徽省|福建省|江西省|山东省|河南省|湖北省|湖南省|广东省|海南省|四川省|贵州省|云南省|陕西省|甘肃省|青海省|台湾省|内蒙古自治区|广西壮族自治区|西藏自治区|宁夏回族自治区|新疆维吾尔自治区|北京市|天津市|上海市|重庆市|香港特别行政区|澳门特别行政区|[0-9]{4}年度)([\u4e00-\u9fa5“”]*?)(奖)");
		Matcher match = award.matcher(content);
		
		while(match.find()){
			String findString =  match.group();
			if (findString.contains("年度")) {
				findString = findString.substring(6);
			}
			if (findString.length() < 25) {
				awardName = findString;
				break;
			}
		}
		
		return awardName;
	}
	
	/**
	 * 抽取获奖年度
	 * @param html
	 * @return
	 */
	public static String getAwardYear(String html) {

		String content = Jsoup.parse(html).text();
		String awardyear = "";
		
		Pattern yearPattern = Pattern.compile("([0-9]{4}(年度))|(〔[0-9]{4}〕)");
		Matcher match = yearPattern.matcher(content);
		
		if(match.find()){
			String findString =  match.group();			
			awardyear = findString.replace("〔", "").replace("〕","").replace("年度", "");							
		}
		
		return awardyear;
	}
	
	/**
	 * 抽取获奖级别
	 * @param html
	 * @return
	 */
	public static String getAwardLevel(String awardName) {
		String typeName = "";	
		
		if(awardName.contains("国家") || awardName.contains("中国")|| awardName.contains("全国") ){
			typeName = "国家级";
		}else if(awardName.contains("省")){
			typeName = "省级";
		}else if(awardName.contains("市")){
			typeName = "市级";
		}else{
			typeName = "其它";
		}
		
		return typeName;
	}
	
	/**
	 * 抽取获奖类别
	 * @param html
	 * @return
	 */
	public static String getAwardType(String awardName) {		
		String type = "";
		
		if(awardName.contains("国家") || awardName.contains("中国")|| awardName.contains("全国") ){
			type = "2";
		}else if(awardName.contains("省")){
			type = "3";
		}else if(awardName.contains("市")){
			type = "4";
		}else{
			type = "9";
		}
		
		return type;
	}
	
	public static void main(String[] args) throws IOException {
		String url = "http://www.hncd.gov.cn/portal/dzzw/zwgg/webinfo/2012/05/1338251542390886.htm";
		System.out.println(getMyPdate(Jsoup.connect(url).get().html()));
	}

}
