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
import com.wyy.pay.bean.TableUserBean;
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


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private com.wyy.pay.view.ClearEditText etAccount, etPasswd;
    private TextView tvForgetPwd;
    private Button btnRegister, btnLogin;
    private CustomDialog mCustomDialog;
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
                if(SystemUtils.checkAllNet(this)){
                    checkParamAndLogin();
                }else {
                    Toast.makeText(this,getString(R.string.text_net_error),Toast.LENGTH_SHORT).show();
                }
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

    private void checkParamAndLogin() {
        final String userName = etAccount.getText().toString().trim();
        final String password = etPasswd.getText().toString().trim();

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
        new XTAsyncTask() {
            @Override
            protected void onPreExectue() {
                if(mCustomDialog==null)
                    mCustomDialog = CustomDialog.createLoadingDialog(LoginActivity.this,
                            "正在登录", true);
            }

            @Override
            protected void doInbackgroud() {
                RequestEngine.getInstance().login(userName, password, new NetReqCallBack() {
                    @Override
                    public void getSuccData(int statusCode, final String strJson, String strUrl) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject mJSONObject = new JSONObject(strJson);
                                    int code = mJSONObject.optInt("Code");
                                    String message = mJSONObject.optString("Message");
                                    if(code==1){
                                        //登录成功，到登录界面
                                        TableUserBean userBean = new TableUserBean();
                                        JSONObject obj = mJSONObject.optJSONObject("Obj");
                                        String userName = obj.optString("UserName");
                                        userBean.setUserName(userName);
                                        String userId = Utils.get6MD5WithString(userName);
                                        userBean.setUserId(userId);
                                        String storeName = obj.optString("StoreName");
                                        userBean.setStoreName(storeName);
                                        String storeLogo = obj.optString("storeLogo");
                                        userBean.setStoreLogo(storeLogo);
                                        String wyyCode = obj.optString("WyyCode");
                                        userBean.setWyyCode(wyyCode);
                                        int subId = obj.optInt("SubId");
                                        userBean.setSubId(subId);
                                        int isFree = obj.optInt("IsFree");
                                        userBean.setIsFree(isFree);
                                        userBean.insert(true,TableUserBean.COLUMN_USER_ID,userId);
                                        BaseApplication.setUserName(userName);
                                        BaseApplication.setWyyCode(wyyCode);
                                          Intent  intent = new Intent(LoginActivity.this,MainUIActivity.class);
                                            startActivity(intent);
                                            LoginActivity.this.finish();
                                    }else {
                                        if(!TextUtils.isEmpty(message))
                                            Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(LoginActivity.this,"服务器异常，请稍后再试",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }

            @Override
            protected void onPostExecute() {
                if(null!=LoginActivity.this&& mCustomDialog.isShowing())
                    mCustomDialog.dismiss();
            }
        }.execute();
    }
}
