package MinHash;

import java.util.ArrayList;

public class MinHash {
	private final static int[] seed = {
		2,2503,5003,7499,
		10007,12503,15013,17497,
		19997,22501,24989,27509,
		29989,32503,35023,37501,
		40009,42499,45007,49999
	};
	
	private final static int ACCURACY = 7;
	private final static int HASH_FUNC_NUM = 20;
	
	public static ArrayList<Long> hash(ArrayList<String> words){
		ArrayList<Long> hash_code = new ArrayList<Long>();
		
		for (int i = 0; i < seed.length; i++)
			hash_code.add(Long.MAX_VALUE);
		
		for (int i = 0; i < seed.length; i++){
			for (String word : words){
				long temp_hash_code = HashJenkins.hash(word.getBytes(), seed[i]);
				if (temp_hash_code < hash_code.get(i))
					hash_code.set(i, temp_hash_code);
			}
		}
		
		return hash_code;
	}
	
	public static boolean isSim(ArrayList<Long> hash_code_A, ArrayList<Long> hash_code_B){
		int total_sim_code = 0;
		for (int i = 0; i < HASH_FUNC_NUM; i++){
			if (hash_code_A.get(i) != hash_code_B.get(i))
				total_sim_code++;
		}
		
		boolean isSim = (total_sim_code > ACCURACY) ? false : true;
		return isSim;
	}

	/*
	public static void main(String[] args) {
		String test = "正如上面所列举的，当要将ArrayList类型的数据转换为String[]的时候，必须对List类型进行遍历，其实没有这种必要，List提供给我们一个很好的方法解决List转换成为数组的问题，不防再看一个例子";
		ArrayList<String> words = IKCutWord.getWords(test);
		ArrayList<Long> hash_code = MinHash.hash(words);
		for (Long l : hash_code)
			System.out.println(l);
	}
	*/
}
