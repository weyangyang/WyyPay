package com.wyy.pay.bean;

import java.io.Serializable;

import db.utils.BaseDbBean;
import db.utils.ColumnAnnotation;

/**
 * {
     UserName:”username”,
     StoreName:”店铺名称”,
     StoreLogo:”http://xxx/店铺Logo.jpg”,
     WyyCode:”本次登录凭证”,
     SubId:0,  非0 为子账户登录
    IsFree:1   1 = APP注册用户未开通Web端 , 0 = 全网通用
 }

 *
 * Created by liyusheng on 16/12/11.
 */

public class TableUserBean extends BaseDbBean implements Serializable{
    public static final String TABLE_NAME = "kmp_user";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_STORE_NAME = "store_name";
    public static final String COLUMN_WYY_CODE = "wyy_code";
    public static final String COLUMN_SUB_ID = "sub_id";
    public static final String COLUMN_IS_FREE = "is_free";
    public static final String COLUMN_STORE_LOGO = "store_logo";



    @ColumnAnnotation(column = COLUMN_USER_NAME)
    public String userName;
    @ColumnAnnotation(column = COLUMN_STORE_NAME)
    public String storeName;
    @ColumnAnnotation(column = COLUMN_USER_ID)
    public String userId;
    @ColumnAnnotation(column = COLUMN_WYY_CODE)
    public String wyyCode;
    @ColumnAnnotation(column = COLUMN_SUB_ID,info = "int",defaultValue = "-1")
    public int subId;
    @ColumnAnnotation(column = COLUMN_IS_FREE,info = "int",defaultValue = "-1")
    public int isFree;
    @ColumnAnnotation(column = COLUMN_STORE_LOGO)
    public String storeLogo;

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWyyCode() {
        return wyyCode;
    }

    public void setWyyCode(String wyyCode) {
        this.wyyCode = wyyCode;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    @Override
    public String toString() {
        return "TableUserBean{" +
                "userName='" + userName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", userId='" + userId + '\'' +
                ", wyyCode='" + wyyCode + '\'' +
                ", subId=" + subId +
                ", isFree=" + isFree +
                ", storeLogo='" + storeLogo + '\'' +
                '}';
    }
}
