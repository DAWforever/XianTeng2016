package cn.edu.nju.iip.model;

import java.util.Date;

/**
 * 公路水运建设市场从业企业 通报表彰表对应实体
 * @author wangqiang
 *
 */
public class TBBZ {
	
	private String id;
	//企业id
	private String corp_Id;
	//所属行业
	private String industry;
	//文号(*)
	private String code;
	//表彰年度(*)
	private String year;
	//发布单位
	private String unit;
	//发布日期
	private String issue_Date;
	//录入时间
	private Date cDate;
	//跟新时间
	private Date uDate;
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
  //企业名
  	private String corp_Name;
	
	
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
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getIssue_Date() {
		return issue_Date;
	}
	public void setIssue_Date(String issue_Date) {
		this.issue_Date = issue_Date;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
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
