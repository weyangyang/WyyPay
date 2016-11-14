package com.wyy.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.pay.R;
import com.wyy.pay.bean.ProductBean;
import com.wyy.pay.utils.BaseOptions;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.view.ClearEditText;


public class ProDetailActivity extends BaseActivity implements View.OnClickListener {
private ClearEditText editBarCode;//输入条码
private ClearEditText editProName;//输入商品名称
private Button btnScanBarCode;//扫码按钮
private ClearEditText editProPrice;//输入商品价格
    private TextView tvProCategory;//选择分类
    private ImageView ivProImg;//商品图片
    private Button btnProSave;//保存商品
    private ProductBean mProductBean;//商品数据bean

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_product_detail);
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();

    }

    @Override
    public void initView() {
        tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
        editBarCode = (ClearEditText) findViewById(R.id.editBarCode);
        editProName = (ClearEditText) findViewById(R.id.editProName);
        btnScanBarCode = (Button) findViewById(R.id.btnScanBarCode);
        tvProCategory = (TextView) findViewById(R.id.tvProCategory);
        editProPrice = (ClearEditText) findViewById(R.id.editProPrice);
        ivProImg = (ImageView) findViewById(R.id.ivProImg);
        btnProSave = (Button) findViewById(R.id.btnProSave);
        btnProSave.setVisibility(View.GONE);
        btnScanBarCode.setVisibility(View.GONE);
        ivProImg.setEnabled(false);
        editBarCode.setInputType(InputType.TYPE_NULL);
        tvProCategory.setEnabled(false);
        editProPrice.setInputType(InputType.TYPE_NULL);
        editProName.setInputType(InputType.TYPE_NULL);
        Intent intent = getIntent();
       int from =  intent.getIntExtra(ConstantUtils.INTENT_KEY_FROM_ACTIVITY_TYPE,ConstantUtils.FROM_POPUP_WINDOW_ADD_PRODUCT);
        if(ConstantUtils.FROM_PRODUCT_MANAGE_ACTIVITY_PRODUCT_LIST_ITEM == from){
            //来自pro list item 点击
            mProductBean = (ProductBean) intent.getSerializableExtra(ConstantUtils.INTENT_KEY_PRODUCT_BEAN);
            tvNavTitle.setText("商品详情");
            tvNavRight.setVisibility(View.VISIBLE);
            tvNavRight.setText("编辑");
            if(mProductBean!=null){
                editBarCode.setText(mProductBean.getProName());
                editBarCode.setText(mProductBean.getProNo());
                tvProCategory.setText(mProductBean.getCategoryId());//一会修改成分类名称
                editProPrice.setText(String.valueOf(mProductBean.getProPrice()));
                ImageLoader.getInstance().displayImage(mProductBean.getImgUrl(),ivProImg, BaseOptions.getInstance().getProductImgOptions());

            }

        }else if(ConstantUtils.FROM_POPUP_WINDOW_ADD_PRODUCT == from){
            //来自popupwindow add button 点击
            tvNavTitle.setText("新增商品");
            tvNavRight.setVisibility(View.GONE);
            btnProSave.setVisibility(View.VISIBLE);
            btnScanBarCode.setVisibility(View.VISIBLE);
            ivProImg.setEnabled(true);
            editBarCode.setInputType(InputType.TYPE_CLASS_TEXT);
            tvProCategory.setEnabled(true);
            editProPrice.setInputType(InputType.TYPE_CLASS_TEXT);
        }

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        tvNavLeft.setOnClickListener(this);
        btnProSave.setOnClickListener(this);
        ivProImg.setOnClickListener(this);
        tvProCategory.setOnClickListener(this);
        btnScanBarCode.setOnClickListener(this);
        tvNavRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNavLeft://返回
                ProDetailActivity.this.finish();
                break;
            case R.id.btnProSave://保存商品
                break;
            case R.id.ivProImg: //设置商品图片
                break;
            case R.id.tvProCategory: //选择分类
                break;
            case R.id.btnScanBarCode: //扫码获取条码
                break;
            case R.id.tvNavRight: //编辑
                break;
        }
    }
}
