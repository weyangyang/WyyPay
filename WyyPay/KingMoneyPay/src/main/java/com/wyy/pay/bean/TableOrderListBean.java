package com.wyy.pay.bean;

import java.io.Serializable;

import db.utils.BaseDbBean;
import db.utils.ColumnAnnotation;

/**
 * Created by liyusheng on 16/12/13.
 */

public class TableOrderListBean extends BaseDbBean implements Serializable{
    public static final String TABLE_NAME = "kmp_order_list";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CREATE_TIEM = "create_time";
    public static final String COLUMN_CREATE_DATE = "create_date";
    public static final String COLUMN_MONEY = "money";
    public static final String COLUMN_PAY_TYPE = "pay_type";
    public static final String COLUMN_ORDER_NO = "order_no";

    @ColumnAnnotation(column = COLUMN_MONEY,info = "double",defaultValue = "-1")
    public double money;//流水金额
    @ColumnAnnotation(column = COLUMN_CREATE_DATE)
    public String createDate;//创建的日期 16-12-12
    @ColumnAnnotation(column = COLUMN_CREATE_TIEM)
    public String createTime;//创建的时间  18:32:12
    @ColumnAnnotation(column = COLUMN_PAY_TYPE)
    public int payType;//支付类型 1微信  2支付宝  3现金
    @ColumnAnnotation(column = COLUMN_USER_ID)
    public String userId;//用户id
    @ColumnAnnotation(column = COLUMN_ORDER_NO)
    public String orderNo;//订单编号

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }



    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "TableOrderListBean{" +
                "money=" + money +
                ", createDate='" + createDate + '\'' +
                ", createTime='" + createTime + '\'' +
                ", payType=" + payType +
                ", userId='" + userId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }
}
