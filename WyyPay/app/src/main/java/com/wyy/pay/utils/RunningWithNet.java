package com.wyy.pay.utils;

import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.ui.BaseApplication;

/**
 * 处理网络状态逻辑代码抽像类，构造里不传参数则默认在没有网络时显示errToast
 * @author liyusheng
 *private boolean showErrToast;
	public RunningWithNet(){
		showErrToast = true;
	}
	public RunningWithNet(boolean showErrToast){
		this.showErrToast = showErrToast;
	}
 */
public abstract class RunningWithNet implements RunningWithNetInterf{
	private boolean showErrToast;
	public RunningWithNet(){
		showErrToast = true;
	}
	public RunningWithNet(boolean showErrToast){
		this.showErrToast = showErrToast;
	}
	@Override
	public void netErr() {
		if(showErrToast){
			Toast.makeText(BaseApplication.mContext, R.string.net_error, Toast.LENGTH_SHORT).show();
		}
	}
	
}
