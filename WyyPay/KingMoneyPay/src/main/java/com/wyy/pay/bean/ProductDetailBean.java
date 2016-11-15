package com.wyy.pay.bean;

import java.io.File;
import java.io.Serializable;

/**
 * Created by liyusheng on 16/11/14.
 */

public class ProductDetailBean implements Serializable{
    private String proBarCode;//商品条码
    private String proName;//商品名称
    private String proCategoryId;//商品分类id
    private String proCategoryName;//商品分类名称
    private String proPrice;//商品价格
    private File proImgFile;//商品图片file

    public String getProBarCode() {
        return proBarCode;
    }

    public void setProBarCode(String proBarCode) {
        this.proBarCode = proBarCode;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProCategoryId() {
        return proCategoryId;
    }

    public void setProCategoryId(String proCategoryId) {
        this.proCategoryId = proCategoryId;
    }

    public String getProCategoryName() {
        return proCategoryName;
    }

    public void setProCategoryName(String proCategoryName) {
        this.proCategoryName = proCategoryName;
    }

    public String getProPrice() {
        return proPrice;
    }

    public void setProPrice(String proPrice) {
        this.proPrice = proPrice;
    }

    public File getProImgFile() {
        return proImgFile;
    }

    public void setProImgFile(File proImgFile) {
        this.proImgFile = proImgFile;
    }

    @Override
    public String toString() {
        return "ProductDetailBean{" +
                "proBarCode='" + proBarCode + '\'' +
                ", proName='" + proName + '\'' +
                ", proCategoryId='" + proCategoryId + '\'' +
                ", proCategoryName='" + proCategoryName + '\'' +
                ", proPrice='" + proPrice + '\'' +
                ", proImgFile=" + proImgFile +
                '}';
    }
}
