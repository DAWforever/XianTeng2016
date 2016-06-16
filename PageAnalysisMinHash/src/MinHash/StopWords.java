package MinHash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class StopWords {
	private static final String filepath = "data/stopwords.txt";
	private static final HashSet<String> stopwords = new HashSet<String>();
	
	static{
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void load() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));
		String line = "";
		while((line = br.readLine()) != null){
			stopwords.add(line);
		}
		br.close();
	}
	
	public static HashSet<String> getStopWords(){
		return stopwords;
	}
}
