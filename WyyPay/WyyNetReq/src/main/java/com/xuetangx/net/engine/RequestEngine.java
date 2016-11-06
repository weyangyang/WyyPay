package com.xuetangx.net.engine;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import netutils.engine.NetReqCallBack;
import netutils.http.HttpHeader;
import netutils.http.HttpNetUtils;
import netutils.http.RequestParams;
import netutils.httpclient.core.ParameterList;
import netutils.httpclient.core.ParameterList.HeaderParameter;
import netutils.httpclient.core.ParameterList.StringParameter;
import netutils.impl.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xtcore.utils.SubstringUtils;
import xtcore.utils.ZipUtils;
import android.text.TextUtils;

import com.xuetangx.net.config.Urls;

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

	public void postFeedback(HttpHeader header, String content, String contact, String log,
			NetReqCallBack netDataCallBackInterf) {
		ParameterList paramsList = setHeaderParameter(header);
		paramsList.add(new StringParameter("content", content));
		paramsList.add(new StringParameter("contact", contact));
		paramsList.add(new StringParameter("log", log));
		super.post(Urls.BASE_URL + Urls.FEEDBACK_URL,paramsList, netDataCallBackInterf);
	}


	public void getUpgradeData(HttpHeader header, String strChannel, int strVersionCode, NetReqCallBack mNetReqCallBack) {
		super.post(Urls.BASE_URL + Urls.UPGRADE_URL +"?app_version_code="+strVersionCode+"&channel="+strChannel.replace(" ","%20"), header, mNetReqCallBack,null);
	}


	
	
	
}
