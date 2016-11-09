package com.wyy.net.interf;

import netutils.http.HttpHeader;
import android.content.Context;

import com.wyy.net.abs.AbsFeedbackData;


/**
 * 意见反馈接口
 * 
 * @author liyusheng
 * 
 */
public interface FeedbackInterf {
	/**
	 * 意见反馈
	 * 
	 * @param content
	 *            User message
	 * @param contact
	 *            QQ or Email
	 * @param mAbsFeedbackData
	 *            数据回调接口
	 *            @param header 请求头参数
	 */
	public void feedback(HttpHeader header,String content, String contact, AbsFeedbackData mAbsFeedbackData);

	/**
	 * 意见反馈
	 * @param content
	 *            User message
	 * @param contact
	 *            QQ or Email
	 * @param log
	 *            System or extra log
	 * @param mAbsFeedbackData
	 * @param header 请求头参数
	 */
	public void feedback(HttpHeader header,String content, String contact, String log, AbsFeedbackData mAbsFeedbackData);

	/**
	 * 意见反馈
	 * 
	 * @param content
	 *            User message
	 * @param contact
	 *            QQ or Email
	 * @param mShowDialogInterf
	 *            自定义加载对话框接口
	 * @param mAbsFeedbackData
	 *            数据回调接口
	 *            @param header 请求头参数
	 */
	public void feedback(HttpHeader header,String content, String contact, ShowDialogInterf mShowDialogInterf,
			AbsFeedbackData mAbsFeedbackData);

	/**
	 * 意见反馈
	 * 
	 * @param content
	 *            User message
	 * @param contact
	 *            QQ or Email
	 * @param mContext
	 *            应用上下文对象
	 * @param isShowDialog
	 *            是否显示加载等待框，true为显示加载默认进度框，false为不显示
	 * @param mAbsFeedbackData
	 *            数据回调接口
	 *            @param header 请求头参数
	 */
	public void feedback(HttpHeader header,Context mContext, boolean isShowDialog, String content, String contact,
			AbsFeedbackData mAbsFeedbackData);
}
