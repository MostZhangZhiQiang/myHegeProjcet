package com.hege.pts.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

import com.hege.pts.R;
import com.hege.pts.data.NewsListBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsListAdapter extends BaseAdapter {

	private ArrayList<NewsListBean> newsListData = new ArrayList<NewsListBean>();
	private Context context;
	public  NewsListAdapter(Context context){
		this.context = context;
	}
	
	public void addDate(ArrayList<NewsListBean> newsListDataT){
		newsListData.addAll(newsListDataT);
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return newsListData.size();
	}

	@Override
	public NewsListBean getItem(int position) {
		// TODO 自动生成的方法存根
		return newsListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder=null;
		
		if(null==convertView){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.news_list_item,null);
			TextView title_tv = (TextView) convertView.findViewById(R.id.news_item_title_tv);
			TextView date_tv = (TextView) convertView.findViewById(R.id.news_item_date_tv);
			viewHolder = new ViewHolder();
			viewHolder.date_tv = date_tv;
			viewHolder.title_tv = title_tv;
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String date = getItem(position).getDate();
		date = getDateToString(Long.parseLong(date));
		viewHolder.date_tv.setText(date);
		viewHolder.title_tv.setText(getItem(position).getTitle());
		return convertView;
	}
	
	public String getDateToString(long time) {
		Date d = new Date(time);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
		return sf.format(d);
	}
	final class ViewHolder{
		public TextView title_tv;
		public TextView date_tv;
	}

}
