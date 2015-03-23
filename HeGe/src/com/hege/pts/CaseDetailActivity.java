package com.hege.pts;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.ProductDetailActivity.GetDataRunnable;
import com.hege.pts.adapter.MainAdverPagerAdapter;
import com.hege.pts.data.CaseDetailBean;
import com.hege.pts.data.CategoryDetailBean;
import com.hege.pts.tools.WebService;
import com.hege.pts.view.AdvertScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CaseDetailActivity extends Activity {

	private final static int GET_DATA=1;
	private AdvertScrollView adverScrollView;
	
	private ArrayList<View> img_list = new ArrayList<View>();
	private MainAdverPagerAdapter adverPagerAdapter ;
	
	private ImageView mImageMessage;
	private ImageView mImagePraise;
	private TextView mTextTitle;
	private TextView mTextCode;
	private TextView mTextPhone;
	private WebView mWebView;
	
	private ArrayList<String> urls = new ArrayList<String>();
	private ArrayList<String> titles = new ArrayList<String>();
	private String mPhoneNumber;
	private String id; //产品id
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_case_detail);
		Intent intent = this.getIntent();
		id = intent.getStringExtra("id");
		setTitle(intent.getStringExtra("title"));
		
		initView();
		changeImageHeight();
		addListener();
		addData();
		addViewBack();
	}
	
	private void changeImageHeight() {
		android.view.ViewGroup.LayoutParams pm = adverScrollView.getLayoutParams();
		pm.height = (int) ((48/64.0f)*this.getResources().getDisplayMetrics().widthPixels);
	}
	private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CaseDetailActivity.this.finish();//结束当前activity以返回上一级
			}
		});
	}
	private void addListener() {
		mImageMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CaseDetailActivity.this,CustomerMessageActivity.class);
				
				
				intent.putExtra("id", id); 
				intent.putExtra("way", 1);//案例点赞
				startActivity(intent);
			}
		});
		mImagePraise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new WebService.PraiseRunnable(CaseDetailActivity.this,id,1)).start();
			}
		});
		mTextPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(mPhoneNumber)){
					 //用intent启动拨打电话  
	                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+mPhoneNumber));  
	                startActivity(intent); 
				}
			}
		});
	}

	private void initView() {
		adverScrollView = (AdvertScrollView) this.findViewById(R.id.advertview);	
		mTextCode = (TextView) this.findViewById(R.id.tv_code);
		mTextPhone = (TextView) this.findViewById(R.id.tv_phone);
		mTextTitle = (TextView) this.findViewById(R.id.tv_title);
		mWebView = (WebView) this.findViewById(R.id.tv_webview);
	
		mImageMessage = (ImageView) this.findViewById(R.id.image_message);
		mImagePraise = (ImageView) this.findViewById(R.id.image_praise);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}
	
	private void addData() {
		new Thread(new GetDataRunnable()).start(); //启动线程获得后台数据 
	
		adverPagerAdapter = new MainAdverPagerAdapter(
				img_list);
	}
		
	private Handler handler= new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DATA:
				addViewData((CaseDetailBean)msg.obj,img_list);
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
			
			CaseDetailBean caseDetailBean = new CaseDetailBean();
				
			try {
				WebService.getCaseDetailList(caseDetailBean,id);
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
			msg.obj = caseDetailBean;
			handler.sendMessage(msg);
		}
		
	}
	private void addViewData(CaseDetailBean caseDetailBean,ArrayList<View> img_list) {
		img_list.clear();
		String[] photos = caseDetailBean.getPhotos();
		urls.add(photos[0]);
		titles.add(caseDetailBean.getTitle());
		View view = LayoutInflater.from(this).inflate(
				R.layout.item_viewpager_main_advert, null);
		ImageView iv_advert = (ImageView) view.findViewById(R.id.iv_1);
		iv_advert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CaseDetailActivity.this,
						ShowBigActivity.class);
				intent.putExtra("title", "Case");
				intent.putExtra("titles", titles); //图片标题
				intent.putExtra("urls", urls);
				startActivity(intent);
			}
		});
		
		ImageLoader.getInstance().displayImage("http://news.pts80.net/hege/" + photos[1], iv_advert);
		img_list.add(view);
		
		//为view添加字符数据 
		mTextTitle.setText(caseDetailBean.getTitle());
		mTextCode.setText("分类:"+caseDetailBean.getCate());
		mTextPhone.setText("电话:"+caseDetailBean.getTel());
		mPhoneNumber = caseDetailBean.getTel();
		mWebView.loadDataWithBaseURL("", caseDetailBean.getContent(),
				"text/html", "utf-8", "");
	}
}
