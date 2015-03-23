package com.hege.pts;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.HonorsActivity.GetDataRunnable;
import com.hege.pts.adapter.ImagePagerAdapter;
import com.hege.pts.data.AdvertItemBean;
import com.hege.pts.data.TitlePhotoBean;
import com.hege.pts.tools.WebService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import android.net.Uri;
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

public class EquipmentDiscriptionActivity extends Activity {

	private final static int GET_PHOTO=1;
	private ArrayList<View> img_list = new ArrayList<View>();
	ArrayList<TitlePhotoBean> titlePhotoList;
	private ImagePagerAdapter pagerAdapter;
	
	private ViewPager viewpage;
	private LinearLayout linear_points;
	private TextView equipment_title;
	//画点相关
	private ShapeDrawable unselect_drawable,selected_drawable;
	private DisplayMetrics displayMetrics;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipment_discription);
		Intent intent = getIntent();
		setTitle(intent.getStringExtra("title"));
		initPointers();
		initView();
		changeImageHeight();
		getPhoto();
		addListener();	
		addViewBack();
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
				EquipmentDiscriptionActivity.this.finish();//结束当前activity以返回上一级
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
		new Thread(new GetDataRunnable()).start(); //启动线程获得后台数据
		pagerAdapter = new ImagePagerAdapter();
		viewpage.setAdapter(pagerAdapter);
	}

	/*
	 * 在LinearLayout中画点
	 * @linear_points 传入的布局视图
	 * @size 要画点的数目
	 * @position 每几个点是高亮显示的
	 */
	private void drawPoints(LinearLayout linear_points, int size,
			int position) {
		
		equipment_title.setText(titlePhotoList.get(position).getTitle());
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
			case GET_PHOTO:
				addViewData((ArrayList<TitlePhotoBean>)msg.obj,img_list);
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
			titlePhotoList = new ArrayList<TitlePhotoBean>();
			try {
				WebService.getTitlePhoto(titlePhotoList);
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
			msg.what = GET_PHOTO; //已经获得数据的标志
			msg.obj = titlePhotoList;
			handler.sendMessage(msg);
		}
		
	}
	
    private void addViewData(ArrayList<TitlePhotoBean> titlePhotoList,ArrayList<View> img_list) {
    	for(int i=0;i<titlePhotoList.size();i++){
			View view = LayoutInflater.from(this).inflate(
					R.layout.item_viewpager_main_advert, null);
			ImageView iv_advert = (ImageView) view
					.findViewById(R.id.iv_1);
			//以后为view设置监听
			Uri url = Uri.parse("http://news.pts80.net/hege/"+titlePhotoList.get(i).getPhoto());
			//Picasso.with(EquipmentDiscriptionActivity.this).load(url).fit().into(iv_advert);
			ImageLoader.getInstance().displayImage(url.toString(), iv_advert);
			img_list.add(view);
		}
    }
}
