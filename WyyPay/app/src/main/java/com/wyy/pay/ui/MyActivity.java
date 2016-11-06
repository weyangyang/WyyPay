package com.wyy.pay.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.pay.R;
import com.wyy.pay.view.WyyCircleImageView;


public class MyActivity extends Activity {
    private WyyCircleImageView ivAvater;//头像
    ImageLoader mImageLoader = ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
      initView();
    }

    private void initView() {
        ivAvater = (WyyCircleImageView) findViewById(R.id.img_account_icon);
    }

}
