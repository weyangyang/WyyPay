package netutils.http;

import java.io.IOException;

public class ReqErrException extends IOException {
	private static final long serialVersionUID = -5673184769637998155L;
	private int errCode;
	public ReqErrException(){
		
	}
	public ReqErrException(String message,int errCode){
		super(message);
		this.errCode = errCode;
	}
	public ReqErrException(String message, Throwable cause,int errCode){
		super(message);
		this.errCode = errCode;
	}
	public ReqErrException(Throwable cause,int errCode){
		this.errCode = errCode;
	}
	public int getErrCode(){
		return errCode;
	}
}
