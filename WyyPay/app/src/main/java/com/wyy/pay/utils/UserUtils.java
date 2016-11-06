package com.wyy.pay.utils;

import config.bean.ConfigBean;
import netutils.http.HttpHeader;

/**
 * Created by liyusheng on 16/11/6.
 */

public class UserUtils {
    public static HttpHeader getDefaultHttpHeader() {
        HttpHeader header = new HttpHeader();
        header.addHeader("uid", ConfigBean.getInstance().getStrUID());
        header.addHeader("uuid", ConfigBean.getInstance().getStrUUID());
        return header;
    }
    public static HttpHeader getEmptyHttpHeader() {
        HttpHeader header = new HttpHeader();
        return header;
    }
}
