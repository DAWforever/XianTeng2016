package cn.edu.nju.iip.spider;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.util.CommonUtil;
import cn.edu.nju.iip.util.Config;

public class MainSpider implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(MainSpider.class);
	
	public static void main(String[] args) throws Exception {
		ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
		long delayTime = CommonUtil.getDelayTime();//延迟时间
		int period =  Integer.valueOf(Config.getValue("period"))*60*60*1000;
		MainSpider spider = new MainSpider();
		logger.info("MainSpider will be start in "+delayTime/(1000*60.0)+" minites...");
		executorService.scheduleAtFixedRate(spider, delayTime,period,TimeUnit.MILLISECONDS);
	}

	public void run() {
		try{
			RawHtmlSpider.startRawHtmlCrawler();
			NewsCrawler.startNewsCrawler();
		}catch(Exception e) {
			logger.error("MainSpider run() failed", e);
		}
	}

}
