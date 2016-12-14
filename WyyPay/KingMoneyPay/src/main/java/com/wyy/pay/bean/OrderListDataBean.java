package com.wyy.pay.bean;

import java.io.Serializable;

/**
 * Created by liyusheng on 16/12/13.
 */

public class OrderListDataBean implements Serializable{
    private double money;//流水金额
    private String createDate;//创建的日期 16-12-10
    private String createTime;//创建的时间 时分秒
    private int payType;//支付类型 1微信  2支付宝  3现金
    private String orderNo;//订单编号
    private boolean isTitle = false;

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitleStatus(boolean isTitle) {
        this.isTitle = isTitle;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
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

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "OrderListDataBean{" +
                "money=" + money +
                ", createDate='" + createDate + '\'' +
                ", createTime='" + createTime + '\'' +
                ", payType=" + payType +
                ", orderNo='" + orderNo + '\'' +
                ", isTitle=" + isTitle +
                '}';
    }
}
