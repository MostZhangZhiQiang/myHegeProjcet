package com.hege.pts.fragment;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.hege.pts.R;
import com.hege.pts.adapter.CasesListAdapter;
import com.hege.pts.data.CasesListBean;
import com.hege.pts.fragment.CasesTabFragment.GetDataRunnable;
import com.hege.pts.tools.WebService;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class GroupPhotoTabFragment extends Fragment {

	private ListView mListView;
	private CasesListAdapter mAdapter;
	
	private final static int GET_DATA = 1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.group_photo, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mListView = (ListView) this.getActivity().findViewById(R.id.group_photo_list);
		
		addData();
	}
	
	private void addData() {
		mAdapter = new CasesListAdapter(this.getActivity());
		mListView.setAdapter(mAdapter);
		new Thread(new GetDataRunnable()).start(); //�����̻߳�ú�̨����
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
				WebService.getCaseList(caseListBean,1);
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
			
			Message msg = Message.obtain();
			msg.what = GET_DATA; //�Ѿ�������ݵı�־
			msg.obj = caseListBean;
			handler.sendMessage(msg);
		}
		
	}
}
