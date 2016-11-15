package netutils.impl;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import netutils.engine.NetReqCallBack;
import netutils.engine.ThreadPoolHttp;
import netutils.http.HttpHandler;
import netutils.http.HttpHeader;
import netutils.http.HttpNetUtils;
import netutils.http.RequestCallBack;
import netutils.http.RequestParams;
import netutils.httpclient.core.ParameterList;
import netutils.httpclient.core.ParameterList.FileParameter;
import netutils.httpclient.core.ParameterList.HeaderParameter;
import netutils.httpclient.core.ParameterList.StringParameter;
import netutils.interf.RequestInterf;

import org.apache.http.NameValuePair;

import android.text.TextUtils;

@SuppressWarnings("deprecation")
public abstract class BaseRequest implements RequestInterf {

	@Override
	public void post(String strUrl, List<NameValuePair> nameValuePairs, NetReqCallBack dataCallBack) {
		ParameterList paramsList = changeParameter(null, nameValuePairs);
		HttpNetUtils.getHttpClient().post(strUrl, paramsList, dataCallBack);
		
	}
	public void post(String strUrl,ParameterList paramsList, NetReqCallBack dataCallBack){
		HttpNetUtils.getHttpClient().post(strUrl, paramsList, dataCallBack);
	};
	@Override
	public void post(String strUrl, NetReqCallBack dataCallBack, NameValuePair... params) {
		ParameterList paramsList = changeParameter(null, params);
		HttpNetUtils.getHttpClient().post(strUrl, paramsList, dataCallBack);
	}

	@Override
	public void get(String strUrl, NetReqCallBack dataCallBack) {
		HttpNetUtils.getHttpClient().get(strUrl, null, dataCallBack);
	}
	public void get(String strUrl,ParameterList paramsList, NetReqCallBack dataCallBack){
		HttpNetUtils.getHttpClient().get(strUrl, paramsList, dataCallBack);
	}
	@Override
	public void get(String strUrl, RequestParams params, NetReqCallBack dataCallBack) {
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		paramsList.add(new StringParameter(params.getParamString(), ""));
		HttpNetUtils.getHttpClient().get(strUrl, paramsList, dataCallBack);
	}

	@Override
	public HttpHandler<File> download(String strUrl, String target, RequestCallBack<File> callBack) {
		return new ThreadPoolHttp().download(strUrl, target, callBack);
	}

	@Override
	public HttpHandler<File> download(String strUrl, String target, RequestParams params, RequestCallBack<File> callBack) {
		return new ThreadPoolHttp().download(strUrl, params, target, callBack);
	}

	@Override
	public HttpHandler<File> download(String url, RequestParams params, String target, boolean isResume,
			RequestCallBack<File> callback) {
		return new ThreadPoolHttp().download(url, params, target, isResume, callback);
	}

	@Override
	public HttpHandler<File> download(String url, String target, boolean isResume, RequestCallBack<File> callback) {
		return new ThreadPoolHttp().download(url, target, isResume, callback);
	}

	@Override
	public void post(String strUrl, HttpHeader header, List<NameValuePair> nameValuePairs,
			NetReqCallBack mNetReqCallBack) {
		ParameterList paramsList = changeParameter(header, nameValuePairs);
		HttpNetUtils.getHttpClient().post(strUrl, paramsList, mNetReqCallBack);
	}

	private ParameterList changeParameter(HttpHeader header) {
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		if (header != null) {
			ConcurrentHashMap<String, String> map = header.getHeaderMap();
			for (String key : map.keySet()) {
				paramsList.add(new HeaderParameter(key, map.get(key)));
			}
		}
		return paramsList;
	}

	private ParameterList changeParameter(HttpHeader header, RequestParams params) {
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		if (header != null) {
			ConcurrentHashMap<String, String> map = header.getHeaderMap();
			for (String key : map.keySet()) {
				paramsList.add(new HeaderParameter(key, map.get(key)));
			}
		}
		if (params != null) {
			ConcurrentHashMap<String, String> map = params.getParamsMap();
			for (String key : map.keySet()) {
				paramsList.add(new StringParameter(key, map.get(key)));
			}
		}
		return paramsList;
	}

	private ParameterList changeParameter(HttpHeader header, List<NameValuePair> nameValuePairs) {
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		if (header != null) {
			ConcurrentHashMap<String, String> map = header.getHeaderMap();
			for (String key : map.keySet()) {
				paramsList.add(new HeaderParameter(key, map.get(key)));
			}
		}
		if (nameValuePairs != null && nameValuePairs.size() > 0) {
			for (NameValuePair p : nameValuePairs) {
				//TODO:XXX 此处已处理参数为null，但应该把这种判断放在上层去处理
				if (TextUtils.isEmpty(p.getValue())) {

				} else {
					paramsList.add(new StringParameter(p.getName(), p.getValue()));
				}
			}
		}
		return paramsList;
	}

	private ParameterList changeParameter(HttpHeader header, NameValuePair... nameValuePairs) {
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		if (null != header) {
			ConcurrentHashMap<String, String> map = header.getHeaderMap();
			for (String key : map.keySet()) {
				paramsList.add(new HeaderParameter(key, map.get(key)));
			}
		}
		if (null != nameValuePairs && nameValuePairs.length > 0) {
			for (NameValuePair p : nameValuePairs) {
				//TODO:XXX 此处已处理参数为null，但应该把这种判断放在上层去处理
				if (TextUtils.isEmpty(p.getValue())) {

				} else {
					paramsList.add(new StringParameter(p.getName(), p.getValue()));
				}
			}
		}
		return paramsList;
	}

	@Override
	public void post(String strUrl, HttpHeader header, NetReqCallBack mNetReqCallBack, NameValuePair... nameValuePairs) {
		ParameterList paramsList = changeParameter(header, nameValuePairs);
		HttpNetUtils.getHttpClient().post(strUrl, paramsList, mNetReqCallBack);
	}

	@Override
	public void get(String strUrl, HttpHeader header, NetReqCallBack mNetReqCallBack) {
		ParameterList paramsList = changeParameter(header);
		HttpNetUtils.getHttpClient().get(strUrl, paramsList, mNetReqCallBack);

	}

	@Override
	public String get(String strUrl, HttpHeader header) {
		ParameterList paramsList = changeParameter(header);
		return HttpNetUtils.getHttpClient().get(strUrl, paramsList, null).getBodyAsString();

	}

	@Override
	public void get(String strUrl, HttpHeader header, RequestParams params, NetReqCallBack mNetReqCallBack) {
		ParameterList paramsList = changeParameter(header, params);
		HttpNetUtils.getHttpClient().get(strUrl, paramsList, mNetReqCallBack);
	}

	@Override
	public void delete(String url, HttpHeader header, List<NameValuePair> nameValuePairs, NetReqCallBack mNetReqCallBack) {
		ParameterList paramsList = changeParameter(header, nameValuePairs);
		HttpNetUtils.getHttpClient().delete(url, paramsList, mNetReqCallBack);

	}

	@Override
	public void delete(String url, NetReqCallBack mNetReqCallBack) {

		HttpNetUtils.getHttpClient().delete(url, null, mNetReqCallBack);
	}
	public void delete(String url,ParameterList paramsList,NetReqCallBack mNetReqCallBack){
	HttpNetUtils.getHttpClient().delete(url, paramsList, mNetReqCallBack);
	}
	@Override
	public void delete(String url, HttpHeader header, NetReqCallBack mNetReqCallBack) {
		ParameterList paramsList = changeParameter(header);
		HttpNetUtils.getHttpClient().delete(url, paramsList, mNetReqCallBack);
	}

	@Override
	public void put(String strUrl, HttpHeader header, List<NameValuePair> nameValuePairs, NetReqCallBack mNetReqCallBack) {
		ParameterList paramsList = changeParameter(header, nameValuePairs);
		HttpNetUtils.getHttpClient().put(strUrl, paramsList, mNetReqCallBack);
	}
	public void put(String strUrl, ParameterList paramsList, NetReqCallBack mNetReqCallBack){
		HttpNetUtils.getHttpClient().put(strUrl, paramsList, mNetReqCallBack);
	}
	@Override
	public String post(String url, HttpHeader header, File mFile, NetReqCallBack mNetReqCallBack) {
		ParameterList paramsList = changeParameter(header);
		paramsList.add(new FileParameter("file", mFile));
		return HttpNetUtils.getHttpClient().post(url, paramsList, mNetReqCallBack).getBodyAsString();
	}

	@Override
	public String post(String url, File mFile, NetReqCallBack mNetReqCallBack) {
		ParameterList paramsList = HttpNetUtils.getHttpClient().newParams();
		paramsList.add(new FileParameter("file", mFile));
		return HttpNetUtils.getHttpClient().post(url, paramsList, mNetReqCallBack).getBodyAsString();
	}

}
