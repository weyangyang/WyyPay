package com.xuetangx.net.interf.impl;

import netutils.engine.NetConstants;
import netutils.engine.NetReqCallBack;
import netutils.http.HttpHeader;

import org.json.JSONException;

import android.content.Context;

import com.xuetangx.net.abs.AbsFeedbackData;
import com.xuetangx.net.engine.ParserEngine;
import com.xuetangx.net.engine.RequestEngine;
import com.xuetangx.net.exception.ParserException;
import com.xuetangx.net.interf.FeedbackInterf;
import com.xuetangx.utils.CustomDialog;
import com.xuetangx.utils.XTAsyncTask;

public class FeedbackImpl implements FeedbackInterf {

	@Override
	public void feedback(HttpHeader header,String content, String contact, AbsFeedbackData mAbsFeedbackData) {
		new FeedBackEngine(header,null, content, contact, "", mAbsFeedbackData).execute();
	}

	@Override
	public void feedback(HttpHeader header,String content, String contact, String log, AbsFeedbackData mAbsFeedbackData) {
		new FeedBackEngine(header,null, content, contact, log, mAbsFeedbackData).execute();
	}

	@Override
	public void feedback(HttpHeader header,String content, String contact, ShowDialogInterf mShowDialogInterf,
			AbsFeedbackData mAbsFeedbackData) {
		new FeedBackEngine(header,mShowDialogInterf, content, contact, "", mAbsFeedbackData).execute();
	}

	@Override
	public void feedback(HttpHeader header,Context mContext, boolean isShowDialog, String content, String contact,
			AbsFeedbackData mAbsFeedbackData) {
		new FeedBackEngine(header,mContext, isShowDialog, content, contact, "", mAbsFeedbackData).execute();
		
	}
class FeedBackEngine extends XTAsyncTask{
	private Context mContext;
	private boolean isShowDialog;
	private CustomDialog mCustomDialog;
	private ShowDialogInterf mShowDialogInterf;
	private String content;
	private String contact;
	private String log;
	private AbsFeedbackData mAbsFeedbackData;
	private HttpHeader header;
	public FeedBackEngine(HttpHeader header,ShowDialogInterf mShowDialogInterf, String content, String contact,String log,
			AbsFeedbackData mAbsFeedbackData){
		this.mShowDialogInterf = mShowDialogInterf;
		this.contact = contact;
		this.content = content;
		this.log = log;
		this.mAbsFeedbackData = mAbsFeedbackData;
		this.header = header;
	}
	public FeedBackEngine(HttpHeader header,Context mContext, boolean isShowDialog, String content, String contact,String log,
			AbsFeedbackData mAbsFeedbackData){
		this.mContext = mContext;
		this.isShowDialog = isShowDialog;
		this.contact = contact;
		this.content = content;
		this.log = log;
		this.mAbsFeedbackData = mAbsFeedbackData;
		this.header = header;
		
	}
	
	@Override
	protected void doInbackgroud() {
		RequestEngine.getInstance().postFeedback(header,content, contact, log, new NetReqCallBack() {
			
			@Override
			public void getErrData(int statusCode, String strJson,String strUrl) {
				try {
					ParserEngine.getInstance().parserErrData(strJson,strUrl, mAbsFeedbackData);
				} catch (ParserException e) {
					mAbsFeedbackData.getParserErrData(NetConstants.PARSE_EXCEPTION, e.getMessage(),strUrl);
				} catch (JSONException e) {
					mAbsFeedbackData.getParserErrData(NetConstants.JSON_EXCEPTION, e.getMessage(),strUrl);
				}
				super.getErrData(statusCode, strJson,strUrl);
			}

			@Override
			public void getSuccData(int statusCode, String strJson,String strUrl) {
				try {
					ParserEngine.getInstance().parserFeedbackData(strJson, mAbsFeedbackData,strUrl);
				} catch (ParserException e) {
					mAbsFeedbackData.getParserErrData(NetConstants.PARSE_EXCEPTION, e.getMessage(),strUrl);
				} catch (JSONException e) {
					mAbsFeedbackData.getParserErrData(NetConstants.JSON_EXCEPTION, e.getMessage(),strUrl);
				}
			}
		});
	}
	@Override
	protected void onPreExectue() {
		if(mShowDialogInterf!=null){
			mShowDialogInterf.show();
			return;
		}
		if (isShowDialog&&mContext!=null) {
			mCustomDialog = CustomDialog.createLoadingDialog(mContext, null, true);
		}
	}


	@Override
	protected void onPostExecute() {
		if(mShowDialogInterf!=null){
			mShowDialogInterf.dismiss();
			return;
		}
		if (isShowDialog&&mContext!=null) {
			mCustomDialog.dismiss();
		}
	}
	
}

}
