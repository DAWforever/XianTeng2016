package cn.edu.nju.iip.main;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import cn.edu.nju.iip.redis.JedisPoolUtils;
import cn.edu.nju.iip.solr.SolrImpl;
import cn.edu.nju.iip.util.CommonUtil;

public class TagProcess implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(TagProcess.class);
	
	private BlockingQueue<String> NameQueue;
	
	//实体标签分类
	private String tag_type;
	
	
	public TagProcess(BlockingQueue<String> NameQueue,String tag_type) {
		this.NameQueue = NameQueue;
		this.tag_type = tag_type;
	}
	
	/**
	 * 打某一个标签
	 * @param tag_name
	 */
	public void tagOnetag(String tag_name) {
		try{
			String query = "\""+tag_name+"\"";
			Jedis jedis = JedisPoolUtils.getInstance().getJedis();
			QueryResponse rsp = SolrImpl.queryDocuments(query);
			SolrDocumentList list = rsp.getResults();
	        for (SolrDocument solrDocument : list) {
	        	String id = "jw_raw_data:"+(String) solrDocument.getFieldValue("id");
	            jedis.sadd(id, tag_type+":"+tag_name);
	            jedis.sadd(tag_type+":"+tag_name, id);
	        }
	        JedisPoolUtils.getInstance().returnRes(jedis);
		}catch(Exception e) {
			logger.error("tagOne error",e);
		}
	}
	
	@Override
	public void run() {
		while(true) {
			String unitName = NameQueue.poll();
			if(unitName!=null) {
				tagOnetag(unitName);
			}
			else {
				logger.info(Thread.currentThread().getName()+"#"+tag_type+" tag finish...");
				break;
			}
		}
	}
	
	/**
	 * 打某一分类下的所有标签
	 * @param tag_type
	 */
	public static void TagOneType(String tag_type) {
		BlockingQueue<String> NameQueue = new LinkedBlockingQueue<String>();
		Set<String> unitNameSet= CommonUtil.getUnitNameSet(tag_type);
		NameQueue.addAll(unitNameSet);
		ExecutorService service = Executors.newCachedThreadPool();
		for(int i=0;i<5;i++) {
			TagProcess process = new TagProcess(NameQueue,tag_type.split(":")[1]);
			service.execute(process);
		}
		service.shutdown();
	}
	
	public static void tagProcessMain() {
		Jedis jedis = JedisPoolUtils.getInstance().getJedis();
		try{
			//取出标签库中所有标签类
			Set<String> TaglibSet = jedis.keys("Taglib*");
			JedisPoolUtils.getInstance().returnRes(jedis);
			for(String tag_type:TaglibSet) {
				TagOneType(tag_type);
			}
		}catch(Exception e) {
			logger.error("tagProcessMain error",e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		tagProcessMain();
	}

}
