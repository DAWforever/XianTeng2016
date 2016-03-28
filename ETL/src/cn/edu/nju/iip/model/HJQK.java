package cn.edu.nju.iip.model;

import java.util.Date;

/**
 * 公路水运建设市场从业企业 获奖情况表对应实体
 * 
 * @author wangqiang
 * 
 */
public class HJQK {
	private String award_id;
	// 企业id
	private String corp_Id;
	// 奖项名称
	private String name;
	// 获奖年度
	private String year;
	// 奖项等级类别
	private String type;
	// 奖项等级
	private String type_Name;
	// 所属行业
	private String industry;
	// 颁奖单位
	private String unit;
	// 颁奖日期
	private Date pDate;
	// 录入时间
	private Date cdate;
	//内容
	private String content;
	// 来源
	private String data_Source;
	
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAward_id() {
		return award_id;
	}
	public void setAward_id(String award_id) {
		this.award_id = award_id;
	}
	public String getCorp_Id() {
		return corp_Id;
	}
	public void setCorp_Id(String corp_Id) {
		this.corp_Id = corp_Id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType_Name() {
		return type_Name;
	}
	public void setType_Name(String type_Name) {
		this.type_Name = type_Name;
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
	public Date getpDate() {
		return pDate;
	}
	public void setpDate(Date pDate) {
		this.pDate = pDate;
	}
	
	public Date getCdate() {
		return cdate;
	}
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	public String getData_Source() {
		return data_Source;
	}
	public void setData_Source(String data_Source) {
		this.data_Source = data_Source;
	}
	
	

}
