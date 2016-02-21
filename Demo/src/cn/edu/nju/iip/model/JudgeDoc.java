package cn.edu.nju.iip.model;

/**
 * 法院裁决文书
 * @author wangqiang
 *
 */
public class JudgeDoc {
	
	private int id;
	//执行法院
	private String courtName;
	//案号
	private String caseCode;
	//身份证号码/组织机构代码
	private String cardNum;
	//省份
	private String areaName;
	//法定代表人或者负责人姓名
	private String businessEntity;
	//生效法律文书确定的义务
	private String duty;
	//被执行人的履行情况
	private String performance;
	//失信被执行人行为具体情形
	private String disruptTypeName;
	//发布时间
	private String publishDate;
	//被执行人姓名
	private String iname;
	
	
	
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public String getCaseCode() {
		return caseCode;
	}
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getBusinessEntity() {
		return businessEntity;
	}
	public void setBusinessEntity(String businessEntity) {
		this.businessEntity = businessEntity;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getPerformance() {
		return performance;
	}
	public void setPerformance(String performance) {
		this.performance = performance;
	}
	public String getDisruptTypeName() {
		return disruptTypeName;
	}
	public void setDisruptTypeName(String disruptTypeName) {
		this.disruptTypeName = disruptTypeName;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	
	


}
