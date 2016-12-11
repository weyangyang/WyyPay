package com.wyy.pay.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wyy.pay.R;


public class CustomProgressDialog extends Dialog  {
	protected static final String TAG = "CustomProgressDialog";
	private View dialogView = null;
	private static Resources res;
	private Context mContext;
	public CustomProgressDialog(Context context) {
		super(context, R.style.DefaultDialog);
		mContext = context;
		res = context.getResources();
	}

	public CustomProgressDialog(Context context, int layoutID) {
		super(context, R.style.DefaultDialog);
		mContext = context;
		res = context.getResources();
		dialogView = LayoutInflater.from(context).inflate(layoutID, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (dialogView != null) {
			setContentView(dialogView);
		}
	}

	public View getCustomView() {
		return dialogView;
	}


	/**
	 * @param context
	 * @param desc
	 * @param isCancel
	 *            返回键和点击外部是否可以取消
	 */
	public static CustomProgressDialog createLoadingDialog(Context context, String desc,boolean isCancel) {
		final CustomProgressDialog dialog = new CustomProgressDialog(context, R.layout.layout_dialog_progress);
		View view = dialog.getCustomView();
		if (!TextUtils.isEmpty(desc)) {
			TextView descView = (TextView) view.findViewById(R.id.layout_dialog_progress_tv);
			descView.setText(desc);
		}
//		if (dismissListener != null) {
//			dialog.setOnDismissListener(dismissListener);
//		}
		dialog.setCanceledOnTouchOutside(isCancel);
		dialog.setCancelable(isCancel);
		
		//dialog.show();
		return dialog;
	}
	public static CustomProgressDialog createLoadingDialog(Context context) {
		final CustomProgressDialog dialog = new CustomProgressDialog(context, R.layout.layout_dialog_progress);
		View view = dialog.getCustomView();
		TextView descView = (TextView) view.findViewById(R.id.layout_dialog_progress_tv);
		descView.setText(R.string.xlistview_header_hint_loading);
//		if (dismissListener != null) {
//			dialog.setOnDismissListener(dismissListener);
//		}
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
	
		return dialog;
	}
	@Override
	public void show() {
//		if (mContext instanceof BaseActivity) {
//			if (((BaseActivity)mContext).isActive()) {
//				super.show();
//			}else {
//				//activity is not running, do not show.
//			}
//		}
		try {
			super.show();
		}catch(IllegalArgumentException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void dismiss() {
//		if (mContext instanceof BaseActivity) {
//			if (((BaseActivity)mContext).isActive()) {
//				super.dismiss();
//				return;
//			}else {
//				//activity is not running
//			}
//		}
		try {
			super.dismiss();
		}catch(IllegalArgumentException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
