package cn.edu.nju.iip.main;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TagMain implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(TagMain.class);
	//执行周期
	public static final int period = 12 * 60 * 60 * 1000;

	@Override
	public void run() {
		try{
			TagProcess.tag_process_main();
		}catch(Exception e) {
			logger.error("TagMain run() failed", e);
		}
	}
	public static void main(String[] args) throws Exception {
		ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
		TagMain tagTask = new TagMain();
		executorService.scheduleAtFixedRate(tagTask,0,period,TimeUnit.MILLISECONDS);
	}

}
