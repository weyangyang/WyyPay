package config.bean;

import android.content.Context;
import android.text.TextUtils;

import xtcore.utils.PreferenceUtils;
/**
 * 外部初始化上下文对象类
 * @author liyusheng
 */
public final class XTCoreConfig {
	private static Context context;
	private static String strEvent;
	public static void initConfig(Context context){
		ConfigBean.getInstance().setContext(context);
		setContext(context);
	}



	/**
	 * 创建用户uid
	 * @param context
	 */
	 static String createUID(Context context) {
		 String strUID="";
		 String spUID = PreferenceUtils.getPrefString(context, PreferenceUtils.SP_UID, "");
		 if(!TextUtils.isEmpty(spUID)){
			 return spUID;
		 }else{
			 strUID ="!"+ PreferenceUtils.getPrefString(context, PreferenceUtils.SP_UUID, "");
		 }
		return strUID;
	}

	 static Context getContext() {
		return context;
	}
	 static void setContext(Context context) {
		 XTCoreConfig.context = context;
	}
}
