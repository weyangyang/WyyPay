package netutils.engine;

import netutils.httpclient.core.HttpResponse;
import netutils.interf.NetDataCallBackInterf;
/**
 * 接收网络数据回调抽象类
 * @author liyusheng
 * 
 * 
 * 如果要处理网络访问失败或异常，请覆盖以下两个方法：
 * @Override
	public void getErrData(int statusCode, String strJson) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getExceptionMsg(int exceptionCode, String strMsg) {
		// TODO Auto-generated method stub

	}
 *
 */
public abstract class NetReqCallBack implements NetDataCallBackInterf {

	@Override
	public void getTimeOutMsg(int exceptionCode, String strMsg, String strUrl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSuccHttpResponse(HttpResponse mHttpResponse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getErrData(int statusCode, String strJson,String strUrl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getExceptionMsg(int exceptionCode, String strMsg,String strUrl) {
		// TODO Auto-generated method stub

	}

}
