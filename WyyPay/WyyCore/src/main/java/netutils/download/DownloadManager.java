package netutils.download;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import netutils.engine.NetReqCallBack;
import netutils.engine.ThreadPoolHttp;
import netutils.http.HttpHandler;
import netutils.http.HttpNetUtils;
import netutils.http.RequestCallBack;
import android.text.TextUtils;

/**
 * 多线程下载管理类
 * 
 * @author liyusheng
 * 
 */
//TODO:XXX 删除下载任务需测试
public class DownloadManager implements Serializable{
	private static final long serialVersionUID = 6963541952846912778L;
	private static DownloadManager mDownloadManager;
	private static RequestCallBack<File> requestCallBack;
	private static HttpHandler<File> handler;
	
	public static DownloadManager getInstance() {
		if (mDownloadManager == null) {
			mDownloadManager = new DownloadManager();
		}
		return mDownloadManager;
	}
	protected RequestCallBack<File> getDownloadCallback(){
		return requestCallBack;
	}
	private DownloadManager() {
		handlerMap = new HashMap<String, Object>();
		targetMap = new HashMap<String, Object>();
		callBackMap = new HashMap<String, Object>();
	}
	public HttpHandler<File> getHeader(String ccid){
		Object obj = handlerMap.get(ccid);
		if(obj!=null){
			return (HttpHandler<File>) obj;
		}else{
			return null;
		}
		
	}
	private static HashMap<String, Object> handlerMap;
	private static HashMap<String, Object> targetMap;
	private static HashMap<String, Object> callBackMap;
	public static void addHandler(String url,String target){
		addHandler(url, target, requestCallBack);
	}
	// 添加
	public static void addHandler(String strCCID, String target, RequestCallBack<File> callback) {
		if (TextUtils.isEmpty(strCCID) || TextUtils.isEmpty(target)) {
			return;
		}
//		HttpNetUtils.get(url, header, new NetReqCallBack() {
//			
//			@Override
//			public void getSuccData(int statusCode, String strJson) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
				handler = new ThreadPoolHttp().download(strCCID, target, true, callback);
				handlerMap.put(strCCID, handler);
				handlerMap.put("url", strCCID);
				targetMap.put(strCCID, target);
				callBackMap.put(strCCID, callback);
				
	}

	// 继续
	@SuppressWarnings("unchecked")
	public static void continueHandler(String strCCID) {
		isEmpty(strCCID);
		String target = (String) targetMap.get(strCCID);
		RequestCallBack<File> callback = (RequestCallBack<File>) callBackMap.get(strCCID);
		addHandler(strCCID, target, callback);
	}

	private static void isEmpty(String strCCID) {
		String mapUrl = (String) handlerMap.get("strCCID");
		if (TextUtils.isEmpty(strCCID)&&!strCCID.equals(mapUrl)) {
			return;
		}
	}

	// 暂停
	
	@SuppressWarnings("unchecked")
	public static void pauseHandler(String strCCID) {
		isEmpty(strCCID);
		handler = (HttpHandler<File>) handlerMap.get(strCCID);
		handler.stop();
	}

	// 删除
	@SuppressWarnings("unchecked")
	public static void deleteHandler(String strCCID) {
		isEmpty(strCCID);
		handler = (HttpHandler<File>) handlerMap.get(strCCID);
		handler.delete();
		handlerMap.clear();
		callBackMap.clear();
		targetMap.clear();
	}
	
	@SuppressWarnings("static-access")
	public void setDownloadCallBack(RequestCallBack<File> requestCallBack){
		this.requestCallBack = requestCallBack;
	}
	
}
