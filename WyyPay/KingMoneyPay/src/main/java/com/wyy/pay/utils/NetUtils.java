package com.wyy.pay.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;


import com.wyy.pay.R;
import com.wyy.pay.ui.BaseApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetUtils {
	public static final int CMNET = 3;
	public static final int CMWAP = 2;
	public static final int WIFI = 1;
	public static final int UNKNOW = -1;
	public static final int OTHER = 4;

	public static String getNetwork(Context context) {
		int type = getAPNType(context);
		switch (type) {
		case UNKNOW:
			return "unknow";
		case CMNET:
			return "CMNET";
		case CMWAP:
			return "CMWAP";
		case WIFI:
			return "WIFI";
		default:
			return "UNKNOW";
		}
	}
	/**
	 * 检测是否有网络，可以在回调代码里处理业务逻辑
	 * @param mRunningWithNet 回调接口
	 * 
	 * 	@Override
		public void netOK() {
			//TODO:处理有网络的逻辑代码
		}

		@Override
		public void netErr() {
		//TODO:处理没有网络的逻辑代码
			Toast.makeText(BaseApplication.mContext, R.string.net_error, Toast.LENGTH_SHORT).show();
		}
	 */
	public static void isNetForRunning(RunningWithNet mRunningWithNet){
		if(NetUtils.getAPNType(BaseApplication.mContext) != NetUtils.UNKNOW){
			mRunningWithNet.netOK();
		}else{
			mRunningWithNet.netErr();
		}
	}

	public static String getLocalIpAddress() { 
		try { 
			for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) { 
				NetworkInterface intf = (NetworkInterface) en.nextElement(); 
				for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) { 
					InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement(); 
					if (!inetAddress.isLoopbackAddress() && inetAddress.isLoopbackAddress() && inetAddress instanceof InetAddress ){//.isIPv4Address(inetAddress
                        //    .getHostAddress())) { 
						return inetAddress.getHostAddress().toString();
					}
				} 
		 
			}
		}catch(Exception ex) { 
			//Log.e("error", ex.toString()); 
		} 
		return ""; 
	}
	
	
	/**
     * 网络连接
     * @tags @return 是否有连接
     */

    public static String checkNet(Context context) {
        try {
            // 获取手机所有连接管理对象（wi_fi,net等连接的管理）
            ConnectivityManager manger = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manger != null) {
                NetworkInfo info[] = manger.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        	String typeName = info[i].getSubtypeName();
                        	if(TextUtils.isEmpty(typeName)){
                        		 typeName = info[i].getTypeName();
                        	}
                        	return typeName;
                           // return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "";
        }
        return "";

    }
	public static String getHasNetwork(Context context) {
		int type = getAPNType(context); 
		return String.valueOf(type != UNKNOW);
	}
	public static int getAPNType(Context context) {
		int netType = UNKNOW;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		} else {
			if (!networkInfo.isAvailable()) {
				return netType;
			}
		}
		int nType = networkInfo.getType();
		netType = OTHER;
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			try {
				if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
					netType = CMNET;
				} else {
					netType = CMWAP;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = WIFI;
		}
		return netType;
	}

	public static String getErrorToast(Context context, String defaultString) {
		int type = NetUtils.getAPNType(context);
		if (type == NetUtils.UNKNOW) {
			return context.getString(R.string.net_error);
		}
		return defaultString;
	}

}