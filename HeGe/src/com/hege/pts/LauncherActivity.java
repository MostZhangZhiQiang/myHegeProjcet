package com.hege.pts;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class LauncherActivity extends Activity {

	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Intent intent = new Intent(LauncherActivity.this,MainActivity.class);
				startActivity(intent);
				LauncherActivity.this.finish();
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		Message msg = Message.obtain();
		msg.what = 1;
		handler.sendMessageDelayed(msg, 3000);
	}
}
