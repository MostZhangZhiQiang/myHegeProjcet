package com.hege.pts.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MainAdverPagerAdapter extends PagerAdapter {
	private List<View> list;

	public MainAdverPagerAdapter(List<View> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list!=null?list.size():0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(list.get(position));
		return list.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return list.indexOf(object);
	}
}
