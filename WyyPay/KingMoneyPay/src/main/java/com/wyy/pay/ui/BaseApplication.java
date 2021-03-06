package com.wyy.pay.ui;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wyy.pay.upgrade.DBManager;
import com.wyy.pay.utils.BaseOptions;
import com.wyy.pay.utils.Utils;

import config.bean.XTCoreConfig;

public class BaseApplication extends Application {
	public static Context mContext;
	public static String wyyCode;
	private static String userName;

	@Override
	public void onCreate() {
		super.onCreate();
		Utils.isEmulator();
		mContext = this.getApplicationContext();
		initImageLoader();
		XTCoreConfig.initConfig(mContext);
		initDB();
	}

	public void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mContext).threadPriority(Thread.NORM_PRIORITY - 1)
				.threadPoolSize(3).denyCacheImageMultipleSizesInMemory()
				.memoryCache(new WeakMemoryCache())
				.diskCacheFileCount(500).memoryCacheSize(2 * 1024 * 1024)
				.memoryCacheSizePercentage(20).diskCacheSize(100 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// .writeDebugLogs() // Not necessary in common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		BaseOptions.getInstance();
	}

	public void initDB() {
		DBManager db = new DBManager();
		db.initTables(getApplicationContext());
	}
	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {

		BaseApplication.userName = userName;
	}
	public static String getWyyCode() {
		return wyyCode;
	}

	public static void setWyyCode(String wyyCode) {

		BaseApplication.wyyCode = wyyCode;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

}
