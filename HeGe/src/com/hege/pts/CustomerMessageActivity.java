package com.hege.pts;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.tools.WebService;

import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomerMessageActivity extends Activity {

	private String id; //产品id
	private String way; //点赞分类，0为产品点赞，1为案例点赞
	private Button mSubmit;

	private EditText mContent;
	private EditText mName;
	private EditText mTel;
	private EditText mCountry;
	private EditText mAdd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_message);
		setTitle("Customer message");
		Intent intent = this.getIntent();
		id = intent.getStringExtra("id");
		way = intent.getStringExtra("way");
		initView();
		addListener();
		addViewBack();
	}

	private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CustomerMessageActivity.this.finish();// 结束当前activity以返回上一级
			}
		});
	}
	private void initView() {
		mSubmit = (Button) this.findViewById(R.id.btn_submit);

		mContent = (EditText) this.findViewById(R.id.edit_content);
		mName = (EditText) this.findViewById(R.id.edit_name);
		mTel = (EditText) this.findViewById(R.id.edit_tel);
		mCountry = (EditText) this.findViewById(R.id.edit_country);
		mAdd = (EditText) this.findViewById(R.id.edit_add);
	}

	private void addListener() {
		mSubmit.setOnClickListener(new OnClickListener() {
			
			private Map<String,String> map = new HashMap<String,String>();
			@Override
			public void onClick(View v) {
				map.put("pid", id);
				map.put("username", mName.getText().toString());
				map.put("tel", mTel.getText().toString());
				map.put("country", mCountry.getText().toString());
				map.put("add", mAdd.getText().toString());
				map.put("content", mContent.getText().toString());
				map.put("way", way);//0为产品留言
				
				new Thread(new SendMessgeRunnable(map)).start();
			}
		});
	}

	private void setTitle(String title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}
	
	class  SendMessgeRunnable implements Runnable{

		private Map<String,String> mMap;
		public SendMessgeRunnable(Map<String,String> map){
			mMap = map;
		}
		@Override
		public void run() {
			try {
				boolean isOk = WebService.sendMessage(mMap);
				Looper.prepare();
				if(isOk){
					Toast.makeText(CustomerMessageActivity.this, "Message successfully", 1).show();
				}else{
					Toast.makeText(CustomerMessageActivity.this, "Message failed", 1).show();
				}
				Looper.loop();
				
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
