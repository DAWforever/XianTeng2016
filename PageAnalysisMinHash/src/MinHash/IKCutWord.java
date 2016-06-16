package MinHash;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class IKCutWord {
	public static ArrayList<String> getWords(String text){
		HashSet<String> stopwords = StopWords.getStopWords();
		ArrayList<String> wordSet = new ArrayList<String>();
		
		IKAnalyzer analyzer = new IKAnalyzer();
		try
    	(
    		StringReader reader=new StringReader(text);        
            TokenStream ts=analyzer.tokenStream("", reader);  	
    	)
    	{        
            CharTermAttribute term=ts.getAttribute(CharTermAttribute.class);  

            while(ts.incrementToken()){ 
            	if (stopwords.contains(term.toString()))
            		continue;
            	else
            		wordSet.add(term.toString());
            }  
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    	analyzer.close();
		
		return wordSet;
	}
	
	/*
	public static void main(String[] args) {
		String test = "正如上面所列举的，当要将ArrayList类型的数据转换为String[]的时候，必须对List类型进行遍历，其实没有这种必要，List提供给我们一个很好的方法解决List转换成为数组的问题，不防再看一个例子";
		ArrayList<String> result = IKCutWord.getWords(test);
		for (String str : result)
			System.out.println(str);
	}
	*/
}
