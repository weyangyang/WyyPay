package com.wyy.pay.bean;

import java.io.Serializable;

import db.utils.BaseDbBean;
import db.utils.ColumnAnnotation;

/**
 * Created by liyusheng on 16/12/9.
 */

public class TableDiscountNumBean extends BaseDbBean implements Serializable{
    public static final String TABLE_NAME = "kmp_discount_num";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DISCOUNT_ID = "discount_id";
    public static final String COLUMN_DISCOUNT_NUM = "discount_num";
    public static final String COLUMN_CREATE_CATEGORY_TIEM = "create_discount_time";
    public static final String COLUMN_DISCOUNT_SHOW_TEXT = "discount_show_text";

    @ColumnAnnotation(column = COLUMN_CREATE_CATEGORY_TIEM,info ="long",defaultValue = "-1")
    public long createTime;//创建的时间戳
    @ColumnAnnotation(column = COLUMN_DISCOUNT_ID)
    public String discountId;//折扣id (由优惠字符的6位md5生成) 如6.5折 ，减10元
    @ColumnAnnotation(column = COLUMN_DISCOUNT_SHOW_TEXT)
    public String showText;//如6.5折 ，减10元
    @ColumnAnnotation(column = COLUMN_DISCOUNT_NUM,info ="float",defaultValue = "-1")
    public float discountNum;//折扣或减少   6.5
    @ColumnAnnotation(column = COLUMN_TYPE,info="int",defaultValue = "-1")
    public int type;//type =1 整单折扣， type=2整单减少多少钱
    @ColumnAnnotation(column = COLUMN_USER_ID)
    public String userId;//用户id

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public float getDiscountNum() {
        return discountNum;
    }

    public void setDiscountNum(float discountNum) {
        this.discountNum = discountNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    @Override
    public String toString() {
        return "TableDiscountNumBean{" +
                "createTime=" + createTime +
                ", discountId='" + discountId + '\'' +
                ", showText='" + showText + '\'' +
                ", discountNum=" + discountNum +
                ", type=" + type +
                ", userId='" + userId + '\'' +
                '}';
    }
}
