package com.hege.pts.fragment;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.CaseDetailActivity;
import com.hege.pts.R;
import com.hege.pts.adapter.CasesListAdapter;
import com.hege.pts.adapter.CategoryListAdapter;
import com.hege.pts.data.CasesListBean;
import com.hege.pts.tools.WebService;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CasesTabFragment extends Fragment {

	private ListView mListView;
	private CasesListAdapter mAdapter;
	private final static int GET_DATA = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.cases, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mListView = (ListView) this.getActivity().findViewById(R.id.cases_list);
		
		addData();
		addListener();
	}
	
	private void addListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(CasesTabFragment.this.getActivity(),CaseDetailActivity.class);
				intent.putExtra("id",((CasesListBean)parent.getItemAtPosition(position)).getId() );
				intent.putExtra("title", "Cases");
				startActivity(intent);
			}
		});
	}

	private void addData() {
		mAdapter = new CasesListAdapter(this.getActivity());
		mListView.setAdapter(mAdapter);
		new Thread(new GetDataRunnable()).start(); //启动线程获得后台数据
	}
	
	private Handler handler= new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DATA:			
				mAdapter.addData((ArrayList<CasesListBean>)msg.obj);
				break;
			default:
				break;
			}
		};
	};
	
	class GetDataRunnable implements Runnable{
		@Override
		public void run() {
			
			ArrayList<CasesListBean> caseListBean = new ArrayList<CasesListBean>();
			
			try {
				WebService.getCaseList(caseListBean,0);
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
			msg.obj = caseListBean;
			handler.sendMessage(msg);
		}
		
	}
	
}
