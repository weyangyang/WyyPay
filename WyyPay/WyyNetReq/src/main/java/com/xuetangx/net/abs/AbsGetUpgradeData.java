package com.xuetangx.net.abs;

import android.app.Activity;

import com.xuetangx.net.data.interf.GetUpgradeDataInterf;
import com.xuetangx.utils.ToastUtils;

public abstract class AbsGetUpgradeData implements GetUpgradeDataInterf {
	private Activity mActivity;

	public AbsGetUpgradeData() {
	};

	public AbsGetUpgradeData(Activity mActivity) {
		this.mActivity = mActivity;
	}
	@Override
	public void getErrData(int errCode, String errMsg,String strUrl) {
		ToastUtils.httpToast(mActivity, errMsg);
		//TODO:XXX 请求服务器失败后的数据
	}

	@Override
	public void getParserErrData(int errCode, String errMsg,String strUrl) {
		ToastUtils.httpToast(mActivity, errMsg);
		//TODO:解析异常后的数据

	}

	@Override
	public void getExceptionData(int errCode, String errMsg,String strUrl) {
		// TODO Auto-generated method stub
		
	}

}
