package com.wyy.pay.bean;

import java.io.Serializable;

/**
 * Created by liyusheng on 16/11/12.
 */

public class ProCategoryBean implements Serializable{
    private String categoryId;//分类id (由分类名称的6位md5生成)
    private String categoryName;//分类名称

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
        return "ProCategoryBean{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
