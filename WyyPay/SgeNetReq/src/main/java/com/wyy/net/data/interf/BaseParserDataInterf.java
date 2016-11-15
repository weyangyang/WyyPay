package com.wyy.net.data.interf;
/**
 * 网络请求异常数据回调接口
 * @author liyusheng
 *
 */
public interface BaseParserDataInterf {
    /**
     * 获取网络异常
     * @param errCode
     * @param errMsg
     */
    public void getErrData(int statusCode, String errMsg,String strUrl);
    /**
     * 获取解析异常数据
     * @param errCode
     * @param errMsg
     */
    public void getParserErrData(int errCode, String errMsg,String strUrl);
    /**
     * 获取各种异常的数据
     * 
     * @param errCode
     *            错误编号
     * @param errMsg
     *            错误信息
     *   
     */
    
    public void getExceptionData(int errCode, String errMsg,String strUrl);
}
