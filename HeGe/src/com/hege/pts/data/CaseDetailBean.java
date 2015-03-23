package com.hege.pts.data;

public class CaseDetailBean {

	private String title;
	private String[] photos;
	private String[] share_url;
	private String cate;
	private String tel;
	private String content;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getPhotos() {
		return photos;
	}
	public void setPhotos(String[] photos) {
		this.photos = photos;
	}
	public String getCate() {
		return cate;
	}
	public void setCate(String cate) {
		this.cate = cate;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
