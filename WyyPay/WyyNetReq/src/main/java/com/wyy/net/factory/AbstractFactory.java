package com.wyy.net.factory;


import com.wyy.net.interf.FeedbackInterf;
import com.wyy.net.interf.GetUpgradeInterf;

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
