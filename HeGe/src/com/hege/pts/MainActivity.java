package com.hege.pts;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hege.pts.adapter.MainAdverPagerAdapter;
import com.hege.pts.data.AdvertItemBean;
import com.hege.pts.tools.WebService;
import com.hege.pts.view.AdvertScrollView;
import com.squareup.picasso.Picasso;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{

	private final static int GET_DATA=1;
	private AdvertScrollView adverScrollView;
	
	private ArrayList<View> img_list = new ArrayList<View>();
	private MainAdverPagerAdapter adverPagerAdapter ;
	private RelativeLayout relative_1;
	private RelativeLayout relative_2;
	private RelativeLayout relative_3;
	private RelativeLayout relative_4;
	private RelativeLayout relative_5;
	private RelativeLayout relative_6;
	private RelativeLayout relative_7;
	private RelativeLayout relative_8;
	private RelativeLayout relative_9;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		changeImageHeight();
		addListener();
		addData();
	}
	
   private void changeImageHeight() {
		android.view.ViewGroup.LayoutParams pm = adverScrollView.getLayoutParams();
		pm.height = (int) ((36/64.0f)*this.getResources().getDisplayMetrics().widthPixels);
	}
	private void addData() {
		new Thread(new GetDataRunnable()).start(); //启动线程获得后台数据 
	
		adverPagerAdapter = new MainAdverPagerAdapter(
				img_list);
	}

	private void initView() {
		adverScrollView = (AdvertScrollView) this.findViewById(R.id.advertview);
		
		relative_1 = (RelativeLayout) this.findViewById(R.id.relative_1);
		relative_2 = (RelativeLayout) this.findViewById(R.id.relative_2);
		relative_3 = (RelativeLayout) this.findViewById(R.id.relative_3);
		relative_4 = (RelativeLayout) this.findViewById(R.id.relative_4);
		relative_5 = (RelativeLayout) this.findViewById(R.id.relative_5);
		relative_6 = (RelativeLayout) this.findViewById(R.id.relative_6);
		relative_7 = (RelativeLayout) this.findViewById(R.id.relative_7);
		relative_8 = (RelativeLayout) this.findViewById(R.id.relative_8);
		relative_9 = (RelativeLayout) this.findViewById(R.id.relative_9);
	}

	private void addListener() {
		relative_1.setOnClickListener(this);
		relative_2.setOnClickListener(this);
		relative_3.setOnClickListener(this);
		relative_4.setOnClickListener(this);
		relative_5.setOnClickListener(this);
		relative_6.setOnClickListener(this);
		relative_7.setOnClickListener(this);
		relative_8.setOnClickListener(this);
		relative_9.setOnClickListener(this);
	}
	
	private Handler handler= new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DATA:
				WebService.addImage(MainActivity.this,img_list,(ArrayList<AdvertItemBean>)msg.obj);
				adverScrollView.addData(adverPagerAdapter);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.relative_1:
			intent.setClass(this,AboutUsActivity.class);
			break;
        case R.id.relative_2:
        	intent.setClass(this,NewsActivity.class);
			break;
		case R.id.relative_3:
			intent.setClass(this,ProductsActivity.class);
			break;
		case R.id.relative_4:
			intent.setClass(this,VideoActivity.class);
			break;
		case R.id.relative_5:
			intent.setClass(this,FeedBackActivity.class);
			intent.putExtra("title", "Feedback");
			break;
		case R.id.relative_6:
			intent.setClass(this,ProjectCaseActivity.class);
			break;
		case R.id.relative_7:
			intent.setClass(this,ShowWebActivity.class);
			intent.putExtra("url", "http://news.pts80.net/hege/other/question.php");
			intent.putExtra("title", "FAQ");
			break;
		case R.id.relative_8:
			intent.setClass(this,ContactActivity.class);
	        break;
		case R.id.relative_9:
			intent.setClass(this,MoreActivity.class);
			intent.putExtra("title","More");
			break;
		}
		startActivity(intent);
	}

	class GetDataRunnable implements Runnable{
		@Override
		public void run() {
			
			ArrayList<AdvertItemBean> advertListBean = new ArrayList<AdvertItemBean>();
				
			try {
				WebService.getAdvertBean(advertListBean,0);
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
	
	private long system_time;
	@Override
	public void onBackPressed() {
			if (System.currentTimeMillis() - system_time > 2000) {
				Toast.makeText(this, "再按一次退出程序!", 0).show();
				system_time = System.currentTimeMillis();
			} else {
				MainActivity.this.finish();
				System.exit(0);
			}
	}
	
}
