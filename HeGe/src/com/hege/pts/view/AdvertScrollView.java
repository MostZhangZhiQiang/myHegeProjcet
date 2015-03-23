package com.hege.pts.view;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import android.widget.ImageView.ScaleType;

import com.hege.pts.R;

public class AdvertScrollView  extends RelativeLayout implements OnPageChangeListener{
	private ViewPager viewPager;
	private LinearLayout linear_pointers;
	private Animation anim_left_in,anim_left_out,anim_right_in,anim_right_out;
	private int curPos=0;
	private ShapeDrawable unselect_drawable,selected_drawable;
	private DisplayMetrics displayMetrics;
	private ChangeRunnable changeRunnable;

	public AdvertScrollView(Context context) {
		super(context);
		initView(context);
	}

	public AdvertScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public AdvertScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context){
		displayMetrics=getContext().getResources().getDisplayMetrics();
		changeRunnable=new ChangeRunnable();
		
		initPointers();
		viewPager=new ViewPager(getContext());
		viewPager.setOnPageChangeListener(this);
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		addView(viewPager,params);
		linear_pointers=new LinearLayout(getContext());
		linear_pointers.setPadding(10, 0, 10, 10);
		linear_pointers.setOrientation(LinearLayout.HORIZONTAL);
		linear_pointers.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams params1=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		addView(linear_pointers,params1);
	}
	/**
	 * 点图片
	 */
	private void initPointers(){
		OvalShape shader=new OvalShape();
		OvalShape shader1=new OvalShape();
		selected_drawable=new ShapeDrawable(shader);
		unselect_drawable=new ShapeDrawable(shader1);
		selected_drawable.getPaint().setStyle(Paint.Style.FILL);
		selected_drawable.getPaint().setColor(Color.parseColor("#ffffff"));
		unselect_drawable.getPaint().setStyle(Paint.Style.FILL);
		unselect_drawable.getPaint().setColor(Color.parseColor("#90ffffff"));
	}
	public void setSelectedDrawableColor(int color){
		selected_drawable.getPaint().setColor(color);
	}
	public void setUnSelectDrawableColor(int color){
		unselect_drawable.getPaint().setColor(color);
	}
	
	/**
	 * 添加滚动图片
	 * @param list
	 */
	public void addData(PagerAdapter adapter){
		if(adapter!=null&&adapter.getCount()>0){
			curPos = 0;
			viewPager.setAdapter(adapter);
			linear_pointers.removeAllViews();
			if(adapter.getCount()>1){
				for(int i=0;i<adapter.getCount();i++){
					LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0,0);
					params2.leftMargin = 10;
					/* 广告下的点 */
					ImageView img_pointer = new ImageView(getContext());
					img_pointer.setScaleType(ScaleType.FIT_CENTER);
					if (i == curPos) {
						params2.width=(int)(8*displayMetrics.scaledDensity);
						params2.height=(int)(8*displayMetrics.scaledDensity);
						img_pointer.setBackgroundDrawable(selected_drawable);
					} else {
						params2.width=(int)(5*displayMetrics.scaledDensity);
						params2.height=(int)(5*displayMetrics.scaledDensity);
						img_pointer.setBackgroundDrawable(unselect_drawable);
					}
					img_pointer.setLayoutParams(params2);
					linear_pointers.addView(img_pointer);
				}
			}
			viewPager.postDelayed(changeRunnable, 5000);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		if (linear_pointers.getChildCount() > 1) {
			int oldPos=curPos;
			curPos=arg0;
			LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
					(int) (8 * displayMetrics.scaledDensity),
					(int) (8 * displayMetrics.scaledDensity));
			params1.leftMargin = 10;
			linear_pointers.getChildAt(curPos)
					.setBackgroundDrawable(selected_drawable);
			linear_pointers.getChildAt(curPos).setLayoutParams(params1);
			
			LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
					(int) (5 * displayMetrics.scaledDensity),
					(int) (5 * displayMetrics.scaledDensity));
			params2.leftMargin = 10;
			linear_pointers.getChildAt(oldPos)
					.setBackgroundDrawable(unselect_drawable);
			linear_pointers.getChildAt(oldPos).setLayoutParams(params2);
			viewPager.removeCallbacks(changeRunnable);
			viewPager.postDelayed(changeRunnable, 5000);
		}
	}
	
	class ChangeRunnable implements Runnable{

		@Override
		public void run() {
			int pos=curPos+1;
			if(pos>=linear_pointers.getChildCount()){
				pos=0;
				viewPager.setCurrentItem(0, false);
			}else {
				viewPager.setCurrentItem(pos, true);
			}
		}
		
	}
}
