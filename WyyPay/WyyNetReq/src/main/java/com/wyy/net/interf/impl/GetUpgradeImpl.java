package com.wyy.net.interf.impl;

import netutils.engine.NetConstants;
import netutils.engine.NetReqCallBack;
import netutils.http.HttpHeader;

import org.json.JSONException;

import android.content.Context;

import com.wyy.net.abs.AbsGetUpgradeData;
import com.wyy.net.engine.ParserEngine;
import com.wyy.net.engine.RequestEngine;
import com.wyy.net.exception.ParserException;
import com.wyy.net.interf.GetUpgradeInterf;
import com.wyy.net.interf.ShowDialogInterf;
import com.wyy.utils.CustomDialog;
import com.wyy.utils.XTAsyncTask;

public class GetUpgradeImpl implements GetUpgradeInterf {

	@Override
	public void getUpgrade(HttpHeader header, String strChannel,
			int versionCode, AbsGetUpgradeData mAbsGetUpgradeData) {
		new UpgradeEngine(header,  null, versionCode, strChannel , mAbsGetUpgradeData)
		.execute();
		
	}

	@Override
	public void getUpgrade(HttpHeader header, ShowDialogInterf mShowDialogInterf,
			String strChannel, int versionCode,
			AbsGetUpgradeData mAbsGetUpgradeData) {
		new UpgradeEngine(header, mShowDialogInterf, versionCode, strChannel , mAbsGetUpgradeData)
		.execute();
	}

	@Override
	public void getUpgrade(HttpHeader header, Context mContext,
			boolean isShowDialog, String strChannel, int versionCode,
			AbsGetUpgradeData mAbsGetUpgradeData) {
		new UpgradeEngine(header, mContext, isShowDialog, versionCode, strChannel , mAbsGetUpgradeData)
		.execute();
		
	}

	class UpgradeEngine extends XTAsyncTask {
		private Context mContext;
		private boolean isShowDialog;
		private CustomDialog mCustomDialog;
		private ShowDialogInterf mShowDialogInterf;
		private AbsGetUpgradeData mAbsGetUpgradeData;
		private HttpHeader header;
		private int strVersionCode;
		private String strChannel;

		private UpgradeEngine(HttpHeader header,
				ShowDialogInterf mShowDialogInterf, int strVersionCode, String strChannel,
				AbsGetUpgradeData mAbsGetUpgradeData) {
			this.mShowDialogInterf = mShowDialogInterf;
			this.mAbsGetUpgradeData = mAbsGetUpgradeData;
			this.strChannel = strChannel;
			this.strVersionCode = strVersionCode;
			this.header = header;
		}

		private UpgradeEngine(HttpHeader header, Context mContext,
				boolean isShowDialog, int strVersionCode, String strChannel, AbsGetUpgradeData mAbsGetUpgradeData) {
			this.mContext = mContext;
			this.isShowDialog = isShowDialog;
			this.mAbsGetUpgradeData = mAbsGetUpgradeData;
			this.strChannel = strChannel;
			this.strVersionCode = strVersionCode;
			this.header = header;

		}

		@Override
		protected void doInbackgroud() {
			RequestEngine.getInstance().getUpgradeData(header, strChannel, strVersionCode,
					new NetReqCallBack() {

						@Override
						public void getErrData(int statusCode, String strJson,
								String strUrl) {
							try {
								ParserEngine.getInstance().parserErrData(
										strJson, strUrl, mAbsGetUpgradeData);
							} catch (ParserException e) {
								mAbsGetUpgradeData.getParserErrData(
										NetConstants.PARSE_EXCEPTION,
										e.getMessage(), strUrl);
							} catch (JSONException e) {
								mAbsGetUpgradeData.getParserErrData(
										NetConstants.JSON_EXCEPTION,
										e.getMessage(), strUrl);
							}
							super.getErrData(statusCode, strJson, strUrl);
						}

						@Override
						public void getSuccData(int statusCode, String strJson,
								String strUrl) {
							try {
								ParserEngine.getInstance().parserUpgradeData(
										strJson, mAbsGetUpgradeData, strUrl);
							} catch (ParserException e) {
								mAbsGetUpgradeData.getParserErrData(
										NetConstants.PARSE_EXCEPTION,
										e.getMessage(), strUrl);
							} catch (JSONException e) {
								mAbsGetUpgradeData.getParserErrData(
										NetConstants.JSON_EXCEPTION,
										e.getMessage(), strUrl);
							}
						}
					});
		}

		@Override
		protected void onPreExectue() {
			if (mShowDialogInterf != null) {
				mShowDialogInterf.show();
				return;
			}
			if (isShowDialog && mContext != null) {
				mCustomDialog = CustomDialog.createLoadingDialog(mContext,
						null, true);
			}
		}

		@Override
		protected void onPostExecute() {
			if (mShowDialogInterf != null) {
				mShowDialogInterf.dismiss();
				return;
			}
			if (isShowDialog && mContext != null) {
				mCustomDialog.dismiss();
			}
		}

	}
}
