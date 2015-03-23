package com.hege.pts.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.HorizontalScrollView;

public class SlidScrollView extends HorizontalScrollView {

	private float scollWidth=0;
	private float rate=0;
	private ProgressView progressView;
	public SlidScrollView(Context context) {
		super(context,null);
		// TODO 自动生成的构造函数存根
	}
	
	public SlidScrollView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		// TODO 自动生成的构造函数存根
	}
	
	public SlidScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO 自动生成的方法存根
		super.onScrollChanged(l, t, oldl, oldt);
		if(0==scollWidth){
			scollWidth=this.getChildAt(0).getWidth()-this.getWidth();
		}

		rate = l/scollWidth; //得到比例 
	    progressView.setRate(rate);
	}

	public float getRate(float rate) {
		return  rate;
	}

	public void setProgressView(ProgressView progressView) {
		// TODO 自动生成的方法存根
		this.progressView = progressView;
	}
}
