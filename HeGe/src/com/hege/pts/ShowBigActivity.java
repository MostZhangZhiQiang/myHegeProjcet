package com.hege.pts;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hege.pts.adapter.ImagePagerAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShowBigActivity extends Activity {
	
	private ArrayList<View> img_list = new ArrayList<View>();
	ArrayList<String> titles;
	private ImagePagerAdapter pagerAdapter;
	
	private ViewPager viewpage;
	private LinearLayout linear_points;
	private TextView equipment_title;
	//画点相关
	private ShapeDrawable unselect_drawable,selected_drawable;
	private DisplayMetrics displayMetrics;
	
	//数据相关
	private ArrayList<String> title;
	private ArrayList<String> urls;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipment_discription);
		Intent intent = getIntent();
		setTitle(intent.getStringExtra("title"));
		urls = intent.getStringArrayListExtra("urls");
		titles = intent.getStringArrayListExtra("titles");
		initPointers();
		initView();
		changeImageHeight();
		
		addListener();	
		addViewBack();
		
		getPhoto();
	}
	
	 private void changeImageHeight() {
			android.view.ViewGroup.LayoutParams pm = viewpage.getLayoutParams();
			pm.height = (int) (1.078125f*this.getResources().getDisplayMetrics().widthPixels);
	 }

    private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ShowBigActivity.this.finish();//结束当前activity以返回上一级
			}
		});
	}
    
	/**
	 * 点图片
	 */
	private void initPointers(){
		displayMetrics=this.getResources().getDisplayMetrics();
		OvalShape shader=new OvalShape();
		OvalShape shader1=new OvalShape();
		selected_drawable=new ShapeDrawable(shader);
		unselect_drawable=new ShapeDrawable(shader1);
		selected_drawable.getPaint().setStyle(Paint.Style.FILL);
		selected_drawable.getPaint().setColor(Color.parseColor("#ffffff"));
		unselect_drawable.getPaint().setStyle(Paint.Style.FILL);
		unselect_drawable.getPaint().setColor(Color.parseColor("#90ffffff"));
	}
	
	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}
	
	private void addListener() {
		viewpage.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				drawPoints(linear_points,img_list.size(),position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void initView() {
		viewpage = (ViewPager) this.findViewById(R.id.image_viewpage);
		linear_points = (LinearLayout) this.findViewById(R.id.line_points);
		equipment_title = (TextView) this.findViewById(R.id.equipment_title);
	}

	private void getPhoto(){
		pagerAdapter = new ImagePagerAdapter();
		addViewData(urls,img_list);
		pagerAdapter.addData(img_list);
		viewpage.setAdapter(pagerAdapter);
		drawPoints(linear_points,img_list.size(),0);
	}

	/*
	 * 在LinearLayout中画点
	 * @linear_points 传入的布局视图
	 * @size 要画点的数目
	 * @position 每几个点是高亮显示的
	 */
	private void drawPoints(LinearLayout linear_points, int size,
			int position) {
		
		equipment_title.setText(titles.get(0));
		linear_points.removeAllViews();
		LinearLayout.LayoutParams params2 = null;
		ImageView imageView = null;
		
		for(int i=0;i<size;i++){
			params2 = new LinearLayout.LayoutParams(0,0);
			params2.leftMargin = 10;
			imageView = new ImageView(linear_points.getContext());
			imageView.setScaleType(ScaleType.FIT_CENTER);
			if (i == position) {
				params2.width=(int)(8*displayMetrics.scaledDensity);
				params2.height=(int)(8*displayMetrics.scaledDensity);
				imageView.setBackgroundDrawable(selected_drawable);
			} else {
				params2.width=(int)(5*displayMetrics.scaledDensity);
				params2.height=(int)(5*displayMetrics.scaledDensity);
				imageView.setBackgroundDrawable(unselect_drawable);
			}
			imageView.setLayoutParams(params2);
			linear_points.addView(imageView);
		}
	}
	
    private void addViewData(ArrayList<String> urls,ArrayList<View> img_list) {
    	if(urls!=null){
    		for(int i=0;i<urls.size();i++){
    			View view = LayoutInflater.from(this).inflate(
    					R.layout.item_viewpager_main_advert, null);
    			ImageView iv_advert = (ImageView) view
    					.findViewById(R.id.iv_1);
    			//以后为view设置监听
    			Uri url = Uri.parse("http://news.pts80.net/hege/"+urls.get(i));
    			//Picasso.with(EquipmentDiscriptionActivity.this).load(url).fit().into(iv_advert);
    			ImageLoader.getInstance().displayImage(url.toString(), iv_advert);
    			img_list.add(view);
    		}
    	}
    }
}
