package com.xuetangx.net.interf;

import netutils.http.HttpHeader;
import android.content.Context;

import com.xuetangx.net.abs.AbsGetUpgradeData;

/**
 * 获取app更新接口
 * 
 * @author liyusheng
 * 
 */
public interface GetUpgradeInterf {
	/**
	 * 获取app更新
	 * 
	 * @param mAbsGetUpgradeData
	 *            数据回调接口
	 *            @param header 请求头参数
	 */
	public void getUpgrade(HttpHeader header, String strChannel, int versionCode, AbsGetUpgradeData mAbsGetUpgradeData);

	/**
	 * 获取app更新
	 * 
	 * @param mShowDialogInterf
	 *            自定义加载对话框接口
	 * @param mAbsGetUpgradeData
	 *            数据回调接口
	 *            @param header 请求头参数
	 */
	public void getUpgrade(HttpHeader header,ShowDialogInterf mShowDialogInterf, String strChannel, int versionCode, AbsGetUpgradeData mAbsGetUpgradeData);

	/**
	 * 获取app更新
	 * 
	 * @param mContext
	 *            应用上下文对象
	 * @param isShowDialog
	 *            是否显示加载等待框，true为显示加载默认进度框，false为不显示
	 * @param mAbsGetUpgradeData
	 *            数据回调接口
	 *            @param header 请求头参数
	 */
	public void getUpgrade(HttpHeader header,Context mContext, boolean isShowDialog, String strChannel, int versionCode, AbsGetUpgradeData mAbsGetUpgradeData);
}
