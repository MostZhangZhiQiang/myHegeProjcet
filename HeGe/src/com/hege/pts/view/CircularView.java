package com.hege.pts.view;

import com.hege.pts.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class CircularView extends View {

	private int mCircularColor;
	private String str;
	private Paint paint = new Paint();
	public CircularView(Context context) {
		this(context, null);
		// TODO 自动生成的构造函数存根
	}

	public CircularView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		// TODO 自动生成的构造函数存根
	}
	
	public CircularView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CircularView);
		
		int n = a.getIndexCount();
		
		for(int i=0;i<n;i++){
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.CircularView_color:
				mCircularColor = a.getColor(attr, 0xff000000);				
				break;
			case R.styleable.CircularView_text:
				str = a.getString(attr);
			default:
				break;
			}
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = this.getWidth()/2;
		String[] texts= str.split(" ");
		paint.setColor(mCircularColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3.0f);
		paint.setAntiAlias(true);
		paint.setDither(true);
		canvas.drawCircle(height, height, height-2, paint);
		
		//绘制文本
		paint.reset();
		paint.setColor(0xff747677);
		paint.setTextSize(30.0f);
		Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
		paint.setTypeface( font );
		canvas.drawText(texts[0],22, height-15, paint);
		paint.reset();
		paint.setColor(0xff747677);
		paint.setTextSize(25.0f);
		canvas.drawText(texts[1],22, height+15, paint);
	}
}
