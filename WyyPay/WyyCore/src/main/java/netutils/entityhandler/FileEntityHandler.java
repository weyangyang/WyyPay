package netutils.entityhandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import netutils.download.DownloadResponse;
import netutils.engine.NetConstants;
import netutils.http.ReqErrException;
import android.text.TextUtils;
//TODO:XXX delete方法需测试
public class FileEntityHandler {
	
	private boolean mStop = false;
	private boolean mDelete = false;
	
	public boolean isDelete(){
		return mDelete;
	}
	public boolean isStop() {
		return mStop;
	}


	public void setDelete(boolean delete){
		this.mDelete = delete;
	}
	public void setStop(boolean stop) {
		this.mStop = stop;
	}


	@SuppressWarnings("resource")
	public Object handleEntity(DownloadResponse response, EntityCallBack callback,String target,boolean isResume) throws IOException,ReqErrException{
		if (TextUtils.isEmpty(target) || target.trim().length() == 0)
			return null;

		File targetFile = new File(target);

		if (!targetFile.exists()) {
			targetFile.createNewFile();
		}
		if(mStop){
			return targetFile;
		}
		deleteFile(targetFile);
			
		
		long current = 0;
		FileOutputStream os = null;
		if(isResume){
			current = targetFile.length();
			os = new FileOutputStream(target, true);
		}else{
			os = new FileOutputStream(target);
		}
		
		if(mStop){
			return targetFile;
		}
		deleteFile(targetFile);	
//		InputStream input = new ByteArrayInputStream(bytes); 
		long count = response.getContentLength() + current;
		
		if((current >= count || mStop)&&response.getContentLength()>=0){
			return targetFile;
		}
		deleteFile(targetFile);
		int readLen = 0;
		byte[] buffer = new byte[1024];
		while (!mStop && ((readLen = response.getmImputStream().read(buffer,0,1024)) > 0) ) {//未全部读取
			os.write(buffer, 0, readLen);
			current += readLen;
			callback.callBack(count, current,false);
		}
//		while (!mStop && !(current >= count) && ((readLen = response.getmImputStream().read(buffer,0,1024)) > 0) ) {//未全部读取
//			os.write(buffer, 0, readLen);
//			current += readLen;
//			callback.callBack(count, current,false);
//		}
		callback.callBack(count, current,true);
		
		if(mStop && current < count){ //用户主动停止
			throw new ReqErrException("user stop download thread",NetConstants.USER_STOP_DOWNLOAD_THREAD);
		}
		deleteFile(targetFile);
		return targetFile;
	}
	private Object deleteFile(File targetFile) {
		if(mDelete&&targetFile.exists()){
			targetFile.delete();
			return null;
		}
		return targetFile;
	}

}
