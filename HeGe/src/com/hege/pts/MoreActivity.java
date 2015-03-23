package com.hege.pts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hege.pts.tools.MyApplication;
import com.hege.pts.tools.WebService;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MoreActivity extends Activity implements OnClickListener{

	private LinearLayout mClearanceBuffer;
	private LinearLayout mExaminationVersion;
	private LinearLayout mTechnicalSupport;
	private TextView mVersion;
	private TextView mBufferSize;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		Intent intent = this.getIntent();
		setTitle(intent.getStringExtra("title"));
		
		initView();
		
		addListener();
		
		addViewBack();
	}
	
	private void addListener() {
		mClearanceBuffer.setOnClickListener(this);
		mExaminationVersion.setOnClickListener(this);
		mTechnicalSupport.setOnClickListener(this);
	}

	private void initView() {
		mClearanceBuffer = (LinearLayout) this.findViewById(R.id.clearance_buffer);
		mExaminationVersion = (LinearLayout) this.findViewById(R.id.examination_version);
		mTechnicalSupport = (LinearLayout) this.findViewById(R.id.technical_support);
		mVersion = (TextView) this.findViewById(R.id.id_version);
	    mVersion.setText("v"+getVersion());
	
	    mBufferSize = (TextView) this.findViewById(R.id.buffer_size);
	    mBufferSize.setText(getBufferSize()+" M");
	}

	private CharSequence getBufferSize() {
		MyApplication app = (MyApplication) this.getApplication();
		File cache = app.cacheDir;
		float m = 0;
		for(File f:cache.listFiles()){
			m= m+(f.length()/(1024*1024.0f));
		}
		return (m+"").substring(0, 3);
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}
	
	private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MoreActivity.this.finish();//结束当前activity以返回上一级
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clearance_buffer:
			ImageLoader.getInstance().clearDiscCache();
			ImageLoader.getInstance().clearMemoryCache();
			mBufferSize.setText("0.0 M");
			break;
		case R.id.examination_version:
			new Thread(new FindVersion()).start();
			break;
		case R.id.technical_support:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData( Uri.parse("http://pts80.net/") );
            startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 1:
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData( Uri.parse((String)msg.obj) );
                startActivity(intent);
				break;
			default:
				break;
			}
		};
	};
	
	class FindVersion implements Runnable{

		@Override
		public void run() {
			try {
				String[] version = WebService.findVersion();
				if(!version[0].equals(getVersion())){ //版本不相同，要更新
					//下载apk文件
					Message msg = Message.obtain();
					msg.obj = version[1];
					msg.what=1;
					handler.sendMessage(msg);
				}else{
					Looper.prepare();
				    Toast.makeText(MoreActivity.this, "您当前是最新版本!", 0).show();
			        Looper.loop();
				}
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
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
	    try {
	        PackageManager manager = this.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
	        String version = info.versionName;
	        return version;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "不能找到版本信息";
	    }
	}
}
