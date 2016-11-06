package netutils.interf;

import netutils.httpclient.core.HttpResponse;

/**
 * @description 获取网络数据的回调接口
 *
 */
public interface NetDataCallBackInterf {
	/**
	 * @description 获取网络请求返回的数据
	 * @return strJson
	 * @param strJson
	 */
	public void getSuccData(int statusCode,String strJson,String strUrl);
	/**
	 * @description 获取网络请求返回的异常数据
	 * @param errCode
	 * @param strError
	 */
	public void getErrData(int statusCode,String strJson,String strUrl);
	/**
	 * @description 获取网络请求时产生的各种异常数据
	 * @param exceptionCode
	 * @param strMsg
	 */
	public void getExceptionMsg(int exceptionCode,String strMsg,String strUrl);
	/**
	 * 获取网络请求超时数据
	 * @param exceptionCode
	 * @param strMsg
	 * @param strUrl
	 */
	public void getTimeOutMsg(int exceptionCode,String strMsg,String strUrl);
	/**
	 * 返回所有response 数据
	 * @param mHttpResponse
	 */
	public void getSuccHttpResponse(HttpResponse mHttpResponse);

}
