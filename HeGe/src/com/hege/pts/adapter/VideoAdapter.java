package com.hege.pts.adapter;

import java.util.ArrayList;

import com.hege.pts.R;
import com.hege.pts.data.VideoBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VideoAdapter extends BaseAdapter {

	private ArrayList<VideoBean> videoBean;
	private Context mContext;
	private DisplayMetrics misplayMetrics;
	public VideoAdapter(Context context,ArrayList<VideoBean> videoBean){
		this.videoBean = videoBean;
		this.mContext = context;
		misplayMetrics = context.getResources().getDisplayMetrics();
	}
	@Override
	public int getCount() {
		
		return videoBean.size();
	}

	@Override
	public VideoBean getItem(int position) {
		// TODO 自动生成的方法存根
		return videoBean.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.video_gridview_item, null);
			TextView tv = (TextView) convertView.findViewById(R.id.video_gridview_item_title);
			
			viewHolder.image = (ImageView) convertView.findViewById(R.id.iamge_video);
			changeImageHeight(viewHolder.image);
			viewHolder.tv = tv;
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		Uri url = Uri.parse("http://news.pts80.net/hege/"+getItem(position).getPhoto());
		//Picasso.with(mContext).load(url).fit().into(viewHolder.image);
		if(!url.equals(viewHolder.image.getTag())){
			viewHolder.image.setTag(url);
			ImageLoader.getInstance().displayImage(url.toString(), viewHolder.image);
		}
		viewHolder.tv.setText(getItem(position).getTitle());
		return convertView;
	}
	public void addData(ArrayList<VideoBean> obj) {
		videoBean.addAll(obj);
		this.notifyDataSetChanged();
	}
    
	private void changeImageHeight(View imageView) {
		android.view.ViewGroup.LayoutParams pm = imageView.getLayoutParams();
		pm.height = (int) ((328/288.0f)*(misplayMetrics.widthPixels-30*misplayMetrics.density)/2);
	}
	
	final class ViewHolder{
		public ImageView image;
		public TextView tv;
	}
}
