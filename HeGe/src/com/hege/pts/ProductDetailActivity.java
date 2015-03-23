package com.hege.pts;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.AboutUsActivity.GetDataRunnable;
import com.hege.pts.adapter.MainAdverPagerAdapter;
import com.hege.pts.data.AdvertItemBean;
import com.hege.pts.data.CategoryDetailBean;
import com.hege.pts.tools.WebService;
import com.hege.pts.view.AdvertScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailActivity extends Activity {

	private final static int GET_DATA = 1;
	private AdvertScrollView adverScrollView;

	private ArrayList<View> img_list = new ArrayList<View>();
	private MainAdverPagerAdapter adverPagerAdapter;

	private LinearLayout mButtonDetails;
	private LinearLayout mButtonMessage;
	private LinearLayout mButtonPraise;
	private String id; // 产品id

	private TextView mTextTitle;
	private TextView mTextCode;
	private TextView mTextPhone;
	private String mDetailUrl;
	
	private ArrayList<String> urls = new ArrayList<String>();
	private ArrayList<String> titles = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		setTitle("Products Details");
		Intent intent = this.getIntent();
		id = intent.getStringExtra("id");
		initView();
		addListener();
		addData();
		addViewBack();
	}

	private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ProductDetailActivity.this.finish();// 结束当前activity以返回上一级
			}
		});
	}

	private void initView() {
		adverScrollView = (AdvertScrollView) this.findViewById(R.id.advertview);
		mButtonDetails = (LinearLayout) this.findViewById(R.id.button_details);
		mButtonMessage = (LinearLayout) this.findViewById(R.id.button_message);
		mButtonPraise = (LinearLayout) this.findViewById(R.id.button_praise);

		mTextCode = (TextView) this.findViewById(R.id.tv_code);
		mTextPhone = (TextView) this.findViewById(R.id.tv_phone);
		mTextTitle = (TextView) this.findViewById(R.id.tv_title);
	}

	private void addListener() {
		mButtonDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProductDetailActivity.this,
						ShowWebActivity.class);
				intent.putExtra("title", "Product Introduction");
				intent.putExtra("url", mDetailUrl);
				startActivity(intent);
			}
		});
		mButtonMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProductDetailActivity.this,
						CustomerMessageActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("way", 0); // 0为产品点赞
				startActivity(intent);
			}
		});
		mButtonPraise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new Thread(new WebService.PraiseRunnable(
						ProductDetailActivity.this, id, 0)).start();
			}
		});
	}

	private void addData() {
		new Thread(new GetDataRunnable()).start(); // 启动线程获得后台数据

		adverPagerAdapter = new MainAdverPagerAdapter(img_list);
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DATA:
				addViewData((CategoryDetailBean) msg.obj, img_list);
				adverScrollView.addData(adverPagerAdapter);
				break;
			default:
				break;
			}
		};
	};

	class GetDataRunnable implements Runnable {
		@Override
		public void run() {

			CategoryDetailBean categoryDetailBean = new CategoryDetailBean();

			try {
				WebService.getCategoryDetailList(categoryDetailBean, id);
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
			msg.what = GET_DATA; // 已经获得数据的标志
			msg.obj = categoryDetailBean;
			handler.sendMessage(msg);
		}

	}

	private void addViewData(CategoryDetailBean categoryDetailBean,
			ArrayList<View> img_list) {
		img_list.clear();
		String[] photos = categoryDetailBean.getPhotos();
		urls.add(photos[0]);
		titles.add(categoryDetailBean.getTitle());
		View view = LayoutInflater.from(this).inflate(
				R.layout.item_viewpager_main_advert, null);
		ImageView iv_advert = (ImageView) view.findViewById(R.id.iv_1);
		iv_advert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProductDetailActivity.this,
						ShowBigActivity.class);
				intent.putExtra("urls", urls);
				intent.putExtra("titles", titles);
				intent.putExtra("title", "Big Chart");
				startActivity(intent);
			}
		});
		ImageLoader.getInstance().displayImage("http://news.pts80.net/hege/" + photos[1], iv_advert);
		img_list.add(view);

		// 为view添加字符数据
		mTextTitle.setText(categoryDetailBean.getTitle());
		mTextCode.setText("编号:" + categoryDetailBean.getCode());
		mTextPhone.setText("电话:" + categoryDetailBean.getTel());
		mDetailUrl = categoryDetailBean.getUrl();
	}
}
