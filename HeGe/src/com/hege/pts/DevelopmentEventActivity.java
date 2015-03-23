package com.hege.pts;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.HonorsActivity.GetDataRunnable;
import com.hege.pts.adapter.ImagePagerAdapter;
import com.hege.pts.data.AdvertItemBean;
import com.hege.pts.data.YearMessageBean;
import com.hege.pts.tools.WebService;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class DevelopmentEventActivity extends Activity {

	private LinearLayout linear_points;
	private ViewPager mViewPager;
	private ArrayList<View> view_list = new ArrayList<View>();
	
	private ImagePagerAdapter pagerAdapter;
	
	private final static int GET_DEVELOPMENT_DATA = 1;
	//画点相关
	private ShapeDrawable unselect_drawable,selected_drawable;
	private DisplayMetrics displayMetrics;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_development_event);
		changeImageHeight();
		Intent intent = getIntent();
		setTitle(intent.getStringExtra("title"));
		initPointers();
		displayMetrics=this.getResources().getDisplayMetrics();
		linear_points = (LinearLayout) this.findViewById(R.id.development_line_points);
		mViewPager = (ViewPager) this.findViewById(R.id.development_viewpage);
		addListener();
		getData();
		addViewBack();
	}

    private void changeImageHeight() {
		ImageView imageView = (ImageView) this.findViewById(R.id.course_top_bg);
		android.view.ViewGroup.LayoutParams pm = imageView.getLayoutParams();
		pm.height = (int) (0.75f*this.getResources().getDisplayMetrics().widthPixels);
	}

	private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DevelopmentEventActivity.this.finish();//结束当前activity以返回上一级
			}
		});
	}
	private void getData(){
		new Thread(new GetDataRunnable()).start(); //启动线程获得后台数据
		pagerAdapter = new ImagePagerAdapter();
		mViewPager.setAdapter(pagerAdapter);
	}
	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}
	
	/**
	 * 点图片
	 */
	private void initPointers(){
		OvalShape shader1=new OvalShape();
		unselect_drawable=new ShapeDrawable(shader1);
		unselect_drawable.getPaint().setStyle(Paint.Style.FILL);
		unselect_drawable.getPaint().setColor(Color.parseColor("#6f6c72"));
	}
	
	private void addListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				drawPoints(linear_points,view_list.size(),position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
    private Handler handler= new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DEVELOPMENT_DATA:
				pagerAdapter.addData((ArrayList<View>)msg.obj);
				drawPoints(linear_points,view_list.size(),0);
				break;
			default:
				break;
			}
		};
	};
	
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
		TextView textView = null;
		
		for(int i=0;i<size;i++){
			params2 = new LinearLayout.LayoutParams(0,0);
			params2.leftMargin = 30;
			textView = new TextView(linear_points.getContext());
			
			if (i == position) {
				params2.width=(int)(25*displayMetrics.scaledDensity);
				params2.height=(int)(25*displayMetrics.scaledDensity);
				textView.setBackgroundDrawable(unselect_drawable);
				textView.setGravity(android.view.Gravity.CENTER);
				textView.setTextSize(20.0f);
				textView.setTypeface(null, Typeface.BOLD);
				String str = getFormatStr(i);
				textView.setText(str);
			} else {
				params2.width=(int)(10*displayMetrics.scaledDensity);
				params2.height=(int)(10*displayMetrics.scaledDensity);
				textView.setBackgroundDrawable(unselect_drawable);
			}
			textView.setLayoutParams(params2);
			linear_points.addView(textView);
		}
	}
	
	private String getFormatStr(int i) {
		i = i+1;
		String str = null;
		if(i<10){
			str = "0"+i;
		}else{
			str = Integer.toString(i);
		}
		return str;
	}

	class GetDataRunnable implements Runnable{
		@Override
		public void run() {
			ArrayList<YearMessageBean> yearMessageBean = new ArrayList<YearMessageBean>();
			try {
				WebService.getEvents(yearMessageBean);
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
			addViewData(yearMessageBean,view_list);
			Message msg = Message.obtain();
			msg.what = GET_DEVELOPMENT_DATA; //已经获得数据的标志
			msg.obj = view_list;
			handler.sendMessage(msg);
		}
		
	}
	
    private void addViewData(ArrayList<YearMessageBean> yearMessageListBean,ArrayList<View> view_list) {
    	for(int i=0;i<yearMessageListBean.size();i++){
			View view = LayoutInflater.from(this).inflate(
					R.layout.development_pager_item, null);
			TextView year_tv = (TextView) view.findViewById(R.id.year_tv);
			TextView information_tv = (TextView) view.findViewById(R.id.information_tv);
			year_tv.setText(yearMessageListBean.get(i).getYear());
			information_tv.setText(yearMessageListBean.get(i).getInformation());
			view_list.add(view);
		}
    }
}
