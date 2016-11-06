package netutils.http;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/**
 * <p>
 * 使用方法:
 * <p>
 * 
 * <pre>
 * RequestParams params = new RequestParams();
 * params.put(&quot;username&quot;, &quot;sheng&quot;);
 * params.put(&quot;password&quot;, &quot;123456&quot;);
 * params.put(&quot;email&quot;, &quot;test@qq.com&quot;);
 * SgeHttp fh = new SgeHttp();
 * fh.post(&quot;http://www.baidu.com&quot;, params, new RequestCallBack&lt;String&gt;() {
 * 	&#064;Override
 * 	public void onLoading(long count, long current) {
 * 		textView.setText(current + &quot;/&quot; + count);
 * 	}
 * 
 * 	&#064;Override
 * 	public void onSuccess(String t) {
 * 		textView.setText(t == null ? &quot;null&quot; : t);
 * 	}
 * });
 * </pre>
 */
public class RequestParams {
	private static String ENCODING = "UTF-8";

	protected ConcurrentHashMap<String, String> urlParams;

	public RequestParams() {
		init();
	}
	public ConcurrentHashMap<String, String> getParamsMap(){
		return urlParams;
	}
	public RequestParams(Map<String, String> source) {
		init();

		for (Map.Entry<String, String> entry : source.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public RequestParams(String key, String value) {
		init();
		put(key, value);
	}

	public void put(String key, String value) {
		if (key != null && value != null) {
			urlParams.put(key, value);
		}
	}

	private void init() {
		urlParams = new ConcurrentHashMap<String, String>();
	}

	@SuppressWarnings("deprecation")
	public List<NameValuePair> getParamsList() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		return nameValuePairs;
	}

	@SuppressWarnings("deprecation")
	protected List<BasicNameValuePair> geBasicParamsList() {
		List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

		for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
			lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		return lparams;
	}

	@SuppressWarnings("deprecation")
	public String getParamString() {
		return URLEncodedUtils.format(geBasicParamsList(), ENCODING);
	}

}