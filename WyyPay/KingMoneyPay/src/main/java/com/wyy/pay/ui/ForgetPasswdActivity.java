package com.wyy.pay.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.bean.TableUserBean;
import com.wyy.pay.engine.RequestEngine;
import com.wyy.pay.engine.XTAsyncTask;
import com.wyy.pay.sms.SmsObserver;
import com.wyy.pay.ui.dialog.CustomDialog;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import netutils.engine.NetReqCallBack;
import xtcore.utils.PreferenceUtils;
import xtcore.utils.SystemUtils;


public class ForgetPasswdActivity extends BaseActivity implements View.OnClickListener {
private ClearEditText etPhoneNum,etVerifyCode,etRegPasswd;
    private Button btnCommit;
    private Button btnVerifyCode;
    private CustomDialog mCustomDialog;
    private SmsObserver smsObserver;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
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
        btnVerifyCode = (Button) findViewById(R.id.btnVerifyCode);
        etRegPasswd = (ClearEditText) findViewById(R.id.etRegPasswd);
    }

    @Override
    public void initData() {
        smsObserver = new SmsObserver(this, handler);
        this.getContentResolver()
                .registerContentObserver(SMS_INBOX, true, smsObserver);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnVerifyCode.setText("获取验证码");
        handler.removeMessages(COUNT);
        this.getContentResolver()
                .unregisterContentObserver(smsObserver);
    }
    @Override
    public void initListener() {
        tvNavLeft.setOnClickListener(this);
        btnVerifyCode.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
    }
    private final int COUNT = 0;
    private final int ERROR = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNT: {
                    long time = System.currentTimeMillis()
                            - PreferenceUtils.getPrefLong(ForgetPasswdActivity.this,"last_get_identify", 0);
                    if (time > 60 * 1000) {
                        showOldStatus();
                    } else {
                        btnVerifyCode.setClickable(false);
                        setIdentifyButton(false);
                        btnVerifyCode.setText(String.format("重新获取(%s)",
                                60 - time / 1000));
                        handler.sendEmptyMessageDelayed(COUNT, 1000);
                    }
                    break;
                }
                case ERROR: {
                    Toast.makeText(ForgetPasswdActivity.this, msg.obj.toString(),
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                case ConstantUtils.SMS_HANDLER_CODE: {
                    String code = (String) msg.obj;
                    if (!TextUtils.isEmpty(code)) {
                        etVerifyCode.setText(code);
                    }
                    break;
                }
            }
        }

        private void showOldStatus() {
            btnVerifyCode.setClickable(true);
            setIdentifyButton(true);
            btnVerifyCode.setText("获取验证码");
        }
    };
    public void setIdentifyButton(boolean clickable) {
        if (!clickable) {
            btnVerifyCode.setTextColor(getResources().getColor(
                    R.color.text_cashier_num));
            btnVerifyCode
                    .setBackgroundResource(R.drawable.bg_btn_other);
        } else {
            btnVerifyCode.setTextColor(getResources().getColor(
                    R.color.white));
            btnVerifyCode
                    .setBackgroundResource(R.drawable.bg_btn_normal);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNavLeft://返回
                ForgetPasswdActivity.this.finish();
                break;
            case R.id.btnCommit://修改密码提交
                if(SystemUtils.checkAllNet(this)){
                    checkParamAndCommitPwd();
                }else {
                    Toast.makeText(this,getString(R.string.text_net_error),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnVerifyCode: //获取验证码
                String phone = etPhoneNum.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Utils.isPhoneNumber(phone)){
                    if(SystemUtils.checkAllNet(this)){
                        PreferenceUtils.setPrefLong(ForgetPasswdActivity.this,"last_get_identify",
                                System.currentTimeMillis());
                        handler.sendEmptyMessage(COUNT);
                        getSmsVerifyCode(phone);
                    }else {
                        Toast.makeText(ForgetPasswdActivity.this,getString(R.string.text_net_error),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this,"目前只支持中国地区手机号码",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void checkParamAndCommitPwd() {
        final String userName = etPhoneNum.getText().toString().trim();
        if(!Utils.isPhoneNumber(userName)){
            Toast.makeText(this,"你输入的手机号不正确！请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        final String verifyCode = etVerifyCode.getText().toString().trim();
        if(!Utils.isZhengNumber(verifyCode))
        {
            Toast.makeText(this,"你输入的验证码不正确！请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        final String password = etRegPasswd.getText().toString().trim();
        if(!Utils.isPassword(password)){
            Toast.makeText(this,"你输入的密码不正确！请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }
        new XTAsyncTask() {
            @Override
            protected void onPreExectue() {
                if(mCustomDialog==null)
                    mCustomDialog = CustomDialog.createLoadingDialog(ForgetPasswdActivity.this,
                            "正在提交数据", true);
            }

            @Override
            protected void doInbackgroud() {
                RequestEngine.getInstance().frogetPasswdCommit(userName, password, verifyCode,new NetReqCallBack() {
                    @Override
                    public void getSuccData(int statusCode, final String strJson, String strUrl) {
                        ForgetPasswdActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject mJSONObject = new JSONObject(strJson);
                                    int code = mJSONObject.optInt("Code");
                                    String message = mJSONObject.optString("Message");
                                    if(code==1){
                                        //重置密码成功，到登录界面
                                        TableUserBean userBean = (TableUserBean) new TableUserBean().querySingle(null,null,null,null,null,null);
                                        if(userBean!=null){
                                            userBean.delete();
                                        }
                                        Intent intent = new Intent(ForgetPasswdActivity.this,LoginActivity.class);
                                        intent.putExtra(ConstantUtils.INTENT_KEY_USER_NAME,userName);
                                        ForgetPasswdActivity.this.startActivity(intent);
                                        BaseApplication.setUserName("");
                                        BaseApplication.setWyyCode("");
                                        ForgetPasswdActivity.this.finish();

                                    }else {
                                        if(!TextUtils.isEmpty(message))
                                            Toast.makeText(ForgetPasswdActivity.this,message,Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ForgetPasswdActivity.this,"服务器异常，请稍后再试",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
            }

            @Override
            protected void onPostExecute() {
                if(null!=ForgetPasswdActivity.this&& mCustomDialog.isShowing())
                    mCustomDialog.dismiss();
            }
        }.execute();
    }

    private void getSmsVerifyCode(final String phone) {
        new XTAsyncTask() {
            @Override
            protected void onPreExectue() {
                if(mCustomDialog==null)
                    mCustomDialog = CustomDialog.createLoadingDialog(ForgetPasswdActivity.this,
                            "正在获取短信验证码", true); }

            @Override
            protected void doInbackgroud() {
                RequestEngine.getInstance().getSmsVerifyCode(phone, new NetReqCallBack() {
                    @Override
                    public void getSuccData(int statusCode, String strJson, String strUrl) {
                        try {
                            JSONObject mJSONObject = new JSONObject(strJson);
                            int code = mJSONObject.optInt("Code");
                            String message = mJSONObject.optString("Message");
                            if(code==1){
                                //注册成功，到登录界面
                                PreferenceUtils.setPrefLong(ForgetPasswdActivity.this,"last_get_identify",
                                        System.currentTimeMillis());
                                handler.sendEmptyMessage(COUNT);
                            }else {
                                if(TextUtils.isEmpty(message))
                                    Toast.makeText(ForgetPasswdActivity.this,message,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ForgetPasswdActivity.this,"服务器异常，请稍后再试",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            protected void onPostExecute() {
                if(null!=ForgetPasswdActivity.this && mCustomDialog.isShowing())
                    mCustomDialog.dismiss();
            }
        }.execute();
    }

}
