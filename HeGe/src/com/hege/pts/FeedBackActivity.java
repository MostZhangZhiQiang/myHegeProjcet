package com.hege.pts;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.adapter.ChatMessageAdapter;
import com.hege.pts.data.ChatMessage;
import com.hege.pts.data.ChatMessage.Type;
import com.hege.pts.tools.WebService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.common.view.DropDownListView;
import cn.trinea.android.common.view.DropDownListView.OnDropDownListener;

public class FeedBackActivity extends Activity {

	private DropDownListView listView;
	private ChatMessageAdapter chatMessageAdapter;
	private final static int REFRESH_COMPLETE = 1;
	private final static int REFRESH_NEW_COMPLETE =2;
	private final static int GET_FIRST_CHAT_DATE = 3;
	private final static int SEND_OK = 4;
	
	private Button mSendButton;
	private EditText mEditSend;
	private String mSendStr;
	private String mCodeId;
	
	private long mTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		Intent intent = this.getIntent();
		setTitle(intent.getStringExtra("title"));

		listView = (DropDownListView) this.findViewById(R.id.list_view);
		listView.setHeaderPaddingTopRate(3);// 设置头部toppadding与拖动距离的比例

		mSendButton = (Button) this.findViewById(R.id.id_send_msg);
		mEditSend = (EditText) this.findViewById(R.id.id_input_msg);
		
		getFirstChatMessage();
		TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		mCodeId = tm.getDeviceId();
		addListener();
		addViewBack();
	}

    private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FeedBackActivity.this.finish();//结束当前activity以返回上一级
			}
		});
	}
    
	private void getFirstChatMessage() {
		chatMessageAdapter = new ChatMessageAdapter(this);
		listView.setAdapter(chatMessageAdapter);
		new Thread(new GetFirstChatMessage()).start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_COMPLETE:
				chatMessageAdapter
				.addListData((ArrayList<ChatMessage>) msg.obj);
				listView.onDropDownComplete();
				break;
			case GET_FIRST_CHAT_DATE:
				chatMessageAdapter
						.addListData((ArrayList<ChatMessage>) msg.obj);
				listView.setSelection(chatMessageAdapter.mDatas.size()-1);
				mTime = System.currentTimeMillis();
				break;
			case SEND_OK:
				chatMessageAdapter.addOneDate(new ChatMessage(mSendStr,Type.OUTCOMING,new Date()));
				listView.setSelection(chatMessageAdapter.mDatas.size()-1);
				mSendStr = null;
				mEditSend.setText("");
				break;
			case REFRESH_NEW_COMPLETE:
				chatMessageAdapter
				.addListData((ArrayList<ChatMessage>) msg.obj);
		        listView.setSelection(chatMessageAdapter.mDatas.size()-1);
				break;
			default:
				break;
			}
		};
	};

	private void addListener() {
		listView.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {
				Map<String,String> map = new HashMap<String,String>();
				map.put("page", 0+"");
				map.put("num",10+"");
				map.put("way",0+"");
				map.put("time", mTime+"");
				new Thread(new GetHistoryChatMessage(map)).start();
			}
		});

		mSendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSendStr = mEditSend.getText().toString();
				
				if(TextUtils.isEmpty(mSendStr)){
					Toast.makeText(FeedBackActivity.this, "发送消息不能为空!", 1).show();
		 		    return; //为空不发送
				}
				Map<String,String> map = new HashMap<String,String>();
				map.put("id", mCodeId );
				map.put("content", mSendStr );
				new Thread(new SendChatMessage(map)).start();
			}
		});
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}

	class GetFirstChatMessage implements Runnable {

		@Override
		public void run() {
			ArrayList<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
			try {
				WebService.getFirstChatMessage(chatMessageList);
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
			Message message = Message.obtain();
			message.obj = chatMessageList;
			message.what = GET_FIRST_CHAT_DATE;
			handler.sendMessage(message);
		}
	}
	
	class SendChatMessage implements Runnable{

		private Map<String,String> mMap;
		public SendChatMessage(Map<String,String> map){
			this.mMap = map;
		}
		
		@Override
		public void run() {	
			try {
				boolean isOk = WebService.sendChatMessage(mMap);
				if(isOk){
					handler.sendEmptyMessage(SEND_OK);
				}else{
					Looper.prepare();
					Toast.makeText(FeedBackActivity.this, "消息发送失败!", 1).show();
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
	
	class  GetHistoryChatMessage implements Runnable{

		private Map<String,String> mMap;
		public GetHistoryChatMessage(Map<String,String> map){
			this.mMap = map;
		}
		@Override
		public void run() {
			ArrayList<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
			try {
				WebService.getHistoryChatMessage(mMap,chatMessageList);
				
				Message message = Message.obtain();
				message.obj = chatMessageList;
				message.what = REFRESH_COMPLETE;
				handler.sendMessage(message);
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
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
	private ReFlashRunnable reFlashRunnable;
	@Override
	protected void onResume() {
		super.onResume();
		reFlashRunnable = new ReFlashRunnable();
		reFlashRunnable.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		reFlashRunnable.stopThread();
		reFlashRunnable.interrupt();//发出一个中断
	}
	class ReFlashRunnable extends Thread{

		private boolean over = false;
		@Override
		public void run() {
			while(!over){
				Map<String,String> map = new HashMap<String,String>();
				map.put("page", 0+"");
				map.put("num",2+"");
				map.put("way",1+"");
				map.put("time", mTime+"");
				ArrayList<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
				try {
					WebService.getHistoryChatMessage(map, chatMessageList);
					if(chatMessageList.size()!=0){
						Message message = Message.obtain();
						message.obj = chatMessageList;
						message.what = REFRESH_NEW_COMPLETE;
						handler.sendMessage(message);
					}
				} catch (UnsupportedEncodingException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				} catch (ClientProtocolException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				} catch (JSONException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				try {
					Thread.currentThread().sleep(10000);//10秒刷新一次
				} catch (InterruptedException e) {
					over = true;
					e.printStackTrace();
				}
			}
		}
		
		public void stopThread(){
			over = true;
		}
	}
}
