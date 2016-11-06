package com.xuetangx.net.data.interf;

import com.xuetangx.net.bean.GetUpgradeDataBean;


/**
 * 获取app更新数据回调接口
 * 
 * @author liyusheng
 * 
 */
public interface GetUpgradeDataInterf extends BaseParserDataInterf {
	/**
	 * 获取app更新成功数据
	 * @param mGetUpgradeDataBean
	 */
	public void getSuccData(GetUpgradeDataBean mGetUpgradeDataBean,String strUrl);
}
