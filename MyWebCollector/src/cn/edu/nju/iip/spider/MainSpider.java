package cn.edu.nju.iip.spider;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.nju.iip.util.CommonUtil;

public class MainSpider implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(MainSpider.class);
	
	public static void main(String[] args) throws Exception {
		ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
		long delayTime = CommonUtil.getDelayTime();//延迟时间
		MainSpider spider = new MainSpider();
		executorService.scheduleAtFixedRate(spider, 0, CommonUtil.one_day_millseconds,TimeUnit.MILLISECONDS);
	}

	public void run() {
		try{
			RawHtmlSpider.startRawHtmlCrawler();
			//NewsCrawler.startNewsCrawler();
		}catch(Exception e) {
			logger.error("MainSpider run() failed", e);
		}
		
	}

}
