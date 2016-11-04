package com.weyangyang.pay.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.weyangyang.pay.R;


public abstract class BaseActivity extends Activity implements BaseUiInterf{
   public TextView tvNavLeft,tvNavTitle,tvNavRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNavBar();
    }

    private void initNavBar() {
        tvNavLeft = (TextView) findViewById(R.id.tvNavLeft);
        tvNavTitle = (TextView) findViewById(R.id.tvNavTitle);
        tvNavRight = (TextView) findViewById(R.id.tvNavRight);
    }


}
