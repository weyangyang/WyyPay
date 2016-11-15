package netutils.http;

import java.net.HttpURLConnection;

import netutils.engine.NetReqCallBack;
import netutils.httpclient.core.AbsHttpClient;
import netutils.httpclient.core.HttpDelete;
import netutils.httpclient.core.HttpGet;
import netutils.httpclient.core.HttpHead;
import netutils.httpclient.core.HttpMethod;
import netutils.httpclient.core.HttpPost;
import netutils.httpclient.core.HttpPut;
import netutils.httpclient.core.HttpRequestException;
import netutils.httpclient.core.HttpResponse;
import netutils.httpclient.core.ParameterList;
import android.os.Build;

/**
 * 
 * @author liyusheng
 * 
 */
public class HttpNetUtils extends AbsHttpClient{
	private static HttpNetUtils mHttpNetUtils;

	public synchronized static HttpNetUtils getHttpClient() {
		if (null == mHttpNetUtils) {
			return new HttpNetUtils();
		}
		return mHttpNetUtils;
	}
	 public HttpNetUtils() {
	        super();
	    }

	    static {
	        disableConnectionReuseIfNecessary();
	        // See http://code.google.com/p/basic-http-client/issues/detail?id=8
	        // if (Build.VERSION.SDK_INT > 8)
	        // ensureCookieManager();
	    }

	    /**
	     * Work around bug in {@link HttpURLConnection} on older versions of
	     * Android.
	     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
	     */
	    @SuppressWarnings("deprecation")
	    private static void disableConnectionReuseIfNecessary() {
	        // HTTP connection reuse which was buggy pre-froyo
	        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
	            System.setProperty("http.keepAlive", "false");
	        }
	    }

	    @Override
	    public HttpResponse head(String path, ParameterList params,NetReqCallBack mNetReqCallBack) throws HttpRequestException{
	        HttpMethod req = new HttpHead(path, params);
	        return tryMany(req,mNetReqCallBack);
	    }

	    @Override
	    public HttpResponse get(String path, ParameterList params,NetReqCallBack mNetReqCallBack) {
	        HttpMethod req = new HttpGet(path, params);
	        return tryMany(req,mNetReqCallBack);
	    }

	    @Override
	    public HttpResponse post(String path, ParameterList params,NetReqCallBack mNetReqCallBack) {
	        HttpMethod req = new HttpPost(path, params);
	        return tryMany(req,mNetReqCallBack);
	    }

	    @Override
	    public HttpResponse post(String path, String contentType, byte[] data,NetReqCallBack mNetReqCallBack)
	            throws HttpRequestException {
	        HttpMethod req = new HttpPost(path, null, contentType, data);
	        return tryMany(req,mNetReqCallBack);
	    }

	    @Override
	    public HttpResponse put(String path, String contentType, byte[] data,NetReqCallBack mNetReqCallBack)
	            throws HttpRequestException {
	        HttpMethod req = new HttpPut(path, null, contentType, data);
	        return tryMany(req,mNetReqCallBack);
	    }
	    public HttpResponse put(String path, ParameterList params,NetReqCallBack mNetReqCallBack){
	    	HttpMethod req = new HttpPut(path,params);
	    	return tryMany(req,mNetReqCallBack);
	    }

	    @Override
	    public HttpResponse delete(String path, ParameterList params,NetReqCallBack mNetReqCallBack) {
	        HttpMethod req = new HttpDelete(path, params);
	        return tryMany(req,mNetReqCallBack);
	    }
}
