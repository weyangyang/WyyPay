package com.wyy.pay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.view.ClearEditText;


public class ProManageActivity extends BaseActivity implements View.OnClickListener, ProManagePopWindow.ProMpopWindowOnClickListener {
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
        tvNavLeft.setText("订单");
        tvNavTitle.setText("商品管理");
        tvNavRight.setText("更多");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        tvNavLeft.setOnClickListener(this);
        tvNavRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNavLeft://返回
                ProManageActivity.this.finish();
                break;
            case R.id.tvNavRight:
                ProManagePopWindow morePopWindow = new ProManagePopWindow(ProManageActivity.this);
                morePopWindow.showPopupWindow(tvNavRight);
                morePopWindow.setProMpopWindowOnClickListener(this);
                break;
        }
    }

    @Override
    public void tvAddProOnClick() {
        Toast.makeText(this,"11",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void tvEditProOnClick() {
        Toast.makeText(this,"12",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void tvAddCategoryOnClick() {
        Toast.makeText(this,"13",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void tvEditCategoryOnClick() {
        Toast.makeText(this,"14",Toast.LENGTH_SHORT).show();
    }
}
