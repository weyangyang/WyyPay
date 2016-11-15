package com.wyy.pay.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.wyy.pay.R;
import com.wyy.pay.view.ClearEditText;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
private com.wyy.pay.view.ClearEditText etPhoneNum,etVerifyCode,etRegPasswd,etNickName,etShopName;
    private Button btnVerifyCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();

    }

    @Override
    public void initView() {
        tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
        tvNavTitle.setText("注册");
        etPhoneNum = (ClearEditText) findViewById(R.id.etPhoneNum);
        etVerifyCode = (ClearEditText) findViewById(R.id.etVerifyCode);
        btnVerifyCode = (Button) findViewById(R.id.btnVerifyCode);
        etRegPasswd = (ClearEditText) findViewById(R.id.etRegPasswd);
        etNickName = (ClearEditText) findViewById(R.id.etNickName);
        etShopName = (ClearEditText) findViewById(R.id.etShopName);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        tvNavLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNavLeft://返回
                RegisterActivity.this.finish();
                break;
            case R.id.btnRegister://注册
                break;
            case R.id.btnVerifyCode: //获取验证码
                break;
        }
    }
}
