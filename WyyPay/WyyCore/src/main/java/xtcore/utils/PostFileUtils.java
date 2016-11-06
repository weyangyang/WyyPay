package xtcore.utils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/**
 * 
 * @author liyusheng
 * 
 * 
 * 	try {
		PostFileUtils postFile = new PostFileUtils(url);
		postFile.addFileParameter("file",zipFile);
		byte[] b = postFile.send();
		String result = new String(b);
		//{"success": true}
	 String succ =  SubstringUtils.substringBetween(result, "\"success\":", "}").replaceAll(" ", "").trim();
		//boolean isSucc = Boolean.getBoolean(succ);
		boolean isSucc = new Boolean(succ).booleanValue();
		if(isSucc){
			mUploadCallBackInterf.succ();
			//上传成功后删除zip文件和zip里的源文件
			FileMgr.deleteFile(zipFile);
			String cachePath =PreferenceUtils.getPrefString(ConfigBean.getInstance().getContext(), PreferenceUtils.SP_LOG_DIR_PATH, "");
			FileMgr.deleteFiles(cachePath);
		}else{
			mUploadCallBackInterf.failure(0, "error");
			FileMgr.deleteFile(zipFile);
		}
		
	} catch (Exception e) {
		mUploadCallBackInterf.failure(0, e.getMessage());
		FileMgr.deleteFile(zipFile);
}
 * 
 * 
 *
 */
public class PostFileUtils {
	URL url;
	HttpURLConnection conn;
	String boundary = "Boundary+6F05F7903D187C38";
	Map<String, String> textParams = new HashMap<String, String>();
	Map<String, File> fileparams = new HashMap<String, File>();
	DataOutputStream ds;

	public PostFileUtils(String url) throws Exception {
		this.url = new URL(url);
	}
    //重新设置要请求的服务器地址，即上传文件的地址。
	public void setUrl(String url) throws Exception {
		this.url = new URL(url);
	}
    //增加一个普通字符串数据到form表单数据中
	public void addTextParameter(String name, String value) {
		textParams.put(name, value);
	}
    //增加一个文件到form表单数据中
	public void addFileParameter(String name, File value) {
		fileparams.put(name, value);
	}
    // 清空所有已添加的form表单数据
	public void clearAllParameters() {
		textParams.clear();
		fileparams.clear();
	}
    // 发送数据到服务器，返回一个字节包含服务器的返回结果的数组
	public byte[] send() throws Exception {
		initConnection();
		try {
			
			conn.connect();
		} catch (SocketTimeoutException e) {
			// something
			throw new RuntimeException();
		}
		ds = new DataOutputStream(conn.getOutputStream());
		writeFileParams();
		writeStringParams();
		paramsEnd();
		InputStream in = conn.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int b;
		while ((b = in.read()) != -1) {
			out.write(b);
		}
		conn.disconnect();
		return out.toByteArray();
	}
    //文件上传的connection的一些必须设置
	private void initConnection() throws Exception {
		conn = (HttpURLConnection) this.url.openConnection();
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setConnectTimeout(10000); //连接超时为10秒
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
	}
    //普通字符串数据
	private void writeStringParams() throws Exception {
		Set<String> keySet = textParams.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			String value = textParams.get(name);
			ds.writeBytes("--" + boundary + "\r\n");
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name
					+ "\"\r\n");
			ds.writeBytes("\r\n");
			ds.writeBytes(encode(value) + "\r\n");
		}
	}
    //文件数据
	private void writeFileParams() throws Exception {
		Set<String> keySet = fileparams.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			File value = fileparams.get(name);
			ds.writeBytes("--" + boundary + "\r\n");
			ds.writeBytes("Content-Disposition: form-data; name=\"" + name
					+ "\"; filename=\"" + encode(value.getName()) + "\"\r\n");
			ds.writeBytes("Content-Type: "+ "application/octet-stream" + "\r\n");
			ds.writeBytes("\r\n");
			ds.write(getBytes(value));
			ds.writeBytes("\r\n");
		}
	}
    //把文件转换成字节数组
	private byte[] getBytes(File f) throws Exception {
		FileInputStream in = new FileInputStream(f);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int n;
		while ((n = in.read(b)) != -1) {
			out.write(b, 0, n);
		}
		in.close();
		return out.toByteArray();
	}
	//添加结尾数据
	private void paramsEnd() throws Exception {
		ds.writeBytes("--" + boundary + "--" + "\r\n");
		ds.writeBytes("\r\n");
	}
	// 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
    private String encode(String value) throws Exception{
    	//return URLEncoder.encode(value, "UTF-8");
    	return android.net.Uri.encode(value);
    }


}