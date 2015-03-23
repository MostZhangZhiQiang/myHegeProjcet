package com.hege.pts;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.NewsActivity.GetDataRunnable;
import com.hege.pts.adapter.ImagePagerAdapter;
import com.hege.pts.data.AdvertItemBean;
import com.hege.pts.tools.WebService;
import com.hege.pts.view.AdvertScrollView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class HonorsActivity extends Activity {

	private final static int GET_HONOR_PHOTO=1;
	private ArrayList<View> img_list = new ArrayList<View>();
	private ImagePagerAdapter pagerAdapter;
	
	private ViewPager honor_viewpage;
	private LinearLayout linear_points;
	//画点相关
	private ShapeDrawable unselect_drawable,selected_drawable;
	private DisplayMetrics displayMetrics;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_honors);
		displayMetrics=this.getResources().getDisplayMetrics();
		Intent intent = getIntent();
		setTitle(intent.getStringExtra("title"));
		initPointers();
		initView();
		getPhoto();
		addListener();	
		addViewBack();
	}
	
    private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HonorsActivity.this.finish();//结束当前activity以返回上一级
			}
		});
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
	
	private void addListener() {
		honor_viewpage.setOnPageChangeListener(new OnPageChangeListener() {
			
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
		honor_viewpage = (ViewPager) this.findViewById(R.id.honor_viewpage);
		linear_points = (LinearLayout) this.findViewById(R.id.line_points);
	}

	private void getPhoto(){
		new Thread(new GetDataRunnable()).start(); //启动线程获得后台数据
		pagerAdapter = new ImagePagerAdapter();
		honor_viewpage.setAdapter(pagerAdapter);
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}
	
	/*
	 * 在LinearLayout中画点
	 * @linear_points 传入的布局视图
	 * @size 要画点的数目
	 * @position 每几个点是高亮显示的
	 */
	private void drawPoints(LinearLayout linear_points, int size,
			int position) {
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
	
    private Handler handler= new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_HONOR_PHOTO:
				WebService.addImage2(HonorsActivity.this, img_list,(ArrayList<String>)msg.obj);
				pagerAdapter.addData(img_list);
				drawPoints(linear_points,img_list.size(),0);
				break;
			default:
				break;
			}
		};
	};
	
	class GetDataRunnable implements Runnable{
		@Override
		public void run() {
			ArrayList<String> listPhoto = new ArrayList<String>();
			try {
				WebService.getListPhoto(listPhoto);
			} catch (ClientProtocolException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			Message msg = Message.obtain();
			msg.what = GET_HONOR_PHOTO; //已经获得数据的标志
			msg.obj = listPhoto;
			handler.sendMessage(msg);
		}
		
	}
}
