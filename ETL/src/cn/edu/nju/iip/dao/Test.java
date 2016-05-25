package cn.edu.nju.iip.dao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String content = "发布日期 ： 2016-04-26  啊";
		String pdate = "";

		Pattern datePattern = Pattern.compile("([0-9]{4}|(二...))(年|-).{1,2}(月|-).{1,3}(日?)");
		Matcher match = datePattern.matcher(content);
		
		while(match.find()){
			pdate = match.group();
		}
		
		
		System.out.println(pdate);
	
	}

}
