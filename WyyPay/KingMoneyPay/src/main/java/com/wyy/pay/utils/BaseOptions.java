package com.wyy.pay.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wyy.pay.R;

public class BaseOptions {
    private static BaseOptions instance;
    private DisplayImageOptions avaterOption;
    private DisplayImageOptions productImgOption;
    private DisplayImageOptions productClipImgOption;


    private BaseOptions() {

        avaterOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_user)
                .showImageForEmptyUri(R.drawable.ic_default_user)
                .showImageOnFail(R.drawable.ic_default_user)
                .cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
                .delayBeforeLoading(300)
                .cacheOnDisk(true).build();
        productImgOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_empty)
                .showImageForEmptyUri(R.drawable.image_empty)
                .showImageOnFail(R.drawable.image_error)
                .delayBeforeLoading(300)
                .cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true).build();
        productClipImgOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.image_empty)
                .showImageForEmptyUri(R.drawable.image_empty)
                .showImageOnFail(R.drawable.image_error)
                .delayBeforeLoading(300).bitmapConfig(Bitmap.Config.RGB_565)
               // .displayer(new RoundedBitmapDisplayer(30))
                .cacheInMemory(false).cacheOnDisk(false).build();

    }


    public DisplayImageOptions getAvaterOptions() {
        return avaterOption;
    }
    public DisplayImageOptions getProductImgOptions() {
        return productImgOption;
    }
    public DisplayImageOptions getProductClipImgOptions() {
        return productClipImgOption;
    }


    public static BaseOptions getInstance() {
        if (instance == null) {
            instance = new BaseOptions();
        }
        return instance;
    }

}
