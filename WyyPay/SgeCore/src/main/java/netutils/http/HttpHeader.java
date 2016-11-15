package netutils.http;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.client.methods.HttpRequestBase;

public class HttpHeader implements Serializable {
	private static final long serialVersionUID = 9130139641559708531L;
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";
	protected ConcurrentHashMap<String, String> clientHeaderMap;

	public HttpHeader() {
		init();
	}

	public HttpHeader(String key, String value) {
		init();
		put(key, value);
	}

	public void put(String key, String value) {
		if (key != null && value != null) {
			clientHeaderMap.put(key, value);
		}
	}

	private void init() {
		clientHeaderMap = new ConcurrentHashMap<String, String>();
	}

	public void addHeader(String header, String value) {
		clientHeaderMap.put(header, value);
	}

	public void setHeader(HttpRequestBase request) {
		if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
			request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
		}
		if(clientHeaderMap.size()==0){
			return;
		}
		for (String header : clientHeaderMap.keySet()) {
			request.addHeader(header, clientHeaderMap.get(header));
		}
	}
	public ConcurrentHashMap<String, String> getHeaderMap (){
		return clientHeaderMap;
	}

}
