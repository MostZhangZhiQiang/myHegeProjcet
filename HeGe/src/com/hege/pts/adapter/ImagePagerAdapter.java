package com.hege.pts.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ImagePagerAdapter extends PagerAdapter {

	private  ArrayList<View> img_list = new ArrayList<View>();
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return img_list.size();
	}

	public void addData(ArrayList<View> img_list){
		this.img_list.addAll(img_list);
		this.notifyDataSetChanged();
	}
    @Override  
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡         
         container.addView(img_list.get(position), 0);//添加页卡  
         return img_list.get(position);  
    }  
    
    @Override  
    public void destroyItem(ViewGroup container, int position, Object object)   {     
        container.removeView(img_list.get(position));//删除页卡  
    }  
    
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO 自动生成的方法存根
		return arg0==arg1;
	}

}
