package com.wyy.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wyy.pay.R;
import com.wyy.pay.utils.ConstantUtils;

public class ProOrderActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pro_order);
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        tvNavTitle.setText("生成订单");
        tvNavRight.setText("扫码");
        tvNavLeft.setText("付款");
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
        Intent intent = null;
        switch (v.getId()){
            case R.id.tvNavRight:
                intent = new Intent(ProOrderActivity.this,ScanPayActivity.class);
                intent.putExtra(ConstantUtils.INTENT_KEY_PAY_TYPE,ConstantUtils.PAY_TYPE_SCAN_PRO);
                startActivity(intent);
                break;
            case R.id.tvNavLeft:
                intent = new Intent(ProOrderActivity.this,ScanPayActivity.class);
                intent.putExtra(ConstantUtils.INTENT_KEY_PAY_TYPE,ConstantUtils.PAY_TYPE_ALIPAY);
                intent.putExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,100.00f);
                startActivity(intent);
                break;
        }
    }
}
