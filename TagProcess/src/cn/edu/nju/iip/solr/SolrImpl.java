package cn.edu.nju.iip.solr;


import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.util.Config;

/**
 * Solr
 * @author wangqiang
 *
 */
public class SolrImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(SolrImpl.class);
	
	private static final String SolrHost = Config.getValue("SolrHost");
	
	
	/**
	 * 查询
	 * @param query_words 查询词
	 * @param start 起始文档位置
	 * @param rows  每页文档数
	 * @return
	 */
	public static QueryResponse queryDocuments(String query_words){
		try {
			SolrClient solr = new HttpSolrClient(SolrHost);
			SolrQuery query = new SolrQuery();
			query.setQuery(query_words);
			query.setRows(Integer.MAX_VALUE);// 每次取多少条
			QueryResponse response = solr.query(query);
			solr.close();
			return response;
		}catch(Exception e) {
			logger.info("queryDocuments error",e);
		}
		return null;
	}

}
