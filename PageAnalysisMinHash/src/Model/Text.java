package Model;

public class Text {
	private String clear_id;
	private int life_time;
	
	public Text (String clear_id){
		this.clear_id = clear_id;
		this.life_time = 0;
	}
	
	public String getClear_id() {
		return clear_id;
	}
	
	public int getLife_time() {
		return life_time;
	}
	
	public void setClear_id(String clear_id) {
		this.clear_id = clear_id;
	}
	
	public void setLife_time(int life_time) {
		this.life_time = life_time;
	}
}
