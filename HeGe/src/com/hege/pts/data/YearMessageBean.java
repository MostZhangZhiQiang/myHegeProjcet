package com.hege.pts.data;

public class YearMessageBean {

	private String year;
	private String information;
	
	public YearMessageBean(){}
	public YearMessageBean(String year, String information) {
		super();
		this.year = year;
		this.information = information;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
}
