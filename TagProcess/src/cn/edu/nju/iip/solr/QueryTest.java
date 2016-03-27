package cn.edu.nju.iip.solr;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class QueryTest implements Runnable{


	public static void main(String[] args) throws SolrServerException,IOException {
		ExecutorService service = Executors.newCachedThreadPool();
		QueryTest test = new QueryTest();
		service.execute(test);
		service.shutdown();
	}

	@Override
	public void run() {
		QueryResponse rsp = SolrImpl.queryDocuments("\"万州区凉风建筑工程公司\"");
		SolrDocumentList list = rsp.getResults();
		System.out.println(list.size());
	}
}
