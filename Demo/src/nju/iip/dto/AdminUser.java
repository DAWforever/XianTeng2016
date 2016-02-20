package nju.iip.dto;

/**
 * 管理员用户
 * @author wangqiang
 *
 */
public class AdminUser {
	//id
	private int id;
	//用户名
	private String username;
	//密码
	private String password;
	//注册时间
	private String registeDate;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRegisteDate() {
		return registeDate;
	}
	public void setRegisteDate(String registeDate) {
		this.registeDate = registeDate;
	}
	
	

}
