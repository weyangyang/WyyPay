package com.wyy.net.engine;

import com.wyy.net.abs.AbsFeedbackData;
import com.wyy.net.abs.AbsGetUpgradeData;
import com.wyy.net.bean.GetUpgradeDataBean;
import com.wyy.net.data.interf.BaseParserDataInterf;
import com.wyy.net.exception.ParserException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ParserEngine implements Serializable {
	private static final long serialVersionUID = 1L;
	private static ParserEngine mParserEngine;

	private ParserEngine() {

	}

	public synchronized static ParserEngine getInstance() {
		if (null == mParserEngine) {
			mParserEngine = new ParserEngine();
		}
		return mParserEngine;

	}


	/**
	 * 解析意见反馈成功后返回的数据
	 * 
	 * @param strJson
	 * @param parserDataCallBack
	 * @throws JSONException
	 * @throws ParserException
	 */
	public void parserFeedbackData(String strJson, AbsFeedbackData parserDataCallBack, String strUrl)
			throws JSONException, ParserException {
		// 目前请求成功后，没有返回数据
		parserDataCallBack.getSuccData(strUrl);
	}

	/**
	 * 解析app版本更新数据
	 *
	 * @param strJson
	 * @param parserDataCallBack
	 * @throws JSONException
	 * @throws ParserException
	 */
	public void parserUpgradeData(String strJson, AbsGetUpgradeData parserDataCallBack, String url)
			throws JSONException, ParserException {
		GetUpgradeDataBean mGetUpgradeDataBean = new GetUpgradeDataBean();
		JSONObject mJSONObject = new JSONObject(strJson);
		String strVersion = mJSONObject.optString("version");
		mGetUpgradeDataBean.setStrVersion(strVersion);
		String strDescription = mJSONObject.optString("description");
		mGetUpgradeDataBean.setStrDescription(strDescription);
		long apkSize = mJSONObject.optLong("size");
		mGetUpgradeDataBean.setApkSize(apkSize);
		String apkMD5 = mJSONObject.optString("apk_signature");
		mGetUpgradeDataBean.setApkMD5(apkMD5);
		String strUrl = mJSONObject.optString("url");
		mGetUpgradeDataBean.setStrUrl(strUrl);
		String channel = mJSONObject.optString("channel");
		mGetUpgradeDataBean.setStrChannel(channel);
		mGetUpgradeDataBean.setIntVersionCode(mJSONObject.optInt("app_version_code"));
		String strReleaseDate = mJSONObject.optString("release_date");
		mGetUpgradeDataBean.setStrReleaseDate(strReleaseDate);
		JSONObject force = mJSONObject.optJSONObject("force_upgrade");
		if (force != null) {
			mGetUpgradeDataBean.setForceUpgrade(force.optBoolean("flag"));
			mGetUpgradeDataBean.setStrForceUpgrade(force.optString("desc"));
		}
		parserDataCallBack.getSuccData(mGetUpgradeDataBean, url);
	}

	public void parserErrData(String strJson, String strUrl, BaseParserDataInterf mBaseParserDataInterf) throws JSONException, ParserException {
		JSONObject mJSONObject = new JSONObject(strJson);
		int errCode = mJSONObject.optInt("error_code");
		String errMsg = mJSONObject.optString("error");
		mBaseParserDataInterf.getErrData(errCode, errMsg, strUrl);
	}
}
