package com.hege.pts;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.AboutUsActivity.GetDataRunnable;
import com.hege.pts.adapter.MainAdverPagerAdapter;
import com.hege.pts.adapter.VideoAdapter;
import com.hege.pts.data.AdvertItemBean;
import com.hege.pts.data.VideoBean;
import com.hege.pts.tools.WebService;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VideoActivity extends Activity {

	private GridView mGridView;
	private VideoAdapter mVideoAdapter;
	private final static int GET_VIDEO_DATA = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		setTitle("Video");
		mGridView = (GridView) this.findViewById(R.id.video_gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));//mGridView無背景
		addListener();
		addData();
		addViewBack();
		addBottomListener();
	}

	private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				VideoActivity.this.finish();// 结束当前activity以返回上一级
			}
		});
	}
    private void addBottomListener() {
    	Button  go_home = (Button) this.findViewById(R.id.go_home);
		go_home.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(VideoActivity.this,MainActivity.class);
				VideoActivity.this.finish();
				startActivity(intent);
			}
		});
		Button  go_products = (Button) this.findViewById(R.id.go_products);
		go_products.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(VideoActivity.this,ProductsActivity.class);
				startActivity(intent);
			}
		});
	}
	private void addListener() {
		mGridView.setOnItemClickListener(new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Object object = parent.getItemAtPosition(position);
				String url = ((VideoBean)object).getUrl();
				if(TextUtils.isEmpty(url)){
					Toast.makeText(VideoActivity.this,"播放地址为空!", 1).show();
				}
				intent.setDataAndType(
                		Uri.parse( "http://news.pts80.net/hege/"+url), "video/mp4");
                startActivity(intent);
			}	
		});
	}

	private void addData() {
		mVideoAdapter = new VideoAdapter(this,new ArrayList<VideoBean>());
		mGridView.setAdapter(mVideoAdapter);
		new Thread(new GetDataRunnable()).start(); //启动线程获得后台数据 
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}
	
	private Handler handler= new Handler(){
			
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_VIDEO_DATA:
				mVideoAdapter.addData((ArrayList<VideoBean>)msg.obj);
				break;
			default:
				break;
			}
		};
	};
	
	class GetDataRunnable implements Runnable{
		@Override
		public void run() {
			ArrayList<VideoBean> videoListBean = new ArrayList<VideoBean>();	
			try {
				WebService.getVideoList(videoListBean);
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
			msg.what = GET_VIDEO_DATA; //已经获得数据的标志
			msg.obj = videoListBean;
			handler.sendMessage(msg);
		}
		
	}
}
