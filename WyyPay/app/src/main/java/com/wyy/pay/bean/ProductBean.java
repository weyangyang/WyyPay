package com.wyy.pay.bean;

import java.io.Serializable;

/**
 * Created by liyusheng on 16/11/13.
 */

public class ProductBean implements Serializable{
    private String imgUrl;//商品图片
    private String proName;//商品名称
    private double proPrice;//商品价格
    private String proNo;//商品编号
    private String categoryId;//产品所属分类id
    private int addProCount;//添加的商品数量

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public double getProPrice() {
        return proPrice;
    }

    public void setProPrice(double proPrice) {
        this.proPrice = proPrice;
    }

    public String getProNo() {
        return proNo;
    }

    public void setProNo(String proNo) {
        this.proNo = proNo;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getAddProCount() {
        return addProCount;
    }

    public void setAddProCount(int addProCount) {
        this.addProCount = addProCount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "ProductBean{" +
                "imgUrl='" + imgUrl + '\'' +
                ", proName='" + proName + '\'' +
                ", proPrice=" + proPrice +
                ", proNo='" + proNo + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", addProCount=" + addProCount +
                '}';
    }
}
