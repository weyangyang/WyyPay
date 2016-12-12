package com.wyy.pay.engine;

import java.util.concurrent.ConcurrentHashMap;

import netutils.engine.NetReqCallBack;
import netutils.http.HttpHeader;
import netutils.http.HttpNetUtils;
import netutils.httpclient.core.ParameterList;
import netutils.httpclient.core.ParameterList.HeaderParameter;
import netutils.httpclient.core.ParameterList.StringParameter;
import netutils.impl.BaseRequest;


public class RequestEngine extends BaseRequest {
	private static RequestEngine mRequestEngine;

	private RequestEngine() {

	}

	public synchronized static RequestEngine getInstance() {
		if (null == mRequestEngine) {
			mRequestEngine = new RequestEngine();
		}
		return mRequestEngine;

	}



	private ParameterList setHeaderParameter(HttpHeader header) {
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		if (header != null) {
			ConcurrentHashMap<String, String> map = header.getHeaderMap();
			for (String key : map.keySet()) {
				paramsList.add(new HeaderParameter(key, map.get(key)));
			}
		}
		return paramsList;
	}
	public void login(String uname,String passwd,NetReqCallBack callBack){
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		paramsList.add(new StringParameter("username", uname));
		paramsList.add(new StringParameter("password", passwd));
		super.post(Urls.BASE_URL + Urls.LOGIN_URL,paramsList, callBack);

	}
	public void createOrder(String uname,String wyyCode,double money,NetReqCallBack callBack){
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		paramsList.add(new StringParameter("username", uname));
		paramsList.add(new StringParameter("wyycode", wyyCode));
		paramsList.add(new StringParameter("money", String.valueOf(money)));
		super.post(Urls.BASE_URL + Urls.GET_DISCOUNT_URL,paramsList, callBack);

	}
	public void getDiscountWithPhone(String uname,String wyyCode,String phone,NetReqCallBack callBack){
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		paramsList.add(new StringParameter("username", uname));
		paramsList.add(new StringParameter("wyycode", wyyCode));
		paramsList.add(new StringParameter("phone", phone));
		super.post(Urls.BASE_URL + Urls.GET_DISCOUNT_URL,paramsList, callBack);

	}
	public void frogetPasswdCommit(String phone,String passwd,String smsCode,NetReqCallBack callBack){
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		paramsList.add(new StringParameter("phone", phone));
		paramsList.add(new StringParameter("newpwd", passwd));
		paramsList.add(new StringParameter("vcode", smsCode));
		super.post(Urls.BASE_URL + Urls.FORGET_PASSWORD_URL,paramsList, callBack);

	}
	public void register(String uname,String passwd,String smsCode,String storeName,NetReqCallBack callBack){
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		paramsList.add(new StringParameter("phone", uname));
		paramsList.add(new StringParameter("password", passwd));
		paramsList.add(new StringParameter("vcode", smsCode));
		paramsList.add(new StringParameter("storename", storeName));
		super.post(Urls.BASE_URL + Urls.REGISTER_URL,paramsList, callBack);

	}

	/**
	 * 支付接口
	 * @param uname 用户名
	 * @param wyyCode 微洋洋code
	 * @param authCode 通过扫描用户付款码获得
	 * @param orderNo  通过创建订单获得的号码
     * @param callBack 回调接口
     */
	public void requestPay(String uname,String wyyCode,String authCode,String orderNo,NetReqCallBack callBack){
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		paramsList.add(new StringParameter("username", uname));
		paramsList.add(new StringParameter("wyycode", wyyCode));
		paramsList.add(new StringParameter("authcode", authCode));
		paramsList.add(new StringParameter("orderno", orderNo));
		super.post(Urls.BASE_URL + Urls.REGISTER_URL,paramsList, callBack);

	}
	public void getSmsVerifyCode(String phone,NetReqCallBack callBack){
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		paramsList.add(new StringParameter("phone", phone));
		super.post(Urls.BASE_URL + Urls.SMS_VERIFY_URL,paramsList, callBack);

	}
	public void postFeedback(HttpHeader header, String content, String contact, String log,
			NetReqCallBack netDataCallBackInterf) {
		ParameterList paramsList = setHeaderParameter(header);
		paramsList.add(new StringParameter("content", content));
		paramsList.add(new StringParameter("contact", contact));
		paramsList.add(new StringParameter("log", log));
		super.post(Urls.BASE_URL + Urls.FEEDBACK_URL,paramsList, netDataCallBackInterf);
	}


//	public void getUpgradeData(HttpHeader header, String strChannel, int strVersionCode, NetReqCallBack mNetReqCallBack) {
//		       super.post(Urls.BASE_URL + Urls.UPGRADE_URL +"?app_version_code="+strVersionCode+"&channel="+strChannel.replace(" ","%20"), header, mNetReqCallBack,null);
//	}


	
	
	
}
