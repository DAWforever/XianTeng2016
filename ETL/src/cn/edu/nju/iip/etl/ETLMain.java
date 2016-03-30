package cn.edu.nju.iip.etl;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.nju.iip.util.CommonUtil;
import cn.edu.nju.iip.util.Config;


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
		int period =  Integer.valueOf(Config.getValue("period"))*60*60*1000;
		executorService.scheduleAtFixedRate(ETLTask,delayTime,period,TimeUnit.MILLISECONDS);
	}

}
