package com.hege.pts.data;

import android.graphics.Bitmap;

public class ContactBean {

	private int returns;
	private String title;
	private String address;
	private String tel;
	private String fax;
	private String url;
	private String email;
	private String qq;
	private String weixin;
	private String weixin_img;
    private Bitmap image;
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	private String address_location;
	public int getReturns() {
		return returns;
	}
	public void setReturns(int returns) {
		this.returns = returns;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public String getWeixin_img() {
		return weixin_img;
	}
	public void setWeixin_img(String weixin_img) {
		this.weixin_img = weixin_img;
	}
	public String getAddress_location() {
		return address_location;
	}
	public void setAddress_location(String address_location) {
		this.address_location = address_location;
	}
}
