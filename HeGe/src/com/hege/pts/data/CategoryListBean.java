package com.hege.pts.data;

import com.hege.pts.tools.ConstValue;

import android.graphics.Bitmap;

public class CategoryListBean {

	private String photo;
	private String code;
	private String priseNum;
	public CategoryListBean(){}

	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPriseNum() {
		return priseNum;
	}
	public void setPriseNum(String priseNum) {
		this.priseNum = priseNum;
	}
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCategoryCode(int id){
		String str = null;
		switch (id) {
		case ConstValue.QING_GANG:
			str="Çá¸Ö±ðÊû/±àºÅ:";
			break;
		case ConstValue.JI_ZHUAN:
			str="¼¯×°Ïä»î¶¯·¿/±àºÅ:";
			break;
		case ConstValue.FO_DONG:
			str="»î¶¯·¿/±àºÅ:";
			break;
		case ConstValue.QING_GANG_JIANG:
			str="Çá¸Ö½¨Öþ/±àºÅ:";
			break;
		case ConstValue.JIA_XING_BAN:
			str="¼ÐÐ¾°å/±àºÅ:";
			break;
		default:
			break;
		}
		return str+code;
	}
}
