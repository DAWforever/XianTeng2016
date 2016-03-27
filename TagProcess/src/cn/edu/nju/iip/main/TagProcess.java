package cn.edu.nju.iip.main;

import java.util.List;
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
	
	public TagProcess(BlockingQueue<String> NameQueue) {
		this.NameQueue = NameQueue;
	}
	
	public void tagOne(String tag_name,String tag_type) {
		String query = "\""+tag_name+"\"";
		Jedis jedis = JedisPoolUtils.getInstance().getJedis();
		QueryResponse rsp = SolrImpl.queryDocuments(query);
		SolrDocumentList list = rsp.getResults();
		logger.info(tag_name+" "+list.getNumFound()+"");
        for (SolrDocument solrDocument : list) {
        	String id = "jw_raw_data:"+(String) solrDocument.getFieldValue("id");
            jedis.sadd(id, tag_type+":"+tag_name);
            jedis.sadd(tag_type+":"+tag_name, id);
        }
        JedisPoolUtils.getInstance().returnRes(jedis);
	}
	
	public void tagAll() {
		List<String> unitNameList = CommonUtil.importConsShipUnitName();
		for(String unitName:unitNameList) {
			tagOne(unitName,"construct:com");
		}
	}
	
	public static void main(String[] args) {
		BlockingQueue<String> NameQueue = new LinkedBlockingQueue<String>();
		List<String> unitNameList = CommonUtil.importConsShipUnitName();
		NameQueue.addAll(unitNameList);
		logger.info("count="+unitNameList.size());
		ExecutorService service = Executors.newCachedThreadPool();
		for(int i=0;i<5;i++) {
			TagProcess process = new TagProcess(NameQueue);
			service.execute(process);
		}
		service.shutdown();
//		TagProcess process = new TagProcess(NameQueue);
//		process.tagOne("处罚","credit");
	}

	@Override
	public void run() {
		while(true) {
			String unitName = NameQueue.poll();
			if(unitName!=null) {
				tagOne(unitName,"construct:com");
			}
			else {
				break;
			}
		}
		
	}

}
