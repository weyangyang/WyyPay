package netutils.engine;


import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import netutils.http.HttpHandler;
import netutils.http.RequestCallBack;
import netutils.http.RequestParams;

public class ThreadPoolHttp {

    protected static int httpThreadCount = 5;//http线程池数量
    protected String charset = "utf-8";
    
    
    private static final ThreadFactory  sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        public Thread newThread(Runnable r) {
        	Thread tread = new Thread(r, "xtcore" + mCount.getAndIncrement());
        	tread.setPriority(Thread.NORM_PRIORITY - 1);
            return tread;
        }
    };
    
    protected static final Executor executor =Executors.newFixedThreadPool(httpThreadCount, sThreadFactory);
    
    public void configCharset(String charSet){
    	if(charSet!=null && charSet.trim().length()!=0)
    		this.charset = charSet;
    }


    
    //---------------------下载---------------------------------------
    public HttpHandler<File> download(String url,String target,RequestCallBack<File> callback){
    	return download(url, null, target, false, callback);
    }
    

    public HttpHandler<File> download(String url,String target,boolean isResume,RequestCallBack<File> callback){
    	 return download(url, null, target, isResume, callback);
    }
    
    public HttpHandler<File> download( String url,RequestParams params, String target, RequestCallBack<File> callback) {
   	 	return download(url, params, target, false, callback);
    }
    
    public HttpHandler<File> download( String url,RequestParams params, String target,boolean isResume, RequestCallBack<File> callback) {
//    	URI uri = URIBuilder.build(getUrlWithQueryString(url, params), Charset.forName(charset));
    	url = getUrlWithQueryString(url, params);
//    	 HttpMethod mHttpMethod = new HttpGet(uri.toString(), null);
    	HttpHandler<File> handler = new HttpHandler<File>(callback,charset);
    	handler.executeOnExecutor(executor,url,target,isResume);
    	return handler;
    }

    public static String getUrlWithQueryString(String url, RequestParams params) {
        try {
            URI uri = new URI(url);
            url = uri.toASCIIString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(params != null) {
            String paramString = params.getParamString();
            url += "?" + paramString;
        }
        return url.replaceAll(" ", "%20");
    }

}
