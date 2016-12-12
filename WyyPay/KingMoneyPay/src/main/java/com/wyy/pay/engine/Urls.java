package com.wyy.pay.engine;

/**
 * 所有网络请求url
 * 
 * @author liyusheng
 */
public class Urls {
	public static  String ROOT_URL = "https://fxssl.weyangyang.com";
	public static final String BASE_URL = ROOT_URL + "/storepayapi";
	/**
	 * 意见反馈url
	 */
	public static final String FEEDBACK_URL = "/v2/feedback";
	/**
	 * 登录url
	 */
	public static final String LOGIN_URL = "/login";
	/**
	 * 忘记密码url
	 */
	public static final String FORGET_PASSWORD_URL = "/resetpwd";
	/**
	 * 获取优惠券url
	 */
	public static final String GET_DISCOUNT_URL = "/getusercoupon";
	/**
	 * 注册url
	 */
	public static final String REGISTER_URL = "/register";
	/**
	 * 短信验证码url
	 */
	public static final String SMS_VERIFY_URL = "/sendsmscode";
	/**
	 * app版本更新url
	 */
	public static final String UPGRADE_URL = "/v2/upgrade/Android";
	
	
}
