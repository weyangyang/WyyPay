package com.wyy.pay.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.engine.RequestEngine;
import com.wyy.pay.engine.XTAsyncTask;
import com.wyy.pay.ui.dialog.CustomDialog;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import netutils.engine.NetReqCallBack;
import xtcore.utils.SystemUtils;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
private com.wyy.pay.view.ClearEditText etPhoneNum,etVerifyCode,etRegPasswd,etShopName;
    private Button btnVerifyCode;
    private CustomDialog mCustomDialog;
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
        tvNavRight.setText("登录");
        etPhoneNum = (ClearEditText) findViewById(R.id.etPhoneNum);
        etVerifyCode = (ClearEditText) findViewById(R.id.etVerifyCode);
        btnVerifyCode = (Button) findViewById(R.id.btnVerifyCode);
        etRegPasswd = (ClearEditText) findViewById(R.id.etRegPasswd);
        etShopName = (ClearEditText) findViewById(R.id.etShopName);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        tvNavRight.setOnClickListener(this);
        tvNavLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNavLeft://返回
                RegisterActivity.this.finish();
                break;
            case R.id.tvNavRight://返回
                RegisterActivity.this.finish();
                break;
            case R.id.btnRegister://注册

                if(SystemUtils.checkAllNet(this)){
                    checkParamAndRegister();
                }else {
                    Toast.makeText(RegisterActivity.this,getString(R.string.text_net_error),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnVerifyCode: //获取验证码
                break;
        }
    }

    private void checkParamAndRegister() {
        final String userName = etPhoneNum.getText().toString().trim();
        if(!Utils.isPhoneNumber(userName)){
            Toast.makeText(RegisterActivity.this,"你输入的手机号不正确！请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        final String verifyCode = etVerifyCode.getText().toString().trim();
        if(!Utils.isZhengNumber(verifyCode))
        {
            Toast.makeText(RegisterActivity.this,"你输入的验证码不正确！请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        final String password = etRegPasswd.getText().toString().trim();
        if(!Utils.isPassword(password)){
            Toast.makeText(RegisterActivity.this,"你输入的密码不正确！请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        final String storeName = etShopName.getText().toString().trim();
        new XTAsyncTask() {
            @Override
            protected void onPreExectue() {
                 mCustomDialog = CustomDialog.createLoadingDialog(RegisterActivity.this,
                 null, true);
            }

            @Override
            protected void doInbackgroud() {
                RequestEngine.getInstance().register(userName, password, verifyCode, storeName, new NetReqCallBack() {
                    @Override
                    public void getSuccData(int statusCode, final String strJson, String strUrl) {
                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject mJSONObject = new JSONObject(strJson);
                                    int code = mJSONObject.optInt("Code");
                                    String message = mJSONObject.optString("Message");
                                    if(code==1){
                                        //注册成功，到登录界面
                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                        intent.putExtra(ConstantUtils.INTENT_KEY_USER_NAME,userName);
                                        RegisterActivity.this.startActivity(intent);
                                        RegisterActivity.this.finish();
                                    }else {
                                        if(TextUtils.isEmpty(message))
                                            Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                });
            }

            @Override
            protected void onPostExecute() {
             mCustomDialog.dismiss();
             }
        };


    }
}
