package com.hege.pts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class BgDrawLineView extends View {

	private int[] mLocation01;
	private int[] mLocation02;
	private int[] mLocation03;
	private int[] mLocation04;
	private int[] mLocation05;
	private int mHeight;
	private Paint paint;
	private DisplayMetrics metrics;
	private float titleHeight;
    public void setMHeight(int height) {
		this.mHeight = height;
	}
    
	
	public BgDrawLineView(Context context) {
		this(context, null);
		// TODO 自动生成的构造函数存根
	}

	public BgDrawLineView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO 自动生成的构造函数存根
	}
	
	public BgDrawLineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		metrics =this.getResources().getDisplayMetrics();
		paint = new Paint();
		paint.setColor(Color.parseColor("#99ffffff"));
		paint.setStrokeWidth(3.0f);
	    titleHeight = (metrics.density*44);
	}
	public int[] getmLocation02() {
		return mLocation02;
	}

	public void setmLocation02(int[] mLocation02) {
		this.mLocation02 = mLocation02;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO 自动生成的方法存根
		super.onDraw(canvas);
		
		//移动
		canvas.translate(mHeight, mHeight-titleHeight);
		if(mLocation01!=null||mLocation02!=null||mLocation03!=null||mLocation04!=null||mLocation05!=null){
			canvas.drawLine(mLocation01[0],mLocation01[1], mLocation02[0], mLocation02[1], paint);
			canvas.drawLine(mLocation02[0],mLocation02[1], mLocation03[0], mLocation03[1], paint);
			canvas.drawLine(mLocation03[0],mLocation03[1], mLocation04[0], mLocation04[1], paint);
			canvas.drawLine(mLocation04[0],mLocation04[1], mLocation05[0], mLocation05[1], paint);
		}
		canvas.translate(-mHeight, -mHeight);
	}

	public int[] getmLocation01() {
		return mLocation01;
	}

	public void setmLocation01(int[] mLocation01) {
		this.mLocation01 = mLocation01;
	}

	public void setmLocation03(int[] location03) {
		this.mLocation03 = location03;
	}

	public void setmLocation04(int[] location04) {
		this.mLocation04 = location04;
	}

	public void setmLocation05(int[] location05) {
		this.mLocation05 = location05;
	}


}
