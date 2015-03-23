package com.hege.pts.adapter;

import java.util.ArrayList;

import com.hege.pts.R;
import com.hege.pts.ShowCategoryActivity;
import com.hege.pts.adapter.VideoAdapter.ViewHolder;
import com.hege.pts.data.CategoryListBean;
import com.hege.pts.data.VideoBean;
import com.hege.pts.tools.WebService;
import com.hege.pts.tools.WebService.PraiseRunnable.IsOkBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CategoryListAdapter extends BaseAdapter {

	public  ArrayList<CategoryListBean> mList = new ArrayList<CategoryListBean>();
	private Context mContext;
	private int mCategoryId;
	private DisplayMetrics misplayMetrics;
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				String str = ((TextView)msg.obj).getText().toString();
				((TextView)msg.obj).setText((Integer.parseInt(str)+1)+"");
			}
		};
	};
	public CategoryListAdapter(Context context,int categoryId){
		this.mContext = context;
		this.mCategoryId = categoryId;
		misplayMetrics = context.getResources().getDisplayMetrics();
	}
	
	public void addData(ArrayList<CategoryListBean> list){
		mList.addAll(list);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return mList.size();
	}

	@Override
	public CategoryListBean getItem(int position) {
		// TODO 自动生成的方法存根
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.category_list_item, null);

			viewHolder.imagePraise = (ImageView) convertView.findViewById(R.id.image_praise);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.category_image);
			viewHolder.tv_code = (TextView) convertView.findViewById(R.id.category_list_item_title);
			viewHolder.tv_prise = (TextView) convertView.findViewById(R.id.prise_num);
			changeImageHeight(viewHolder.image);
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
			
		final TextView tv = viewHolder.tv_prise;
		viewHolder.imagePraise.setTag(getItem(position).getId());
		final WebService.PraiseRunnable praiseRunnable= new WebService.PraiseRunnable(mContext,getItem(position).getId(),1);
		praiseRunnable.setInterfaceIsOk(new IsOkBack() {
			@Override
			public void isOk(boolean status) { //点赞后会调用此方法
				if(status==true){ //点赞成功
					Message message = Message.obtain();
					message.what = 1;
					message.obj = tv;
					handler.sendMessage(message);
					
				}
			}
		});
		viewHolder.imagePraise.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				new Thread(praiseRunnable).start();//1代表案例点赞
			}
		});
		
		viewHolder.tv_code.setText(getItem(position).getCategoryCode(mCategoryId));
		viewHolder.tv_prise.setText(getItem(position).getPriseNum());
		return convertView;
	}

    private void changeImageHeight(View imageView) {
    	
		android.view.ViewGroup.LayoutParams pm = imageView.getLayoutParams();
		pm.height = (int) ((33/60.0f)*(misplayMetrics.widthPixels-20*misplayMetrics.density));
	}
	final class ViewHolder{
		public ImageView imagePraise;
		public ImageView image;
		public TextView tv_prise;
		public TextView tv_code;
	}
}
