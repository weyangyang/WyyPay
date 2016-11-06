package com.wyy.pay.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.pay.R;
import com.wyy.pay.view.WyyCircleImageView;


public class MyActivity extends Activity implements View.OnClickListener {
    private WyyCircleImageView ivAvater;//头像
    private TextView tvShopName;//店铺名称
    private TextView tvNameVerify;//实名认证
    private TextView tvSign;//签到
    private TextView tvAccountSetting;//账号设置
    private TextView tvCashierManage;//收银员管理
    private TextView tvCashierMateriel;//收银物料
    private TextView tvTransactionCosts;//交易费率
    private TextView tvCashierSetting;//收银设置
    private TextView tvEvaluateUs;//评价我们
    private TextView tvFeedback;//意见反馈
    private TextView tvAboutUs;//关于我们
    private TextView tvUpdateVersition;//版本更新
    private TextView tvNewVersionHint;//有新版本（提示）
    private TextView tvVersionNum;//版本号
    private ImageView ivShowAuthPic;//认证图标
    ImageLoader mImageLoader = ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
      initView();
        initData();
        initListener();
    }

    private void initListener() {
        ivAvater.setOnClickListener(this);
        tvNameVerify.setOnClickListener(this);
        tvSign.setOnClickListener(this);
        tvAccountSetting.setOnClickListener(this);
        tvCashierManage.setOnClickListener(this);
        tvCashierMateriel.setOnClickListener(this);
        tvTransactionCosts.setOnClickListener(this);
        tvCashierSetting.setOnClickListener(this);
        tvEvaluateUs.setOnClickListener(this);
        tvFeedback.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);
        tvUpdateVersition.setOnClickListener(this);
    }

    private void initData() {
    }

    private void initView() {
        ivAvater = (WyyCircleImageView) findViewById(R.id.ivAvater);
        tvShopName  = (TextView) findViewById(R.id.tvShopName);
        tvNameVerify  = (TextView) findViewById(R.id.tvNameVerify);
        tvSign  = (TextView) findViewById(R.id.tvSign);
        tvAccountSetting  = (TextView) findViewById(R.id.tvAccountSetting);
        tvCashierManage  = (TextView) findViewById(R.id.tvCashierManage);
        tvCashierMateriel  = (TextView) findViewById(R.id.tvCashierMateriel);
        tvTransactionCosts  = (TextView) findViewById(R.id.tvTransactionCosts);
        tvCashierSetting  = (TextView) findViewById(R.id.tvCashierSetting);
        tvEvaluateUs  = (TextView) findViewById(R.id.tvEvaluateUs);
        tvFeedback  = (TextView) findViewById(R.id.tvFeedback);
        tvAboutUs  = (TextView) findViewById(R.id.tvAboutUs);
        tvUpdateVersition  = (TextView) findViewById(R.id.tvUpdateVersition);
        tvNewVersionHint  = (TextView) findViewById(R.id.tvNewVersionHint);
        tvVersionNum  = (TextView) findViewById(R.id.tvVersionNum);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivAvater://点击头像
                break;
            case R.id.tvNameVerify://实名认证
                break;
            case R.id.tvSign://签到
                break;
            case R.id.tvAccountSetting://账号设置
                break;
            case R.id.tvCashierManage://收银员管理
                break;
            case R.id.tvCashierMateriel://收银物料
                break;
            case R.id.tvTransactionCosts://交易费率
                break;
            case R.id.tvCashierSetting://收银设置
                break;
            case R.id.tvEvaluateUs://评价我们
                break;
            case R.id.tvFeedback://意见反馈
                break;
            case R.id.tvAboutUs://关于我们
                break;
            case R.id.tvUpdateVersition://版本更新
                break;
        }
    }
}
