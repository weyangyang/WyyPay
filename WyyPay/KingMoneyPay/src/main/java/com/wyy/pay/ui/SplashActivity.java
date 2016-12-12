package com.wyy.pay.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wyy.pay.R;
import com.wyy.pay.bean.TableUserBean;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TableUserBean userBean = (TableUserBean) new TableUserBean().querySingle(null,null,null,null,null,null);
        if(userBean!=null){
            BaseApplication.setUserName(userBean.getUserName());
            BaseApplication.setWyyCode(userBean.getWyyCode());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent  intent = new Intent(SplashActivity.this,MainUIActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            },1200);
        }else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            },1200);
        }
    }

}
