package com.wyy.pay.bean;

import java.io.Serializable;

/**
 * Created by liyusheng on 16/11/13.
 */

public class OrderCategoryBean implements Serializable{
    private String categoryId;//分类id (由分类名称的6位md5生成)
    private String categoryName;//分类名称
    private String proSumCount;//先择的数量

    public String getProSumCount() {
        return proSumCount;
    }

    public void setProSumCount(String proSumCount) {
        this.proSumCount = proSumCount;
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

    @Override
    public String toString() {
        return "OrderCategoryBean{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", proSumCount='" + proSumCount + '\'' +
                '}';
    }
}
