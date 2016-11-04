package com.weyangyang.pay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.weyangyang.pay.R;
import com.weyangyang.pay.view.ClearEditText;


public class ForgetPasswdActivity extends BaseActivity implements View.OnClickListener {
private ClearEditText etPhoneNum,etVerifyCode,etRegPasswd;
    private Button btnCommit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forget_passwd);
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();

    }

    @Override
    public void initView() {
        tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
        tvNavTitle.setText("忘记密码");
        etPhoneNum = (ClearEditText) findViewById(R.id.etPhoneNum);
        etVerifyCode = (ClearEditText) findViewById(R.id.etVerifyCode);
        btnCommit = (Button) findViewById(R.id.btnCommit);
        etRegPasswd = (ClearEditText) findViewById(R.id.etRegPasswd);
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
                ForgetPasswdActivity.this.finish();
                break;
            case R.id.btnCommit://注册
                break;
            case R.id.btnVerifyCode: //获取验证码
                break;
        }
    }
}
