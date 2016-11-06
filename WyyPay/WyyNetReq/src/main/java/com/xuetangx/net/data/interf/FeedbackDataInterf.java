package com.xuetangx.net.data.interf;


/**
 * 意见反馈数据回调接口
 * 
 * @author liyusheng
 * 
 */
public interface FeedbackDataInterf extends BaseParserDataInterf {
	/**
	 * 意见反馈成功数据
	 */
	public void getSuccData(String strUrl);
}
