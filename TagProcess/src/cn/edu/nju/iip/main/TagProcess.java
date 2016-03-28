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
	
	//实体标签分类 construct or transport
	private String tag_type;
	
	
	public TagProcess(BlockingQueue<String> NameQueue,String tag_type) {
		this.NameQueue = NameQueue;
		this.tag_type = tag_type;
	}
	
	public void tagOne(String tag_name) {
		try{
			String query = "\""+tag_name+"\"";
			Jedis jedis = JedisPoolUtils.getInstance().getJedis();
			QueryResponse rsp = SolrImpl.queryDocuments(query);
			SolrDocumentList list = rsp.getResults();
	        for (SolrDocument solrDocument : list) {
	        	String id = "jw_gov_data:"+(String) solrDocument.getFieldValue("id");
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
				tagOne(unitName);
			}
			else {
				break;
			}
		}
	}
	
	public static void TagProcessMain(String tag_name,String tag_type) {
		BlockingQueue<String> NameQueue = new LinkedBlockingQueue<String>();
		Set<String> unitNameSet= CommonUtil.getUnitNameSet(tag_name);
		NameQueue.addAll(unitNameSet);
		logger.info(tag_name+" count="+unitNameSet.size());
		ExecutorService service = Executors.newCachedThreadPool();
		for(int i=0;i<5;i++) {
			TagProcess process = new TagProcess(NameQueue,tag_type);
			service.execute(process);
		}
		service.shutdown();
	}
	
	public static void tag_process_main() {
		TagProcessMain("水运建设企业","construct");
		TagProcessMain("公路建设企业","construct");
		TagProcessMain("道路运输企业","transport");
		TagProcessMain("信用相关标签","credit");
	}

}
