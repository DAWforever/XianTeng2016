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
	//标题
	private String title;
	//文号(*)
	private String code;
	//表彰年度(*)
	private String year;
	//发布单位
	private String unit;
	//发布日期
	private Date issue_Date;
	//录入时间
	private Date cDate;
	//内容
	private String content;
	//数据来源
	private String data_Source;
	//附件名
	private String fileName;
	
	
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
	public Date getIssue_Date() {
		return issue_Date;
	}
	public void setIssue_Date(Date issue_Date) {
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getData_Source() {
		return data_Source;
	}
	public void setData_Source(String data_Source) {
		this.data_Source = data_Source;
	}

}
