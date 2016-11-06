package netutils.download;

import java.io.InputStream;
import java.io.Serializable;
/**
 * 下载数据返回的response
 * @author liyusheng
 *
 */
public class DownloadResponse implements Serializable {
	private static final long serialVersionUID = -1908908381696468388L;
	private InputStream mImputStream;
	private int statusCode;
	private int contentLength;
	public InputStream getmImputStream() {
		return mImputStream;
	}
	public void setmImputStream(InputStream mImputStream) {
		this.mImputStream = mImputStream;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public int getContentLength() {
		return contentLength;
	}
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	
}
