package com.wyy.pay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wyy.pay.R;
import com.wyy.pay.view.ClearEditText;


public class ProManageActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_product_manage);
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();

    }

    @Override
    public void initView() {
        tvNavLeft.setText("分类");
        tvNavTitle.setText("商品管理");
        tvNavRight.setText("新增");
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
                ProManageActivity.this.finish();
                break;
        }
    }
}
