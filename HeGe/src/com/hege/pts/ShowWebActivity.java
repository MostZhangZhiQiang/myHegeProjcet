package com.hege.pts;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowWebActivity extends Activity {

	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_news);
		webView = (WebView) this.findViewById(R.id.show_news_wv);
		Intent intent = this.getIntent();
		String url= intent.getStringExtra("url");
		setTitle(intent.getStringExtra("title"));
		webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		addViewBack();
	}

	private void setTitle(String title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}

    private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ShowWebActivity.this.finish();//结束当前activity以返回上一级
			}
		});
	}

}
