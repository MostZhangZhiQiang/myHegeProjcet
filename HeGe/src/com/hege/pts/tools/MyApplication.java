package com.hege.pts.tools;

import java.io.File;
import java.util.Collection;

import com.hege.pts.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;


import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.DisplayMetrics;

public class MyApplication extends Application {

	public File cacheDir;
	private DisplayImageOptions options;
	private DisplayMetrics displayMetrics;

	@Override
	public void onCreate() {
		super.onCreate();
		displayMetrics=getResources().getDisplayMetrics();
		initImageLoader();
	}

	private void initImageLoader() {
		cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(),
				getResources().getString(R.string.cache_name) + "/"
						+"imageCache");
		options = DisplayImgOptionFactory.getDefaultImgOption();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).threadPriority(Thread.NORM_PRIORITY - 1)
				.memoryCacheExtraOptions(480, 480)
				.threadPoolSize(4)
				.memoryCache(new LruMemoryCache(10 * 1024 * 1024))
				.denyCacheImageMultipleSizesInMemory()
				.discCache(new UnlimitedDiscCache(cacheDir))
				.discCacheSize(30 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(options).build();
		ImageLoader.getInstance().init(config);
	}
}
