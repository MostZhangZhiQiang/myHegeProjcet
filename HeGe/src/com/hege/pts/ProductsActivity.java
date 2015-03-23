package com.hege.pts;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.data.CategoryBean;
import com.hege.pts.tools.ConstValue;
import com.hege.pts.tools.WebService;
import com.hege.pts.view.BgDrawLineView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductsActivity extends Activity {
	
	private ImageView image01;
	private ImageView image02;
	private ImageView image03;
	private ImageView image04;
	private ImageView image05;
	private TextView tv_01;
	private TextView tv_02;
	private TextView tv_03;
	private TextView tv_04;
	private TextView tv_05;
	private BgDrawLineView bgView;
	private boolean once = true;
	private ArrayList<CategoryBean> mCategoryList = new ArrayList<CategoryBean>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);
		setTitle("Products");
		initView();
		addViewBack();
		addBottomListener();
		//addData();
	}
	
    private void addData() {
		
    	new Thread(new GetCategory()).start();
	}

	private void addBottomListener() {
    	Button  go_home = (Button) this.findViewById(R.id.go_home);
		go_home.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProductsActivity.this,MainActivity.class);
				ProductsActivity.this.finish();
				startActivity(intent);
			}
		});
		Button  go_products = (Button) this.findViewById(R.id.go_products);
		go_products.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProductsActivity.this,ProductsActivity.class);
				startActivity(intent);
			}
		});
	}
    
    private void initView() {
		image01 = (ImageView) this.findViewById(R.id.pro_column01);
		image02 = (ImageView) this.findViewById(R.id.pro_column02);
		image03 = (ImageView) this.findViewById(R.id.pro_column03);
		image04 = (ImageView) this.findViewById(R.id.pro_column04);
		image05 = (ImageView) this.findViewById(R.id.pro_column05);
		bgView = (BgDrawLineView) this.findViewById(R.id.bg_view);
		
		tv_01 = (TextView) this.findViewById(R.id.tv_column01);
		tv_02 = (TextView) this.findViewById(R.id.tv_column02);
		tv_03 = (TextView) this.findViewById(R.id.tv_column03);
		tv_04 = (TextView) this.findViewById(R.id.tv_column04);
		tv_05 = (TextView) this.findViewById(R.id.tv_column05);
	}
    
	private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ProductsActivity.this.finish();// 结束当前activity以返回上一级
			}
		});
	}
	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}
	
	public void goCategroy(View view){

        Intent intent = new Intent(this,ShowCategoryActivity.class);
        
		switch (view.getId()) {
		case R.id.pro_column01:
			intent.putExtra("id", ConstValue.QING_GANG);
			break;
		case R.id.pro_column02:
			intent.putExtra("id", ConstValue.JI_ZHUAN);		
		    break;
		case R.id.pro_column03:
			intent.putExtra("id", ConstValue.FO_DONG);
			break;
		case R.id.pro_column04:
			intent.putExtra("id", ConstValue.QING_GANG_JIANG);
			break;
		case R.id.pro_column05:
			intent.putExtra("id", ConstValue.JIA_XING_BAN);
			break;
		default:
			break;
		}
		startActivity(intent);
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO 自动生成的方法存根
		super.onWindowFocusChanged(hasFocus);
		if(once ){
			sendLocation();
			once = false;
		}
		
	}
	
	private void sendLocation(){
		int imageHeight = image01.getHeight()/2;
		int location01[] = new int[2];
		int location02[] = new int[2];
		int location03[] = new int[2];
		int location04[] = new int[2];
		int location05[] = new int[2];
		image01.getLocationOnScreen(location01);
		bgView.setmLocation01(location01);
		
		image02.getLocationOnScreen(location02);
		bgView.setmLocation02(location02);
		
		image03.getLocationOnScreen(location03);
		bgView.setmLocation03(location03);
		
		image04.getLocationOnScreen(location04);
		bgView.setmLocation04(location04);
		
		image05.getLocationOnScreen(location05);
		bgView.setmLocation05(location05);
		
		bgView.setMHeight(imageHeight);
		//bgView.invalidate();
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:			  //得到消息表示数据 已成功获得	
				setAllView(); //为所有的view设置 数据 
				break;
			default:
				break;
			}
		}

		private void setAllView() {
			String uri = "http://news.pts80.net/hege/";
			if(mCategoryList.size()==0){
				return ;
			}
			CategoryBean category0 = mCategoryList.get(0);
			image01.setTag(category0.getId());
		    ImageLoader.getInstance().displayImage(uri+category0.getPhoto(), image01);
		    tv_01.setText(category0.getName());
			
		    CategoryBean category1 = mCategoryList.get(1);
			image02.setTag(category1.getId());
		    ImageLoader.getInstance().displayImage(uri+category1.getPhoto(), image02);
		    tv_02.setText(category1.getName());
		    
		    CategoryBean category2 = mCategoryList.get(2);
			image03.setTag(category2.getId());
		    ImageLoader.getInstance().displayImage(uri+category2.getPhoto(), image03);
		    tv_03.setText(category2.getName());
		    
		    CategoryBean category3 = mCategoryList.get(3);
			image04.setTag(category3.getId());
		    ImageLoader.getInstance().displayImage(uri+category3.getPhoto(), image04);
		    tv_04.setText(category3.getName());
		    
		    CategoryBean category4 = mCategoryList.get(4);
			image05.setTag(category4.getId());
		    ImageLoader.getInstance().displayImage(uri+category4.getPhoto(), image05);
		    tv_05.setText(category4.getName());
		    bgView.invalidate();
		};
	};
	
	class GetCategory implements Runnable{

		@Override
		public void run() {
			try {
				WebService.getCategory(mCategoryList);
				handler.sendEmptyMessage(0);
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
