package cn.edu.nju.iip.etl;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.util.CommonUtil;


public class ETLMain implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(ETLMain.class);
	
	@Override
	public void run() {
		try{
			ConstructComETL.ConstructComETLMain();
		}catch(Exception e) {
			logger.error("TagMain run() failed", e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
		ETLMain ETLTask = new ETLMain();
		long delayTime = CommonUtil.getDelayTime();//延迟时间
		executorService.scheduleAtFixedRate(ETLTask,delayTime,CommonUtil.one_day_millseconds,TimeUnit.MILLISECONDS);
	}

}
