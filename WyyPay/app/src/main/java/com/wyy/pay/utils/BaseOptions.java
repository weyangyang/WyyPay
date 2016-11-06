package com.wyy.pay.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.wyy.pay.R;

public class BaseOptions {
    private static BaseOptions instance;
    private DisplayImageOptions avaterOption;
    private int strokeColor = 0x26000000;
    private float strokeWidth = 3.0f;

    private BaseOptions() {

        avaterOption = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_user)
                .showImageForEmptyUri(R.drawable.ic_default_user)
                .showImageOnFail(R.drawable.ic_default_user)
                .cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true).build();
        int round = 4;
        int strokeWidth = 3;

    }


    public DisplayImageOptions getAvaterOptions() {
        return avaterOption;
    }


    public static BaseOptions getInstance() {
        if (instance == null) {
            instance = new BaseOptions();
        }
        return instance;
    }

}
