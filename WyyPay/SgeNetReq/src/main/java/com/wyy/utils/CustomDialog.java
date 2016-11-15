package com.wyy.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wyy.R;

public class CustomDialog extends Dialog {
	protected static final String TAG = "CustomDialog";
	private View dialogView = null;
	private static Resources res;
	public CustomDialog(Context context) {
		super(context, R.style.CustomDialogTheme);
		res = context.getResources();
	}

	public CustomDialog(Context context, int layoutID) {
		super(context, R.style.CustomDialogTheme);
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
	@Override
	public void dismiss(){
		try {
			super.dismiss();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void show(){
		try {
			super.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param context
	 *            如果不需要，置null
	 * @param desc
	 * @param isCancel
	 *            返回键和点击外部是否可以取消
	 */
	public static CustomDialog createLoadingDialog(Context context, String desc,boolean isCancel) {
		final CustomDialog dialog = new CustomDialog(context, R.layout.dialog_loading);
		View view = dialog.getCustomView();
		if (!TextUtils.isEmpty(desc)) {
			TextView descView = (TextView) view.findViewById(R.id.textv_dialogLoading);
			descView.setText(desc);
		}
//		if (dismissListener != null) {
//			dialog.setOnDismissListener(dismissListener);
//		}
		dialog.setCanceledOnTouchOutside(isCancel);
		dialog.setCancelable(isCancel);
		
		dialog.show();
		return dialog;
	}
	
	


}
