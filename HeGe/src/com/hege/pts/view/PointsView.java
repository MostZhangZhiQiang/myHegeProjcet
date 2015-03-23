package com.hege.pts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

public class PointsView extends View {

	ShapeDrawable selected_drawable;
	public PointsView(Context context) {
		this(context,null);
		// TODO 自动生成的构造函数存根
	}

	public PointsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO 自动生成的构造函数存根
	}
	
	public PointsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		selected_drawable.draw(canvas);
	}
	
	private void initPointers(){
		OvalShape shader=new OvalShape();
		OvalShape shader1=new OvalShape();
		ShapeDrawable selected_drawable=new ShapeDrawable(shader);
		ShapeDrawable unselect_drawable=new ShapeDrawable(shader1);
		selected_drawable.getPaint().setStyle(Paint.Style.FILL);
		selected_drawable.getPaint().setColor(Color.parseColor("#ffffff"));
		unselect_drawable.getPaint().setStyle(Paint.Style.FILL);
		unselect_drawable.getPaint().setColor(Color.parseColor("#90ffffff"));
	}
}
