package com.wyy.pay.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.adapter.OrderCategoryListAdapter;
import com.wyy.pay.adapter.OrderProductListAdapter;
import com.wyy.pay.adapter.ProCategoryListAdapter;
import com.wyy.pay.bean.OrderCategoryBean;
import com.wyy.pay.bean.ProCategoryBean;
import com.wyy.pay.bean.ProductBean;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.ClearEditText;
import com.wyy.pay.view.XListView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProOrderActivity extends BaseActivity implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener, AdapterView.OnItemClickListener, OrderProductListAdapter.OrderProductItemOnClickListener {
    private com.wyy.pay.view.ClearEditText etProSearch;
    private XListView categoryListView;
    private XListView orderProListView;
    private OrderCategoryListAdapter categoryListAdapter;
    private OrderProductListAdapter proListAdapter;
    private ImageView ivShopingCart;//购物车
    private TextView tvSumShopNum;//购物车上显示商品数量
    private int totalShopingNum = 0;
    private TextView tvOrderTotalMoney;//合计：￥100.00
    private TextView tvOrderToPay;//去结算
    private List<ProductBean> proList;//商品list

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
        tvNavLeft.setText("无码");
        etProSearch = (ClearEditText) findViewById(R.id.etProSearch);
        categoryListView = (XListView) findViewById(R.id.categoryListView);
        categoryListView.setPullLoadEnable(false);
        categoryListView.setPullRefreshEnable(false);
        categoryListView.setOnItemClickListener(this);
        orderProListView = (XListView) findViewById(R.id.orderProListView);
        orderProListView.setPullRefreshEnable(false);
        orderProListView.setPullLoadEnable(false);
        ivShopingCart = (ImageView) findViewById(R.id.ivShopingCart);
        tvSumShopNum = (TextView) findViewById(R.id.tvSumShopNum);
        tvOrderTotalMoney = (TextView) findViewById(R.id.tvOrderTotalMoney);
        tvOrderToPay = (TextView) findViewById(R.id.tvOrderToPay);

    }

    @Override
    public void initData() {
        categoryListAdapter = new OrderCategoryListAdapter(this);
        List<OrderCategoryBean> beanList = new ArrayList<OrderCategoryBean>();
        for (int i = 0; i < 6; i++) {
            totalShopingNum += i * 3;
            OrderCategoryBean bean = new OrderCategoryBean();
            String categoryName = "默认分类" + i;
            bean.setCategoryId(Utils.get6MD5WithString(categoryName));
            bean.setCategoryName(categoryName);
            bean.setProSumCount(i * 3 + "");
            beanList.add(bean);
        }
        categoryListAdapter.setCategoryListData(beanList);
        categoryListView.setAdapter(categoryListAdapter);
        categoryListView.setItemChecked(1, true);
        categoryListView.setActivated(true);
        categoryListView.setFocusable(true);
        categoryListView.requestFocusFromTouch();
        categoryListView.performItemClick(categoryListView.getAdapter().getView(1, null, null), 1, 1);
        categoryListView.setSelection(1);
        if (totalShopingNum > 0) {
            tvSumShopNum.setVisibility(View.VISIBLE);
            tvSumShopNum.setText(String.valueOf(totalShopingNum));
        }
        tvOrderTotalMoney.setText(String.format("合计：￥%s", "0.00"));

        proListAdapter = new OrderProductListAdapter(this);
        proList = new ArrayList<ProductBean>();
        for (int i = 0; i < 10; i++) {
            ProductBean bean = new ProductBean();
            bean.setCategoryId("categoryId" + i);
            if (i == 1 || i == 5) {
                bean.setAddProCount(0);
            } else {
                bean.setAddProCount(2 * i);
            }

            bean.setImgUrl("http://www.baidu.com");
            bean.setProName("商品" + i);
            bean.setProNo("No" + i * 9);
            bean.setProPrice(18.00);
            proList.add(bean);
        }
        proListAdapter.setProductListData(proList);
        orderProListView.setAdapter(proListAdapter);
        proListAdapter.setOrderProductItemOnClickListener(this);

    }

    @Override
    public void initListener() {
        tvNavRight.setOnClickListener(this);
        tvNavLeft.setOnClickListener(this);
        etProSearch.addTextChangedListener(this);
        etProSearch.setOnEditorActionListener(this);
        ivShopingCart.setOnClickListener(this);
        tvOrderToPay.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideSoftInputFromWindow();
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tvNavRight:
                intent = new Intent(ProOrderActivity.this, ScanPayActivity.class);
                intent.putExtra(ConstantUtils.INTENT_KEY_PAY_TYPE, ConstantUtils.PAY_TYPE_SCAN_PRO);
                startActivity(intent);
                break;
            case R.id.tvNavLeft:
                intent = new Intent(ProOrderActivity.this, ScanPayActivity.class);
                intent.putExtra(ConstantUtils.INTENT_KEY_PAY_TYPE, ConstantUtils.PAY_TYPE_ALIPAY);
                intent.putExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY, 100.00f);
                startActivity(intent);
                break;
            case R.id.ivShopingCart://去购物车
                Toast.makeText(this, "去购物车", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvOrderToPay://去结算
                Toast.makeText(this, "去结算", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


        String shopingName = s.toString();
        Toast.makeText(this, "商品名称：" + shopingName, Toast.LENGTH_SHORT).show();
        /**
         *     String userName = s.toString();
         if (!TextUtils.isEmpty(userName)) {
         beans.clear();
         beans = (ArrayList<GetUserMsgListBean>) new GetUserMsgListBean()
         .query(null, GetUserMsgListBean.COLUMN_NICK_NAME
         + " like '%" + userName + "%'", null, null,
         null, null);
         mAdapter.notifyDataSetChanged();
         } else {
         beans.clear();
         beans = (ArrayList<GetUserMsgListBean>) new GetUserMsgListBean()
         .query(null, null, null, null, null, null);
         mAdapter.notifyDataSetChanged();
         }
         */
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return true;
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        categoryListView.setFocusable(true);
//        categoryListView.setFocusableInTouchMode(true);
//        categoryListView.requestFocus();
//        return false;
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "position==" + position, Toast.LENGTH_SHORT).show();
        categoryListAdapter.setCurrentPosition(position);
        categoryListAdapter.notifyDataSetInvalidated();
        hideSoftInputFromWindow();
    }

    private void hideSoftInputFromWindow() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etProSearch.getWindowToken(), 0);
    }

    @Override
    public void addProOnClick(int position, ProductBean bean) {
        Toast.makeText(this, "点击position==" + position + "::bean==" + bean.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void reduceProOnClick(int position, ProductBean bean) {
        Toast.makeText(this, "点击position==" + position + "::bean==" + bean.toString(), Toast.LENGTH_SHORT).show();
    }
}
