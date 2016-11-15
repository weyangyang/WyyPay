package netutils.http;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import netutils.core.AsyncTask;
import netutils.download.DownloadResponse;
import netutils.engine.NetConstants;
import netutils.entityhandler.EntityCallBack;
import netutils.entityhandler.FileEntityHandler;
import netutils.entityhandler.StringEntityHandler;
import android.os.SystemClock;

public class HttpHandler<T> extends AsyncTask<Object, Object, Object> implements EntityCallBack {

	/**
	 * 连接超时时间，默认 2s
	 */
	protected int connectionTimeout = 2000;
	public static int NOSPACE_ERROR = 1028; //磁盘空间已满

	/**
	 * 读取数据超时时间，默认8s
	 */
	protected int readTimeout = 8000;
	private final StringEntityHandler mStrEntityHandler = new StringEntityHandler();
	private final FileEntityHandler mFileEntityHandler = new FileEntityHandler();

	private final RequestCallBack<T> callback;

	private String targetUrl = null; // 下载的路径
	private boolean isResume = false; // 是否断点续传
	private String charset;

	public HttpHandler(RequestCallBack<T> callback, String charset) {
		this.callback = callback;
		this.charset = charset;
	}

	@SuppressWarnings("unused")
	private void makeRequestWithRetries(String strUrl) throws Exception {
		boolean retry = true;
		Exception cause = null;
		while (retry) {

			publishProgress(UPDATE_START); // 开始
			HttpURLConnection conn = null;
			DownloadResponse response = new DownloadResponse();
			try {
				URL urls = new URL(strUrl);
				conn = (HttpURLConnection) urls.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept-Encoding", "identity");

				if (isResume && targetUrl != null) {
					File downloadFile = new File(targetUrl);
					long fileLen = 0;
					if (downloadFile.isFile() && downloadFile.exists()) {
						fileLen = downloadFile.length();
					}
					if (fileLen > 0) {
						conn.setRequestProperty("RANGE", "bytes=" + fileLen + "-");
					}
				}
				if (conn.getResponseCode() < 400) {
					response.setContentLength(conn.getContentLength());
					response.setmImputStream(conn.getInputStream());
					response.setStatusCode(conn.getResponseCode());
					handleResponse(response);
				} else {
					publishProgress(UPDATE_FAILURE,new Exception("status code >=400"), conn.getResponseCode(), "status code >=400");
				}
			} catch (MalformedURLException e) {
				cause = e;
				retry = false;
				e.printStackTrace();
				return;
			} catch (IOException e) {
				cause = e;
				e.printStackTrace();
				retry = false;
				return;
			} catch (Exception e) {
				cause = e;
				e.printStackTrace();
				retry = false;
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
			return;

		}
		if (cause != null)
			throw cause;
		else
			throw new ReqErrException("unknown net error", NetConstants.UNKNOWN_NET_ERROR);

	}

	@Override
	protected Object doInBackground(Object... params) {
		if (params != null && params.length == 3) {
			targetUrl = String.valueOf(params[1]);
			isResume = (Boolean) params[2];
		}
		try {
			publishProgress(UPDATE_START); // 开始
			makeRequestWithRetries((String) params[0]);
		} catch (ReqErrException e) {
			publishProgress(UPDATE_FAILURE, e, e.getErrCode(), e.getMessage()); // 结束
		} catch (IOException ex) {
			publishProgress(UPDATE_FAILURE, ex, 10, ex.getMessage()); // 结束

		} catch (Exception e) {
			publishProgress(UPDATE_FAILURE, e, 10, e.getMessage()); // 结束
		}

		return null;
	}

	private final static int UPDATE_START = 1;
	private final static int UPDATE_LOADING = 2;
	private final static int UPDATE_FAILURE = 3;
	private final static int UPDATE_SUCCESS = 4;

	@SuppressWarnings("unchecked")
	@Override
	protected void onProgressUpdate(Object... values) {
		int update = Integer.valueOf(String.valueOf(values[0]));
		switch (update) {
		case UPDATE_START:
			if (callback != null)
				callback.onStart();
			break;
		case UPDATE_LOADING:
			if (callback != null)
				callback.onLoading(Long.valueOf(String.valueOf(values[1])), Long.valueOf(String.valueOf(values[2])));
			break;
		case UPDATE_FAILURE:
			if (callback != null)
				callback.onFailure((Throwable) values[1], (Integer) values[2], (String) values[3]);
			break;
		case UPDATE_SUCCESS:
			if (callback != null)
				callback.onSuccess((T) values[1]);
			break;
		default:
			break;
		}
		super.onProgressUpdate(values);
	}

	public boolean isStop() {
		return mFileEntityHandler.isStop();
	}

	/**
	 * 停止下载任务
	 */
	public void stop() {
		mFileEntityHandler.setStop(true);
	}

	/**
	 * 删除下载任务
	 */
	public void delete() {
		mFileEntityHandler.setDelete(true);
		mFileEntityHandler.setStop(true);
	}

	private void handleResponse(DownloadResponse response) {
		int statusCode = response.getStatusCode();
		if (statusCode >= 300) {
			String errorMsg = "response status error code:" + statusCode;
			if (statusCode == NetConstants.DOWNLOAD_COMPLETE && isResume) {
				errorMsg += " \n maybe you have download complete.";
			}
			publishProgress(UPDATE_FAILURE, new Exception(errorMsg),statusCode,errorMsg);
			// new HttpResponseException(status.getStatusCode(),
			// status.getReasonPhrase()),
			// status.getStatusCode(), errorMsg);
		} else {
			try {
				// HttpEntity entity = response.getEntity();
				// byte[] bytes = response.getBody();
				Object responseBody = null;
				if (response.getmImputStream() != null) {
					time = SystemClock.uptimeMillis();
					if (targetUrl != null) {
						responseBody = mFileEntityHandler.handleEntity(response, this, targetUrl, isResume);
					} else {
						responseBody = mStrEntityHandler.handleEntity(response, this, charset);
					}

				}
				publishProgress(UPDATE_SUCCESS, responseBody);

			} catch (ReqErrException e) {
				publishProgress(UPDATE_FAILURE, e, e.getErrCode(), e.getMessage());
			} catch (IOException e) {
				ReqErrException cause =null;
				if (e.toString().toLowerCase().contains("no space left on device")) {
					cause = new ReqErrException(e,NOSPACE_ERROR);
				}else{
					cause = new ReqErrException(e, NetConstants.DOWNLOAD_FILE_IOEXCEPTION);
				}
				publishProgress(UPDATE_FAILURE, cause, cause.getErrCode(), cause.getMessage());
			}

		}
	}

	private long time;

	@Override
	public void callBack(long count, long current, boolean mustNoticeUI) {
		if (callback != null && callback.isProgress()) {
			if (mustNoticeUI) {
				publishProgress(UPDATE_LOADING, count, current);
			} else {
				long thisTime = SystemClock.uptimeMillis();
				if (thisTime - time >= callback.getRate()) {
					time = thisTime;
					publishProgress(UPDATE_LOADING, count, current);
				}
			}
		}
	}

}
