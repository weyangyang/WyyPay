package netutils.interf;

import java.io.File;
import java.util.List;

import netutils.engine.NetReqCallBack;
import netutils.http.HttpHandler;
import netutils.http.HttpHeader;
import netutils.http.RequestCallBack;
import netutils.http.RequestParams;
import netutils.httpclient.core.ParameterList;

import org.apache.http.NameValuePair;

public interface RequestInterf {
	//--------------------post请求-------------------------
	@Deprecated
    public void post(String strUrl,List<NameValuePair> nameValuePairs,
            NetReqCallBack mNetReqCallBack);
	@Deprecated
    public void post(String strUrl,HttpHeader header,List<NameValuePair> nameValuePairs,
    		NetReqCallBack mNetReqCallBack);
	@Deprecated
    public void post(String strUrl,NetReqCallBack mNetReqCallBack, NameValuePair... params);
	@Deprecated
    public void post(String strUrl,HttpHeader header,NetReqCallBack mNetReqCallBack, NameValuePair... params);
	 /**
      * @author liyusheng
      * @version android2.5
      */
    public void post(String strUrl,ParameterList paramsList, NetReqCallBack dataCallBack);
    //--------------------get请求-------------------------
    public void get(String strUrl,NetReqCallBack mNetReqCallBack);
    public void get(String strUrl,HttpHeader header,NetReqCallBack mNetReqCallBack);
    public String get(String strUrl, HttpHeader header);
    /**
     * @author liyusheng
     * @version android2.5
     */
    public void get(String strUrl,ParameterList paramsList, NetReqCallBack dataCallBack);
    
    public void get(String strUrl,HttpHeader header,RequestParams params,NetReqCallBack mNetReqCallBack);
    public void get(String strUrl,RequestParams params,NetReqCallBack mNetReqCallBack);
    
    //--------------------下载-------------------------
    public HttpHandler<File> download(String strUrl,String target,RequestCallBack<File> callBack);
    public HttpHandler<File> download(String strUrl,String target,RequestParams params, RequestCallBack<File> callBack);
    public HttpHandler<File> download( String url,RequestParams params, String target,boolean isResume, RequestCallBack<File> callback);
    public HttpHandler<File> download( String url,String target,boolean isResume, RequestCallBack<File> callback);
    
    //--------------------文件上传-------------------------
     public String post(String url, HttpHeader header, File mFile, NetReqCallBack mNetReqCallBack) ;
     public String post(String url, File mFile, NetReqCallBack mNetReqCallBack);
    
    //----------------------put请求------------------------
//   public void put(String url, RequestCallBack<? extends Object> callBack);
     @Deprecated
     public void put(String strUrl,HttpHeader header,List<NameValuePair>nameValuePairs, NetReqCallBack mNetReqCallBack);
     /**
      * @author liyusheng
      * @version android2.5
      */
     public void put(String strUrl, ParameterList paramsList, NetReqCallBack mNetReqCallBack);
    //----------------------delete请求---------------------
     /**
      * @author liyusheng
      * @version android2.5
      */
     public void delete(String url,ParameterList paramsList,NetReqCallBack mNetReqCallBack);
    public void delete( String url,NetReqCallBack mNetReqCallBack);
    
    public void delete( String url, HttpHeader header, NetReqCallBack mNetReqCallBack);
    @Deprecated
    public void delete( String url, HttpHeader header, List<NameValuePair> nameValuePairs,NetReqCallBack mNetReqCallBack);
    
    
}
