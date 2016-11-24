package com.wyy.pay.bean;

import java.io.File;
import java.io.Serializable;

import db.utils.BaseDbBean;
import db.utils.ColumnAnnotation;

/**
 * Created by liyusheng on 16/11/13.
 */

public class TableGoodsDetailBean extends BaseDbBean implements Serializable{
    public static final String TABLE_NAME = "kmp_product_detail";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_GOODS_ID = "goods_id";
    public static final String COLUMN_GOODS_IMG_URL = "goods_img_url";
    public static final String COLUMN_GOODS_NAME = "goods_name";
    public static final String COLUMN_GOODS_PRICE = "goods_price";
    public static final String COLUMN_GOODS_BARCODE = "goods_barcode";
    public static final String COLUMN_GOODS_CATEGORY_ID = "goods_category_id";
    public static final String COLUMN_GOODS_STOCK_COUNT = "goods_stock_count";
    public static final String COLUMN_GOODS_CATEGORY_NAME = "goods_category_name";
    public static final String COLUMN_GOODS_CREATE_TIME = "goods_create_time";
    public static final String COLUMN_GOODS_ADD_COUNT = "column_goods_add_count";

    @ColumnAnnotation(column = COLUMN_GOODS_ID)
    public String goodsId;//商品id
    @ColumnAnnotation(column = COLUMN_GOODS_IMG_URL)
    public String goodsImgUrl;//商品图片
    @ColumnAnnotation(column = COLUMN_GOODS_NAME)
    public String goodsName;//商品名称
    @ColumnAnnotation(column = COLUMN_GOODS_PRICE,info = "double",defaultValue = "-1")
    public double goodsPrice;//商品价格
    @ColumnAnnotation(column = COLUMN_GOODS_BARCODE)
    public String goodsBarcode;//商品编号
    @ColumnAnnotation(column = COLUMN_GOODS_CATEGORY_ID)
    public String goodsCid;//产品所属分类id
    @ColumnAnnotation(column = COLUMN_GOODS_STOCK_COUNT,info = "int",defaultValue = "-1")
    public int goodsStockCount;//商品库存
    @ColumnAnnotation(column = COLUMN_GOODS_CATEGORY_NAME)
    public String goodsCName;//产品所属的分类名称
    @ColumnAnnotation(column = COLUMN_USER_ID)
    public  String userId;//用户id
    @ColumnAnnotation(column = COLUMN_GOODS_CREATE_TIME,info = "long",defaultValue = "-1")
    public long goodsCreateTime;//商品创建的时间
    @ColumnAnnotation(column = COLUMN_GOODS_ADD_COUNT,info="int",defaultValue = "-1")
    public int addGoodsCount;//添加的商品数量

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsImgUrl() {
        return goodsImgUrl;
    }

    public void setGoodsImgUrl(String goodsImgUrl) {
        this.goodsImgUrl = goodsImgUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsBarcode() {
        return goodsBarcode;
    }

    public void setGoodsBarcode(String goodsBarcode) {
        this.goodsBarcode = goodsBarcode;
    }

    public String getGoodsCid() {
        return goodsCid;
    }

    public void setGoodsCid(String goodsCid) {
        this.goodsCid = goodsCid;
    }

    public int getGoodsStockCount() {
        return goodsStockCount;
    }

    public void setGoodsStockCount(int goodsStockCount) {
        this.goodsStockCount = goodsStockCount;
    }

    public String getGoodsCName() {
        return goodsCName;
    }

    public void setGoodsCName(String goodsCName) {
        this.goodsCName = goodsCName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getGoodsCreateTime() {
        return goodsCreateTime;
    }

    public void setGoodsCreateTime(Long goodsCreateTime) {
        this.goodsCreateTime = goodsCreateTime;
    }

    public int getAddGoodsCount() {
        return addGoodsCount;
    }

    public void setAddGoodsCount(int addGoodsCount) {
        this.addGoodsCount = addGoodsCount;
    }

    @Override
    public String toString() {
        return "TableGoodsDetailBean{" +
                "goodsId='" + goodsId + '\'' +
                ", goodsImgUrl='" + goodsImgUrl + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsBarcode='" + goodsBarcode + '\'' +
                ", goodsCid='" + goodsCid + '\'' +
                ", goodsStockCount=" + goodsStockCount +
                ", goodsCName='" + goodsCName + '\'' +
                ", userId='" + userId + '\'' +
                ", goodsCreateTime=" + goodsCreateTime +
                ", addGoodsCount=" + addGoodsCount +
                '}';
    }
}
