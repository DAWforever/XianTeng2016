package cn.edu.nju.iip.BloomFilter;

import java.io.File;

import cn.edu.nju.iip.util.Config;



public class BloomFactory {
	private static BloomFilter<String> bf; 
	private static BloomFactory bloomFactory; 
	private String path;

	private BloomFactory() {
		path = System.getProperty("user.dir") + "/bloom/bloomfilter.bloom";
		File file = new File(path);
		if (file.exists()) {
			bf = new BloomFilter<String>(file);
		} else {
			bf = new BloomFilter<String>(1, Integer.parseInt(Config.getValue("bloomSize")));
		}
	}

	public static BloomFactory getInstance() {
		if(bloomFactory==null) {
			synchronized (BloomFactory.class) {
				if (bloomFactory == null) {
					bloomFactory = new BloomFactory();
				}
		    }
		}
		return bloomFactory;
	}

	/**
	 * save BoolmFilter
	 * 
	 * @return
	 */
	public synchronized void saveBloomFilter() {
		bf.save(path);
	}

	/**
	 * 增加
	 * 
	 * @param url
	 * @param Class_type
	 * @param type
	 * @return
	 */
	public synchronized void add(String url) {
		bf.add(url);
	}

	/**
	 * 是否已经存在URL
	 * 
	 * @param url
	 * @param Class_type
	 * @param type
	 * @return
	 */
	public boolean contains(String url) {
		if (bf.contains(url)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		BloomFactory bf = BloomFactory.getInstance();
		//bf.add("http://bbs.cqdj520.cn/forum.php?mod=forumdisplay&fid=68&filter=author&orderby=dateline&typeid=542");
		//bf.saveBloomFilter();
		System.out.println(bf.contains("http://www.hebgs.gov.cn/Yw/yp/ygs_bszn_content.asp?ArticleID=68464"));
	}
	
}