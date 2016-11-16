package com.wyy.pay.bean;

import java.io.Serializable;

import db.utils.BaseDbBean;
import db.utils.ColumnAnnotation;

/**
 * Created by liyusheng on 16/11/12.
 */

public class TableCategoryBean extends BaseDbBean implements Serializable{
    public static final String TABLE_NAME = "kmp_category";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    public static final String COLUMN_CREATE_CATEGORY_TIEM = "create_category_time";



    @ColumnAnnotation(column = COLUMN_CATEGORY_ID)
    public String categoryId;//分类id (由分类名称的6位md5生成)
    @ColumnAnnotation(column = COLUMN_CREATE_CATEGORY_TIEM,info ="Long",defaultValue = "-1")
    public Long createTime;//创建分类的时间戳
    @ColumnAnnotation(column = COLUMN_CATEGORY_NAME)
    public String categoryName;//分类名称
    @ColumnAnnotation(column = COLUMN_USER_ID)
    public String userId;//用户id

    private int proSumCount;//先择的数量

    public int getProSumCount() {
        return proSumCount;
    }

    public void setProSumCount(int proSumCount) {
        this.proSumCount = proSumCount;
    }

    public Long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TableCategoryBean{" +
                "categoryId='" + categoryId + '\'' +
                ", createTime=" + createTime +
                ", categoryName='" + categoryName + '\'' +
                ", userId='" + userId + '\'' +
                ", proSumCount=" + proSumCount +
                '}';
    }
}
