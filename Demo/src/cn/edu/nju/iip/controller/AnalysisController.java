package cn.edu.nju.iip.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.nju.iip.util.CommonUtil;


@Controller
public class AnalysisController {
	
	private static final Logger logger = LoggerFactory.getLogger(AnalysisController.class);
	
	@RequestMapping(value = "/SentimentAnalysis")
	public void  SentimentAnalysis(String text,HttpServletResponse response) throws IOException {
		logger.info("SentimentAnalysis called!");
		String result = CommonUtil.SentimentApiExample(text);
		PrintWriter out = response.getWriter();
		out.write(result);
	}

}
