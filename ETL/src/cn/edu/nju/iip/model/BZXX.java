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
	//业户名
	private String corp_Name;
	//表彰名称
	private String bz_Name;
	//发布单位
	private String unit;
	//录入时间
	private Date cDate;
	//更新时间
    private Date uDate;
	//表彰时间
	private String pDate;
	//数据来源
	private String data_Source;
	//附件名
	private String fileName;
	//网站名称
    private String webName;
    //正文内容
    private String webContent;
    //关注度等级
    private String webLevel;
    //标题
    private String webTitle;
	
    
	
	public String getBz_Name() {
		return bz_Name;
	}
	public void setBz_Name(String bz_Name) {
		this.bz_Name = bz_Name;
	}
	public String getWebName() {
		return webName;
	}
	public void setWebName(String webName) {
		this.webName = webName;
	}
	public String getWebContent() {
		return webContent;
	}
	public void setWebContent(String webContent) {
		this.webContent = webContent;
	}
	public String getWebLevel() {
		return webLevel;
	}
	public void setWebLevel(String webLevel) {
		this.webLevel = webLevel;
	}
	public String getWebTitle() {
		return webTitle;
	}
	public void setWebTitle(String webTitle) {
		this.webTitle = webTitle;
	}
	
    
	public String getCorp_Name() {
		return corp_Name;
	}
	public void setCorp_Name(String corp_Name) {
		this.corp_Name = corp_Name;
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
