package netutils.entityhandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import netutils.download.DownloadResponse;

public class StringEntityHandler {

	public Object handleEntity(DownloadResponse response, EntityCallBack callback,String charset)throws IOException {
		if (response == null){
			return null;
		}
		
		InputStream is = response.getmImputStream();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		
		long count = response.getContentLength();
		long curCount = 0;
		int len = -1;
//		InputStream is = entity.getContent();
		while ((len = is.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
			curCount += len;
			if(callback!=null)
				callback.callBack(count, curCount,false);
		}
		if(callback!=null)
			callback.callBack(count, curCount,true);
		byte[] data = outStream.toByteArray();
		outStream.close();
		is.close();
		return new String(data,charset);
	}

}
