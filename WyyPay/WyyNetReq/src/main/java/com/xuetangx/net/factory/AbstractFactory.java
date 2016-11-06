package com.xuetangx.net.factory;

import com.xuetangx.net.interf.FeedbackInterf;
import com.xuetangx.net.interf.GetUpgradeInterf;

public abstract class AbstractFactory {

	/**
	 * 意见反馈接口
	 * @return
	 */
	public abstract FeedbackInterf createFeedback();
	/**
	 * 获取app更新接口
	 * @return
	 */
	public abstract GetUpgradeInterf createGetUpgrade();

}
