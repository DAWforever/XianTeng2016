package cn.edu.nju.iip.model;

import java.util.Date;

/**
 * 道路运输市场从业企业 表彰信息表对应实体
 * 
 * @author wangqiang
 * 
 */
public class BZXX {
	private String id;
	//业户id
	private String corp_Id;
	//业户id
	private String corp_Name;
	//表彰名称
	private String title;
	//发布单位
	private String unit;
	//录入时间
	private Date cDate;
	//录入时间
    private Date uDate;
	//表彰时间
	private String pDate;
	//内容
	private String content;
	//数据来源
	private String data_Source;
	//附件名
	private String fileName;
	//类别
	private String type;
	
	
	public String getCorp_Name() {
		return corp_Name;
	}
	public void setCorp_Name(String corp_Name) {
		this.corp_Name = corp_Name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getuDate() {
		return uDate;
	}
	public void setuDate(Date uDate) {
		this.uDate = uDate;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getpDate() {
		return pDate;
	}
	public void setpDate(String pDate) {
		this.pDate = pDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCorp_Id() {
		return corp_Id;
	}
	public void setCorp_Id(String corp_Id) {
		this.corp_Id = corp_Id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Date getcDate() {
		return cDate;
	}
	public void setcDate(Date cDate) {
		this.cDate = cDate;
	}
	public String getData_Source() {
		return data_Source;
	}
	public void setData_Source(String data_Source) {
		this.data_Source = data_Source;
	}
}
