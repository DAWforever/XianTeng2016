package Model;

import java.util.ArrayList;

import Config.Config;

public class MINHASH_CODE {
	private int rawid;
	private String ID;
	private ArrayList<Long> hash_code;
	private ArrayList<String> hash_code_str;
	
	public MINHASH_CODE(){
		this.rawid = 0;
		this.setHash_code_str(new ArrayList<String>());
	}
	
	public MINHASH_CODE(int rawid, ArrayList<Long> hash_code, int hash_func_num) {
		this.rawid = rawid;
		this.hash_code = hash_code;
	}

	public MINHASH_CODE(DT_CLEAR_DATA temp, ArrayList<Long> hash_code) {
		this.rawid = temp.getRawID();
		this.ID =rawid  + "_" + Config.getValue("NODENAME");
		this.hash_code = hash_code;
	}

	public ArrayList<Long> getHash_code() {
		return hash_code;
	}

	public void setHash_code(ArrayList<Long> hash_code) {
		this.hash_code = hash_code;
	}

	public int getRawid() {
		return rawid;
	}

	public void setRawid(int rawid) {
		this.rawid = rawid;
	}

	public ArrayList<String> getHash_code_str() {
		return hash_code_str;
	}

	public void setHash_code_str(ArrayList<String> hash_code_str) {
		this.hash_code_str = hash_code_str;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
}
