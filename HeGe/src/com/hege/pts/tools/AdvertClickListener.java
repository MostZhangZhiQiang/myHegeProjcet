package com.hege.pts.tools;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.hege.pts.R;
import com.hege.pts.data.AdvertItemBean;

public class AdvertClickListener implements OnClickListener {
	private WeakReference<Context> wr;
	private AdvertItemBean itemBean;

	public AdvertClickListener(Context context, AdvertItemBean arg0) {
		wr = new WeakReference<Context>(context);
		itemBean = arg0;
	}

	@Override
	public void onClick(View v) {
		/*if (itemBean != null && !TextUtils.isEmpty(itemBean.getAction_key())
				&& !TextUtils.isEmpty(itemBean.getAction_val())) {
			if ("0".equals(itemBean.getAction_key())) {
				return;
			}
			//  ÈºÆ
		   if (AppValues.LINE_ADVERT_BOOK.equals(itemBean.getAction_key())) {
				Intent intent = new Intent(wr.get(), BooksDetailActivity.class);
				intent.putExtra("id", itemBean.getAction_val());
				wr.get().startActivity(intent);
			}
			// Õ‚¡¥
			if (AppValues.LINE_ADVERT_WEB.equals(itemBean.getAction_key())) {
				Intent intent = new Intent(wr.get(), WebActivity.class);
				intent.putExtra("web", itemBean.getAction_val());
				wr.get().startActivity(intent);
			}
		}*/
	}
}
