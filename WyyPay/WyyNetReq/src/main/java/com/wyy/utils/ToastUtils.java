package com.wyy.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public static void toast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void toast(Context context, int resId) {
		toast(context, context.getResources().getString(resId));
	}

	public static void httpToast(final Activity activity, final String errMsg) {
		if (activity != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ToastUtils.toast(activity, errMsg);
				}
			});
		}
	}

	public static void httpToast(final Activity activity, final int errMsg) {
		if (activity != null) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ToastUtils.toast(activity, errMsg);
				}
			});
		}
	}

}
