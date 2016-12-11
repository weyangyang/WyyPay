package com.wyy.pay.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.ClearEditText;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private com.wyy.pay.view.ClearEditText etAccount, etPasswd;
    private TextView tvForgetPwd;
    private Button btnRegister, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        etAccount = (ClearEditText) findViewById(R.id.etAccount);
        etPasswd = (ClearEditText) findViewById(R.id.etPasswd);
        tvForgetPwd = (TextView) findViewById(R.id.tvForgetPwd);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String username = intent.getStringExtra(ConstantUtils.INTENT_KEY_USER_NAME);
        etAccount.setText(username);

    }

    @Override
    public void initListener() {
        tvForgetPwd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnLogin://登录
                if(3>2){
                    intent = new Intent(LoginActivity.this,MainUIActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                    return;
                }

                final String userName = etAccount.getText().toString().trim();
                String password = etPasswd.getText().toString().trim();

                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, R.string.username_or_password_empty,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Utils.isPhoneNumber(userName)) {
                    Toast.makeText(LoginActivity.this, R.string.username_is_error,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Utils.isPassword(password)) {
                    Toast.makeText(LoginActivity.this, R.string.passwd_is_error,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
//                if("13478277728".equals(userName)&&"wyy277728".equals(password)){
//                    intent = new Intent(LoginActivity.this,MainUIActivity.class);
//                    startActivity(intent);
//                    LoginActivity.this.finish();
//                }
                break;
            case R.id.btnRegister://注册
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.tvForgetPwd://忘记密码
                intent = new Intent(LoginActivity.this,ForgetPasswdActivity.class);
                startActivity(intent);
                break;
        }
    }
}
