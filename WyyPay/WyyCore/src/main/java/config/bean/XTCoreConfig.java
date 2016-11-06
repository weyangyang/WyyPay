package config.bean;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;

import log.engine.DeviceInfo2Json;
import log.engine.UpLoadLog;
import log.engine.UploadLogStrategyBean;
import log.engine.interf.UploadCallBackInterf;
import netutils.core.FileNameGenerator;
import xtcore.utils.LogUtils;
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
		initData(context);
		try{
			uploadEngine(context);
		}catch(Exception e){
		}
	}
private static void initData(Context context) {
		try {
			DeviceInfo2Json.getDeviceJson(true);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		long currentTime =PreferenceUtils.getPrefLong(context, PreferenceUtils.SP_CURRENT_TIME, 0);
		if(currentTime==0){
			currentTime = System.currentTimeMillis();
			PreferenceUtils.setPrefLong(context, PreferenceUtils.SP_CURRENT_TIME, currentTime);
		}
		
		PreferenceUtils.setPrefInt(context, PreferenceUtils.SP_SEQ_NUM, 0);
		String sessionNum = null;
		try {
			int randomNum = Integer.valueOf((int) (Math.random()*10000));
			sessionNum = FileNameGenerator.generator(System.currentTimeMillis()+""+randomNum);
		} catch (Exception e) {
			sessionNum = System.currentTimeMillis()+"";
		}
		ConfigBean.getInstance().setStrSession(sessionNum);
		createUID(context);
	}

	private static void uploadEngine(Context context) {
		
		String url = config.bean.Config.BASE_URL_API+config.bean.Config.UPLOAD_LOG_DEVICE_INFO_URL;
		uploadUUID(url);
		downLogStrategy();
		uploadZipLogFile();
	}

	private static void downLogStrategy() {
		downLogStrategy(new UploadCallBackInterf() {
			
			@Override
			public void succ() {
				//uploadZipLogFile();
				LogUtils.d("下载成功");
			}
			
			@Override
			public void failure(int errCode, String errMsg) {
				LogUtils.e("下载策略失败"+errMsg);
			}
		});
	}

	private static void uploadZipLogFile() {
		boolean isOnLaunchUpload = UploadLogStrategyBean.getInstance().isOnLaunchUpload();
		if(isOnLaunchUpload){
			uploadZipLogFile(new UploadCallBackInterf() {
				@Override
				public void succ() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void failure(int errCode, String errMsg) {
					// TODO Auto-generated method stub
					
				}
			});
		}
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
		
	
	 public static void uploadUUID(String url) {
		new UpLoadLog().uploadUUID(url, new UploadCallBackInterf() {
			
			@Override
			public void succ() {
				PreferenceUtils.setPrefBoolean(getContext(), PreferenceUtils.SP_UPLOAD_UUID_SUCC, true);
				
			}
			
			@Override
			public void failure(int errCode, String errMsg) {
				PreferenceUtils.setPrefBoolean(getContext(), PreferenceUtils.SP_UPLOAD_UUID_SUCC, false);
				
			}
		});
	}
	 static Context getContext() {
		return context;
	}
	 static void setContext(Context context) {
		 XTCoreConfig.context = context;
	}
	public static void downLogStrategy(UploadCallBackInterf mUploadCallBackInterf){
		checkUploadUUIDSucc();//检查上传uuid信息是否成功
		String url = config.bean.Config.BASE_URL_API+config.bean.Config.LOG_UPLOAD_STRATEGY_URL;
		new UpLoadLog().downLogStrategy(url, mUploadCallBackInterf);
		
	}
	private static void checkUploadUUIDSucc() {
		String url;
		boolean uploadUUIDSucc = PreferenceUtils.getPrefBoolean(context, PreferenceUtils.SP_UPLOAD_UUID_SUCC, false);
		if(!uploadUUIDSucc){
			 url = config.bean.Config.BASE_URL_API+config.bean.Config.UPLOAD_LOG_DEVICE_INFO_URL;
			uploadUUID(url);
		}
	}
	public static void uploadZipLogFile(UploadCallBackInterf mUploadCallBackInterf){
		checkUploadUUIDSucc();
		String url = config.bean.Config.BASE_URL_API+config.bean.Config.UPLOAD_ZIP_LOG_URL;
		new UpLoadLog().uploadZipLogFile(url,mUploadCallBackInterf);
	}
	public static void uploadDeviceInfo(UploadCallBackInterf mUploadCallBackInterf){
		String url = config.bean.Config.BASE_URL_API+config.bean.Config.UPLOAD_LOG_DEVICE_INFO_URL;
		new UpLoadLog().uploadUUID(url, mUploadCallBackInterf);
	}
}
