package com.hege.pts;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationActivity extends Activity {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	// ���������
	private BitmapDescriptor mMarker;
	
	private double mLatitude = 0.0d;  //��γ��
	private double mLongtitude = 0.0d;
	private String location;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_location);
		setTitle("Contact");
		Intent intent = this.getIntent();
		location = intent.getStringExtra("location");
		
		mLatitude = Double.parseDouble(location.split(",")[0]);
		mLongtitude = Double.parseDouble(location.split(",")[1]);
		initView();
		addViewBack();
	}
	private void addViewBack() {
		ImageView viewBack = (ImageView) this.findViewById(R.id.icon_back);
		viewBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LocationActivity.this.finish();//������ǰactivity�Է�����һ��
			}
		});
	}
	private void initView()
	{
		mMapView = (MapView) findViewById(R.id.id_bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
		
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);
		
		centerToMyLocation();
			
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
	}
	
	@Override
	public void setTitle(CharSequence title) {
		TextView tv = (TextView) this.findViewById(R.id.title_text);
		tv.setText(title);
	}
	
	/**
	 * ��λ��λ��
	 */
	private void centerToMyLocation()
	{
		mBaiduMap.clear();
		LatLng latLng = null;
		OverlayOptions options;
	
		// ��γ��
		latLng = new LatLng(mLongtitude, mLatitude);
		// ͼ��
		options = new MarkerOptions().position(latLng).icon(mMarker)
				.zIndex(5);
		mBaiduMap.addOverlay(options);
			
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}
}
