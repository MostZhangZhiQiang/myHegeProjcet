package com.hege.pts.view;

import com.hege.pts.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

public class ProgressView extends View {

	private int marginLeft;
	private int width;
	public void setRate(float rate) {
		this.marginLeft = (int) (rate*width/2);
		inValidataView();
	}

	private void inValidataView() {
		if(Looper.getMainLooper()==Looper.myLooper()){
			invalidate();
		}else{
			postInvalidate();
		}
	}

	public ProgressView(Context context) {
		this(context, null);
		// TODO 自动生成的构造函数存根
	}

	public ProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO 自动生成的构造函数存根
	}
	public ProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		width = this.getWidth();
		drawBack(canvas);//画背景色
		drawFock(canvas,marginLeft);//画前景色
	}

	private void drawFock(Canvas canvas,int marginLeft) {
		Paint paint = new Paint();
		paint.setColor(Color.rgb(230, 145, 145));
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawRect(marginLeft,0,this.getWidth()/2+marginLeft,this.getHeight(), paint);	
	}

	private void drawBack(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.rgb(222, 222, 222));
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawRect(0,0,this.getWidth(),this.getHeight(), paint);
	}
}
