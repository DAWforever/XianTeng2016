package PageAnalysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Config.Config;
import DAO.Dao;
import MinHash.IKCutWord;
import MinHash.MinHash;
import Model.DT_CLEAR_DATA;
import Model.MINHASH_CODE;
import Model.Time;
import Model.Text;

public class PageAnalysis {
	long dbTime = 0;
	long timeTime = 0;
	long paTime = 0;
	long hashTime = 0;
	long saveTime = 0;
	
	private static int HashFuncNum = 20;
	private static int Accuracy = 13;
	
	HashMap<String, ArrayList<Text>> hashcode_2_id_map = new  HashMap<String, ArrayList<Text>>();
	
	public static String PageAnal(String html){
		Document doc = Jsoup.parse(html);

		Elements es = doc.getAllElements();
		int temp_length = 0;
		Element longest = es.first();
		for (int i = 0; i < es.size(); i++){
			Element temp = es.get(i);
			temp.removeAttr("href");
			temp.removeAttr("src");
			if (temp.tagName().equals("img")){
				temp.remove();
				continue;
			}
			if (temp.tagName().equals("p") && temp.text().length() > temp_length){
				temp_length = temp.text().length();
				longest = temp;
			}
			else if (!temp.tagName().equals("div") && !temp.ownText().contains("='") && !temp.ownText().contains("=\"") && temp.ownText().length() > temp_length){
				temp_length = temp.ownText().length();
				longest = temp;
			}
		}
		
		String content_tag_name = longest.tagName();
		String all = "";
		if (longest.parent() == null){
			all = longest.text();
			all += "|&|" + longest.toString();
		}
		else{
			String temp = HTMLText(longest.parent().parent(), content_tag_name);
			all = Text(longest.parent().parent(), content_tag_name);
			all += "|&|" + temp;
		}
		
		return all;
	}
	
	public static String HTMLText(Element e, String content_tag_name){
		String result = "";
		for (Element ee : e.children()){
			int flag = 0;
			if (ee.tagName().equals("p")){
				result += ee;
				flag = 1;
			}
			else if (ee.tagName().equals(content_tag_name)){
				result += ee;
				flag = 1;
			}
			else {
				if (ee.children() == null){
//					System.out.println("叶节点"+ee.tagName());
				}
				else{
					String temp = HTMLText(ee, content_tag_name);
					if (!temp.equals("")){
						result += temp;
						flag = 1;
					}
				}
			}
			if (flag == 0){
				ee.remove();
			}
		}
		return result;
		
	}
	
	public static String Text(Element e, String content_tag_name){
		String result = "";
		for (Element ee :e.children()){
			if (ee.tagName().equals("p"))
				result += ee.text();
			else if (ee.tagName().equals(content_tag_name))
				result += ee.ownText();
			else
				result += Text(ee, content_tag_name);
		}
		return result;
	}
	
	public void run() throws Exception{
		Dao dao = new Dao();
		int page = 0;
		int offset = dao.getStart("DT_CLEAR_DATA");
		System.out.println("偏移量: " + offset);
		Long batchNo = System.currentTimeMillis();
		System.out.println(" 批次号: " + batchNo);
		
		
		ResultSet rawData;
		ArrayList<DT_CLEAR_DATA> result;
		ArrayList<MINHASH_CODE> hashcodeSet;
		DT_CLEAR_DATA temp;
		MINHASH_CODE hc;
		String contextHtml;
		String[] split;
		long startMill = 0, endMill = 0;
		Time time = null;
		
		long click = System.currentTimeMillis();
		long start = System.currentTimeMillis();
		while (true){
			// 获取数据
			startMill = System.currentTimeMillis();
			List rawDataList = dao.getRAWDATA(offset, page);	
			Connection conn = (Connection) rawDataList.get(1) ;
			rawData = (ResultSet) rawDataList.get(0) ;					//url,spider_date,text,herfText,source
			endMill = System.currentTimeMillis();
			dbTime += endMill - startMill;
			if (!rawData.next()){
				System.out.println("------此次分析" + page + "页------");
				System.out.println("ZZZzzz进入睡眠zzzZZZ");
				try {
					Thread.sleep(Integer.parseInt(Config.getValue("SLEEPTIME")));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				page = 0;
				offset = dao.getStart("DT_CLEAR_DATA");
				System.out.println("偏移量 " + offset);
				batchNo = System.currentTimeMillis();
				continue;
			}
			System.out.println("----获取第" + page + "页成功----");
			result = new ArrayList<DT_CLEAR_DATA>();
			hashcodeSet = new ArrayList<MINHASH_CODE>();
			rawData.previous();
			while (rawData.next()){
				temp = new DT_CLEAR_DATA(rawData);
				startMill = System.currentTimeMillis();
				time = DateAnal.getHtmlTime(temp.getText());
				endMill = System.currentTimeMillis();
				
				timeTime += endMill - startMill;
				if (time != null)
					temp.setPubDate(time.toString());
				
				// 正文提取
				startMill = System.currentTimeMillis();
				contextHtml = PageAnal(temp.getText());
				endMill = System.currentTimeMillis();
				paTime += endMill - startMill;
				split = contextHtml.split("\\|\\&\\|");
				temp.setText(split[0]);
				temp.setHtmlText(split[1]);

				// 计算哈希值
				startMill = System.currentTimeMillis();
				//System.out.println(temp.getText());
				ArrayList<String> words = IKCutWord.getWords(temp.getText());
				ArrayList<Long> hash_code = MinHash.hash(words);
				hc = new MINHASH_CODE(temp, hash_code);
				
				// 判断重复
				boolean tag = false;
				String text_content = temp.getText();
				if (text_content == null || text_content.trim().length() == 0)
					tag = false;
				else if (hashcode_2_id_map.size() == 0){
					tag = true;
					insert(hc);
				}else{
					tag = isSim(hc);
					if (tag == true)
						insert(hc);	
					}
				
				
				hashcodeSet.add(hc);
				hashTime += endMill - startMill;
				temp.setBatchNo(String.valueOf(batchNo));
				temp.setDuplicate_tag(tag);
				result.add(temp);
			}
			conn.close();
			System.out.println("----分析第" + page + "页成功----");
			
			
			// 保存数据
			startMill = System.currentTimeMillis();
			dao.saveCLEARDATA(result);
			//dao.saveMINHASHCODE(hashcodeSet);
			endMill = System.currentTimeMillis();
			saveTime += endMill - startMill;
			System.out.println("----写入第" + page + "页成功----");
			result.clear();
			page++;
			
			if (System.currentTimeMillis() - click > 100000){
				click = System.currentTimeMillis();
				System.out.println("取数耗时：" + dbTime + " 时间耗时：" + timeTime + " 正文耗时：" + paTime + " hash耗时：" + hashTime + " 存数耗时：" + saveTime);
			}
			
			long current = System.currentTimeMillis();
			if (current - start >= 1000 * 60 * 60 *24){
				hashcode_2_id_map = update(hashcode_2_id_map);
				start = current;
			}
		}
	}
	
	/*
	 * 更新特征码－ID的HashMap，生命周期小于等于6的自增1，大于6的移除
	 */
	private HashMap<String, ArrayList<Text>> update(HashMap<String, ArrayList<Text>> hashcode_2_id_map) {
		Set<String> keySet = hashcode_2_id_map.keySet();
		for (String key : keySet){
			ArrayList<Text> text_list = hashcode_2_id_map.get(key);
			for (int i = 0; i < text_list.size(); i++){
				Text text = text_list.get(i);
				int life_time = text.getLife_time();
				if (life_time <= 6){
					life_time++;
					text.setLife_time(life_time);
				}else{
					text_list.remove(i);
				}
			}
		}
		return hashcode_2_id_map;
	}

	private void insert(MINHASH_CODE mhc) {
		// TODO Auto-generated method stub
		for (int i = 0; i < HashFuncNum; i++){
			String key = Integer.toString(i) + mhc.getHash_code().get(i);
			String docID = mhc.getID();
			if (hashcode_2_id_map.get(key) == null){
				ArrayList<Text> temp = new ArrayList<Text>();
				Text text = new Text(docID);
				temp.add(text);
				hashcode_2_id_map.put(key, temp);
			}else{
				ArrayList<Text> temp = hashcode_2_id_map.get(key);
				Text text = new Text(docID);
				temp.add(text);
				hashcode_2_id_map.put(key, temp);
			}
		}
	}


	private boolean isSim(MINHASH_CODE mhc) {
		HashMap<String, Integer> rslt = new HashMap<String, Integer>();
		
		for (int i = 0; i < 20; i++){
			String key = Integer.toString(i) + mhc.getHash_code().get(i);
			if (hashcode_2_id_map.get(key) != null){
				ArrayList<Text> temp = hashcode_2_id_map.get(key);
				for (Text text : temp){
					String id = text.getClear_id();
					if (rslt.get(id) == null)
						rslt.put(id, 1);
					else
						rslt.put(id, rslt.get(id) + 1);
				}
			}
		}
		
		for (String key : rslt.keySet()){
			if (rslt.get(key) >= Accuracy){
				return false;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		PageAnalysis pa = new PageAnalysis();
		pa.run();
	}
}
