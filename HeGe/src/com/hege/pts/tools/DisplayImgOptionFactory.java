package com.hege.pts.tools;

import android.graphics.Bitmap;

import com.hege.pts.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


public class DisplayImgOptionFactory {
	/** 默认的图片显示 */
	public static DisplayImageOptions getDefaultImgOption() {
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.color.transparent)
				.showImageForEmptyUri(R.color.transparent)
				.showImageOnFail(R.color.transparent).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(100)).build();
	}

	/** 头像的图片显示 */
	public static DisplayImageOptions getHeaderImgOption() {
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.color.transparent)
				.showImageForEmptyUri(R.color.transparent)
				.showImageOnFail(R.color.transparent).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new CircleBitmapDisplayer(0)).build();
	}

	/** 圆角的图片显示 */
	public static DisplayImageOptions getRoundRectImgOption() {
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.color.transparent)
				.showImageForEmptyUri(R.color.transparent)
				.showImageOnFail(R.color.transparent).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(15)).build();
	}
}
