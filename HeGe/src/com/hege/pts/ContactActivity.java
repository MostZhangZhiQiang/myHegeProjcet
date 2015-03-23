package com.hege.pts;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hege.pts.data.ContactBean;
import com.hege.pts.tools.WebService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

public class ContactActivity extends Activity implements OnClickListener{

	public static final int GET_DATA = 0;
	private ImageView mLocation;
	private ImageView mPhone;
	
	private TextView mAddress;
	private TextView mTitle;
	private TextView mFax;
	private TextView mUrl;
	private TextView mEmail;
	private TextView mQq;
	private TextView mWeixin;
	private TextView phone_number;
	
	private ImageView mWeixin_img;
	
	private String mLocationStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		setTitle("Contact");
		initView();
		changeImageHeight();
		addListener();
		addData();
		addViewBack();
	}
	
    private void changeImageHeight() {
		ImageView imageView = (ImageView) this.findViewById(R.id.course_top_bg);
		android.view.ViewGroup.LayoutParams pm = imageView.getLayoutParams();
		pm.height = (int) (36/64.0f*this.getResources().getDisplayMetrics().widthPixels);
	}
	private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ContactActivity.this.finish();//结束当前activity以返回上一级
			}
		});
	}
	private void addData() {
		new Thread(new GetDataRunnable()).start(); //启动线程获得后台数据
	}

	private void addListener() {
		mLocation.setOnClickListener(this);
		mPhone.setOnClickListener(this);
	}

	private void initView() {
		mLocation = (ImageView) this.findViewById(R.id.button_location);
		mPhone = (ImageView) this.findViewById(R.id.button_phone);
		phone_number = (TextView) this.findViewById(R.id.phone_number);
		
		mTitle = (TextView) this.findViewById(R.id.tv_title);
		mAddress = (TextView) this.findViewById(R.id.tv_address);
		mFax = (TextView) this.findViewById(R.id.tv_fax);
		mUrl = (TextView) this.findViewById(R.id.tv_url);
		mEmail = (TextView) this.findViewById(R.id.tv_email);
		mQq = (TextView) this.findViewById(R.id.tv_qq);
		mWeixin = (TextView) this.findViewById(R.id.tv_weixin); 
		
		mWeixin_img = (ImageView) this.findViewById(R.id.image_weixin);
	}

	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.button_location:   //定位
			intent = new Intent(ContactActivity.this,LocationActivity.class);
			intent.putExtra("location", mLocationStr);
			startActivity(intent);
			break;
		case R.id.button_phone:   //打电话
			String number = getPhoneNumber();
			if(!TextUtils.isEmpty(number)){
				 //用intent启动拨打电话  
                intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));  
                startActivity(intent); 
			}
			break;
		default:
			break;
		}
	}

	private String getPhoneNumber() {
		String text = phone_number.getText().toString();
		Log.i("MainActivity", text.split("（")[0]);
		return text.split("（")[0]; //412452452(24小时热线 ) 去掉中文括號后面的文本
	}
	
	private Handler handler= new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DATA:
				ContactBean contactBean = (ContactBean) msg.obj;
				mLocationStr = contactBean.getAddress_location();
				setAllText(contactBean); //将数据填充到视图
				break;
			default:
				break;
			}
		}

		private void setAllText(ContactBean contactBean) {
			mTitle.setText(contactBean.getTitle());
			mAddress.setText(contactBean.getAddress());
			mFax.setText(contactBean.getFax());
			mUrl.setText(contactBean.getUrl());
			mEmail.setText(contactBean.getEmail());
			mQq.setText(contactBean.getQq());
			mWeixin.setText("微信公众号:"+contactBean.getWeixin()+"\n扫一扫有惊喜");
			phone_number.setText(contactBean.getTel());
			Uri url = Uri.parse("http://news.pts80.net/hege/"+contactBean.getWeixin_img());
			//Picasso.with(ContactActivity.this).load(url).into(mWeixin_img);
			ImageLoader.getInstance().displayImage(url.toString(), mWeixin_img);
		};
	};
	
    class GetDataRunnable implements Runnable{

		@Override
		public void run() {
			ContactBean contactBean = new ContactBean();
			try {
				getContactBean(contactBean);
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
			msg.obj = contactBean;
			handler.sendMessage(msg);
		}

		private void getContactBean(ContactBean contactBean) throws ClientProtocolException, IOException, JSONException {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://news.pts80.net/hege/api/?action=link&control=list");
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String str = WebService.getString(instream);
				parseJsonStr(str,contactBean);
			}
		}

		private void parseJsonStr(String json, ContactBean contactBean) throws JSONException {
			JSONObject jsonObject = new JSONObject(json);
			contactBean.setTitle(jsonObject.getString("title"));
			contactBean.setAddress(jsonObject.getString("address"));
			contactBean.setTel(jsonObject.getString("tel"));
			contactBean.setFax(jsonObject.getString("fax"));
			contactBean.setUrl(jsonObject.getString("url"));
			contactBean.setEmail(jsonObject.getString("email"));
			contactBean.setQq(jsonObject.getString("qq"));
			contactBean.setWeixin(jsonObject.getString("weixin"));
			contactBean.setWeixin_img(jsonObject.getString("weixin_img"));
			contactBean.setAddress_location(jsonObject.getString("address_location"));
		}
		
	}
}
