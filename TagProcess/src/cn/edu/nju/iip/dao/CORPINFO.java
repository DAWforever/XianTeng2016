package cn.edu.nju.iip.dao;

public class CORPINFO {
	private String corp_id;
	private String corp_name;
	private String corp_type;
	private String corp_shortNames;
	
	
	
	public String getCorp_shortNames() {
		return corp_shortNames;
	}
	public void setCorp_shortNames(String corp_shortNames) {
		this.corp_shortNames = corp_shortNames;
	}
	public String getCorp_id() {
		return corp_id;
	}
	public String getCorp_name() {
		return corp_name;
	}
	public String getCorp_type() {
		return corp_type;
	}

	public void setCorp_id(String corp_id) {
		this.corp_id = corp_id;
	}
	public void setCorp_name(String corp_name) {
		this.corp_name = corp_name;
	}
	public void setCorp_type(String corp_type) {
		this.corp_type = corp_type;
	}
}
