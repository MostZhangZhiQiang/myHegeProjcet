package com.hege.pts;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.MainActivity.GetDataRunnable;
import com.hege.pts.adapter.MainAdverPagerAdapter;
import com.hege.pts.data.AdvertItemBean;
import com.hege.pts.tools.WebService;
import com.hege.pts.view.AdvertScrollView;
import com.hege.pts.view.CircularView;
import com.hege.pts.view.ProgressView;
import com.hege.pts.view.SlidScrollView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUsActivity extends Activity implements OnClickListener{

	private final static int GET_DATA=1;
	private AdvertScrollView adverScrollView;
	
	private ArrayList<View> img_list = new ArrayList<View>();
	private MainAdverPagerAdapter adverPagerAdapter ;
	
	private SlidScrollView scrollView;
	private ProgressView progressView;
	private ImageView viewBack;
	private CircularView circularView_1;
	private CircularView circularView_2;
	private CircularView circularView_3;
	private CircularView circularView_4;
	private CircularView circularView_5;
	
	private String aboutUrl = null;
	private String cultureUrl = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		setTitle("About Us");
		scrollView = (SlidScrollView) this.findViewById(R.id.scrollView);
		progressView = (ProgressView) this.findViewById(R.id.progressView);
		scrollView.setProgressView(progressView);
		adverScrollView = (AdvertScrollView) this.findViewById(R.id.advertview);
		changeImageHeight();
		initView();
		addListener();
		addViewBack();
		
		addData();
		
		getUrl();
	}

   private void changeImageHeight() {
		android.view.ViewGroup.LayoutParams pm = adverScrollView.getLayoutParams();
		pm.height = (int) ((36/64.0f)*this.getResources().getDisplayMetrics().widthPixels);
	}
	private void getUrl() {
		new Thread(new GetUrlRunnable()).start();
	}

	private void addData() {
		new Thread(new GetDataRunnable()).start(); //启动线程获得后台数据 
	
		adverPagerAdapter = new MainAdverPagerAdapter(
				img_list);
	}

	private void addListener() {
		circularView_1.setOnClickListener(this);
		circularView_2.setOnClickListener(this);
		circularView_3.setOnClickListener(this);
		circularView_4.setOnClickListener(this);
		circularView_5.setOnClickListener(this);
		
		Button  go_home = (Button) this.findViewById(R.id.go_home);
		go_home.setOnClickListener(this);
		Button  go_products = (Button) this.findViewById(R.id.go_products);
		go_products.setOnClickListener(this);
	}

	private void initView() {
		circularView_1 = (CircularView) this.findViewById(R.id.circular_1);
		circularView_2 = (CircularView) this.findViewById(R.id.circular_2);
		circularView_3 = (CircularView) this.findViewById(R.id.circular_3);
		circularView_4 = (CircularView) this.findViewById(R.id.circular_4);
		circularView_5 = (CircularView) this.findViewById(R.id.circular_5);
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.circular_1:
			intent.setClass(this,ShowWebActivity.class);
			intent.putExtra("title","About Us");
			intent.putExtra("url", aboutUrl);
			break;
		case R.id.circular_2:
			intent.setClass(this,HonorsActivity.class);
			intent.putExtra("title","Enterprise Honors");
			break;
		case R.id.circular_3:
			intent.setClass(this,ShowWebActivity.class);
			intent.putExtra("title","Enterprise Culture");
			intent.putExtra("url",cultureUrl);
			break;
		case R.id.circular_4:
			intent.setClass(this,DevelopmentEventActivity.class);
			intent.putExtra("title","Development Events");
			break;
		case R.id.circular_5:
			intent.setClass(this,EquipmentDiscriptionActivity.class);
			intent.putExtra("title","Equipment Discription");
			break;
		case R.id.go_home:
			intent.setClass(this, MainActivity.class);
			this.finish();
			break; 
		case R.id.go_products:
			intent.setClass(this, ProductsActivity.class);		
			break;
		default:
			break;
		}
		startActivity(intent);
	}
	
    private void addViewBack() {
		viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AboutUsActivity.this.finish();//结束当前activity以返回上一级
			}
		});
	}
    
	private Handler handler= new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DATA:
				WebService.addImage(AboutUsActivity.this,img_list,(ArrayList<AdvertItemBean>)msg.obj);
				adverScrollView.addData(adverPagerAdapter);
				break;
			default:
				break;
			}
		};
	};
	
    class GetDataRunnable implements Runnable{
		@Override
		public void run() {
			//以后从网络获得
			ArrayList<AdvertItemBean> advertListBean = new ArrayList<AdvertItemBean>();
			try {
				WebService.getAdvertBean(advertListBean, 1);
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
			msg.what = GET_DATA; //已经获得数据的标志
			msg.obj = advertListBean;
			handler.sendMessage(msg);
		}
		
	}
    
    class GetUrlRunnable implements Runnable{
		@Override
		public void run() {
			try {
				aboutUrl = WebService.getUrl(0);
				cultureUrl = WebService.getUrl(1);
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
		}
		
	}
}
