package com.hege.pts;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.AboutUsActivity.GetDataRunnable;
import com.hege.pts.adapter.MainAdverPagerAdapter;
import com.hege.pts.adapter.NewsListAdapter;
import com.hege.pts.data.AdvertItemBean;
import com.hege.pts.data.NewsListBean;
import com.hege.pts.tools.WebService;
import com.hege.pts.view.AdvertScrollView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NewsActivity extends Activity {

	private final static int GET_GUAN_GAO_DATA=1;
	public static final int GET_NEWS_DATA = 0;
	private AdvertScrollView adverScrollView;
	
	private ArrayList<View> img_list = new ArrayList<View>();
	private MainAdverPagerAdapter adverPagerAdapter ;
	private NewsListAdapter newsListAdapter;
	
	private ListView newsList;
	private ArrayList<NewsListBean> newsListData = new ArrayList<NewsListBean>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		setTitle("News");
		adverScrollView = (AdvertScrollView) this.findViewById(R.id.advertview);
		changeImageHeight();
		newsList = (ListView) this.findViewById(R.id.news_list);
		addListData();
		addListener();
		
		addGuanGaoData();
		addViewBack();
		
		addBottomListener();
	}

	private void changeImageHeight() {
		android.view.ViewGroup.LayoutParams pm = adverScrollView
				.getLayoutParams();
		pm.height = (int) ((24/64.0f) * this.getResources()
				.getDisplayMetrics().widthPixels);
	}
    private void addBottomListener() {
    	Button  go_home = (Button) this.findViewById(R.id.go_home);
		go_home.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewsActivity.this,MainActivity.class);
				NewsActivity.this.finish();
				startActivity(intent);
			}
		});
		Button  go_products = (Button) this.findViewById(R.id.go_products);
		go_products.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewsActivity.this,ProductsActivity.class);
				startActivity(intent);
			}
		});
	}

	private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NewsActivity.this.finish();//结束当前activity以返回上一级
			}
		});
	}
	private void addGuanGaoData() {
		new Thread(new GetDataRunnable()).start(); //启动线程获得后台数据 
	
		adverPagerAdapter = new MainAdverPagerAdapter(
				img_list);
	}
	private void addListener() {
		newsList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NewsListBean listBean = (NewsListBean) parent.getItemAtPosition(position);
				Intent intent = new Intent(NewsActivity.this,ShowWebActivity.class);
				intent.putExtra("url",listBean.getUrl());
				intent.putExtra("title","News Details");
				startActivity(intent);
			}	
		});
	}

	private void addListData() {
		new Thread(new GetNewsRunnable()).start();
		newsListAdapter= new NewsListAdapter(this);
		newsList.setAdapter(newsListAdapter);
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}
	
    private Handler handler= new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_GUAN_GAO_DATA:
				WebService.addImage(NewsActivity.this, img_list, (ArrayList<AdvertItemBean>)msg.obj);
				adverScrollView.addData(adverPagerAdapter);
				break;
			case GET_NEWS_DATA:
			    newsListAdapter.addDate((ArrayList<NewsListBean>)msg.obj);
				break;
			default:
				break;
			}
		};
	};
	
	class GetDataRunnable implements Runnable {
		@Override
		public void run() {
			ArrayList<AdvertItemBean> advertListBean = new ArrayList<AdvertItemBean>();
			try {
				WebService.getAdvertBean(advertListBean, 2);
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
			msg.what = GET_GUAN_GAO_DATA; // 已经获得数据的标志
			msg.obj = advertListBean;
			handler.sendMessage(msg);
		}

	}
	
	class GetNewsRunnable implements Runnable {
		@Override
		public void run() {
			try {
				WebService.getNewsData(newsListData);
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
			msg.what = GET_NEWS_DATA; // 已经获得数据的标志
			msg.obj = newsListData;
			handler.sendMessage(msg);
		}

	}
}
