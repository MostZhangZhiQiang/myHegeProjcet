package com.hege.pts;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.VideoActivity.GetDataRunnable;
import com.hege.pts.adapter.CategoryListAdapter;
import com.hege.pts.data.CategoryListBean;
import com.hege.pts.data.VideoBean;
import com.hege.pts.tools.WebService;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShowCategoryActivity extends Activity {

	private InputMethodManager imm;
	private ListView mListView;
	private TextView mTitle;
	private RelativeLayout mSearchView;
	private ImageView mSearchButton;
	private ImageView mEditSearchButton;
	private String mSearchText = null;
	private EditText mEditSearch;
	
	private CategoryListAdapter mAdapter;
	private final static int GET_CATEGORY_DATA = 1;
	public static final int GET_FIND_DATA = 0;
	private int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_category);
		initView();
		addListener();
		Intent intent = this.getIntent();
		id =intent.getIntExtra("id", 0);
		addData(id);
		addViewBack();
	}
	
	private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowCategoryActivity.this.finish();// ������ǰactivity�Է�����һ��
			}
		});
	}
	
	private void addData(int id) {
		mAdapter = new CategoryListAdapter(this,id);
		mListView.setAdapter(mAdapter);
		new Thread(new GetDataRunnable()).start(); //�����̻߳�ú�̨����
	}

	private void initView() {
		mListView = (ListView) this.findViewById(R.id.category_list);
        mTitle = (TextView) this.findViewById(R.id.title_tv);
        mSearchView = (RelativeLayout) this.findViewById(R.id.search_view);
        mSearchButton = (ImageView) this.findViewById(R.id.search_button);
        mEditSearchButton = (ImageView) this.findViewById(R.id.edit_serach_btn);
        mEditSearch = (EditText) this.findViewById(R.id.edit_search);
	}

	private Handler handler= new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_CATEGORY_DATA:
				mAdapter.addData((ArrayList<CategoryListBean>)msg.obj);
				break;
			case GET_FIND_DATA:
				mAdapter.mList.clear();
				mAdapter.addData((ArrayList<CategoryListBean>)msg.obj);
			default:
				break;
			}
		};
	};
	
	class GetDataRunnable implements Runnable{
		@Override
		public void run() {
			ArrayList<CategoryListBean> categoryListBean = new ArrayList<CategoryListBean>();
			
			try {
				WebService.getCategoryList(categoryListBean,id);
				Message msg = Message.obtain();
				msg.what = GET_CATEGORY_DATA; //�Ѿ�������ݵı�־
				msg.obj = categoryListBean;
				handler.sendMessage(msg);
			} catch (ClientProtocolException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void addListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ShowCategoryActivity.this,ProductDetailActivity.class);
				intent.putExtra("id", ((CategoryListBean)parent.getItemAtPosition(position)).getId());
				
				startActivity(intent);
			}

		});
		mSearchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTitle.setVisibility(View.GONE);
				mSearchButton.setVisibility(View.GONE);
				mSearchView.setVisibility(View.VISIBLE);
				
				mEditSearch.setFocusable(true);
				mEditSearch.setFocusableInTouchMode(true);
				mEditSearch.requestFocus();
				
				//�������
				imm = (InputMethodManager)ShowCategoryActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

				
			}
		});
		
		mEditSearchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSearchText = mEditSearch.getEditableText().toString();
				mTitle.setVisibility(View.VISIBLE);
				mSearchButton.setVisibility(View.VISIBLE);
				mSearchView.setVisibility(View.INVISIBLE);
				//�ر������
				imm.hideSoftInputFromWindow(mEditSearch.getWindowToken(), 0);
			
				mEditSearch.setText("");
				if(TextUtils.isEmpty(mSearchText)){
					return; //Ϊ�ղ�������
				}
				new Thread(new FindProdcutData(id,mSearchText)).start();
			}
		});
	}
	
	class FindProdcutData implements Runnable{

		private int mCid;
		private String mKey;
		public FindProdcutData(int cid,String key){
			this.mCid = cid;
			this.mKey =key;
		}
		@Override
		public void run() {
			ArrayList<CategoryListBean> categoryListBean = new ArrayList<CategoryListBean>();
			try {
				WebService.findProductData(mCid,mKey,categoryListBean);
				if(0==categoryListBean.size()){
					return; //���Ϊ0�Ͳ���ʾ��
				}
				Message msg = Message.obtain();
				msg.what = GET_FIND_DATA; //�Ѿ�������ݵı�־
				msg.obj = categoryListBean;
				handler.sendMessage(msg);
			} catch (ClientProtocolException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		
	}
}
