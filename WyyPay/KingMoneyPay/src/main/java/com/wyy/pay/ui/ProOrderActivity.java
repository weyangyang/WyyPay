package com.wyy.pay.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.wyy.pay.bean.TableGoodsDetailBean;
import com.wyy.pay.bean.TableCategoryBean;
import com.wyy.pay.ui.dialog.CustomProgressDialog;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.ClearEditText;
import com.wyy.pay.view.XListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import db.utils.BaseDbBean;
import db.utils.TableDataListener;

public class ProOrderActivity extends BaseActivity implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener,OrderProductListAdapter.OrderProductItemOnClickListener, ProCategoryListAdapter.CategoryItemOnClickListener {
    private com.wyy.pay.view.ClearEditText etProSearch;
    private XListView categoryListView;
    private XListView orderProListView;
    private OrderCategoryListAdapter categoryListAdapter;
    private OrderProductListAdapter proListAdapter;
    private ImageView ivShopingCart;//购物车
    private TextView tvSumShopNum;//购物车上显示商品数量
    private TextView tvOrderTotalMoney;//合计：￥100.00
    private TextView tvOrderToPay;//去结算
    private List<TableGoodsDetailBean> proList;//商品list
    private CustomProgressDialog initDialog;
    private List<TableCategoryBean> categoryList;
    private TableDataListener<TableGoodsDetailBean> dataListener;
    private TableDataListener<TableCategoryBean> categoryDataListener;
    private  ArrayList<TableGoodsDetailBean> shopingCartList;//购物车
    private String currentCName ="默认分类";//当前分类名称
    private static final int TABLE_CATEGORY_CHANGED = 0;
    private static final int INIT_CATEGORY_COMPLETE = 1;
    private static final int TABLE_GOODS_CHANGED = 2;
    private static final int INIT_GOODS_COMPLETE = 3;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TABLE_GOODS_CHANGED:
                    getGoodsDataFromDB(currentCName);
                    break;
                case INIT_GOODS_COMPLETE:
                    if(initDialog!=null){
                        initDialog.dismiss();
                    }
                    if(proList==null){
                        proList = new ArrayList<>();
                        //显示商品数据为空界面
                    }
                    proListAdapter.setProductListData(proList);
                    proListAdapter.notifyDataSetChanged();
                    break;
                case TABLE_CATEGORY_CHANGED:
                    getCategoryDataFromDB();
                    break;
                case INIT_CATEGORY_COMPLETE:
                    if(initDialog!=null){
                        initDialog.dismiss();
                    }
                    if(categoryList==null){
                        categoryList = new ArrayList<>();
                        //显示商品数据为空界面
                    }
                    categoryListAdapter.setCategoryListData(categoryList);
                    categoryListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };




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
        proList = new ArrayList<>();
        categoryList = new ArrayList<>();
        shopingCartList = new ArrayList<>();
        categoryListAdapter = new OrderCategoryListAdapter(this);
        categoryListAdapter.setItemOnClickListener(this);
        getCategoryDataFromDB();
        categoryListAdapter.setCategoryListData(categoryList);
        categoryListView.setAdapter(categoryListAdapter);
        categoryListView.setItemChecked(1, true);
        categoryListView.setActivated(true);
        categoryListView.setFocusable(true);
        categoryListView.requestFocusFromTouch();
        categoryListView.performItemClick(categoryListView.getAdapter().getView(1, null, null), 1, 1);
        categoryListView.setSelection(1);

        tvOrderTotalMoney.setText(String.format("合计：￥%s", "0.00"));

        proListAdapter = new OrderProductListAdapter(this);

        getGoodsDataFromDB(currentCName);
        proListAdapter.setProductListData(proList);
        orderProListView.setAdapter(proListAdapter);
        proListAdapter.setOrderProductItemOnClickListener(this);

    }
    /**
     * 从数据库获取数据
     * @param goodsCname 商品分类名称
     */
    private void getGoodsDataFromDB(final String goodsCname) {
        if (initDialog == null) {
            initDialog = CustomProgressDialog.createLoadingDialog(this);
        }
        if (!initDialog.isShowing()) {
            initDialog.show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                TableGoodsDetailBean bean = new TableGoodsDetailBean();
                String orderBy = TableGoodsDetailBean.COLUMN_GOODS_CREATE_TIME+" ASC";
                proList =  bean.query(null,TableGoodsDetailBean.COLUMN_USER_ID +"=? AND "+TableGoodsDetailBean.COLUMN_GOODS_CATEGORY_NAME+" =?",new String[]{Utils.get6MD5WithString("18501053570"),goodsCname},null,null,orderBy);
                handler.sendEmptyMessage(INIT_GOODS_COMPLETE);
            }
        }).start();
    }
    private void getCategoryDataFromDB() {
        if (initDialog == null) {
            initDialog = CustomProgressDialog.createLoadingDialog(this);
        }
        if (!initDialog.isShowing()) {
            initDialog.show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(categoryList!=null&&categoryList.size()>0){
                    categoryList.clear();
                }
                TableCategoryBean tableCategoryBean = new TableCategoryBean();
                String orderBy = TableCategoryBean.COLUMN_CREATE_CATEGORY_TIEM+" ASC";
                categoryList =  tableCategoryBean.query(null,TableCategoryBean.COLUMN_USER_ID +"=?",new String[]{Utils.get6MD5WithString("18501053570")},null,null,orderBy);
                handler.sendEmptyMessage(INIT_CATEGORY_COMPLETE);
            }
        }).start();
    }
    @Override
    public void initListener() {
        tvNavRight.setOnClickListener(this);
        tvNavLeft.setOnClickListener(this);
        etProSearch.addTextChangedListener(this);
        etProSearch.setOnEditorActionListener(this);
        ivShopingCart.setOnClickListener(this);
        tvOrderToPay.setOnClickListener(this);
        categoryDataListener = new TableDataListener<TableCategoryBean>(handler) {
            @Override
            public void onDataChanged(int type, TableCategoryBean obj) {
                if (type == TableDataListener.TYPE_UPDATE) {
                    handler.sendEmptyMessage(TABLE_CATEGORY_CHANGED);
                }
                if (type == TableDataListener.TYPE_ADD) {
                    handler.sendEmptyMessage(TABLE_CATEGORY_CHANGED);
                }
                if (type == TableDataListener.TYPE_DELETE
                        || type == TableDataListener.TYPE_RAW_DELETE) {
                    return;
                }
            }
        };
        dataListener = new TableDataListener<TableGoodsDetailBean>(handler) {
            @Override
            public void onDataChanged(int type, TableGoodsDetailBean obj) {
                if (type == TableDataListener.TYPE_UPDATE) {
                    handler.sendEmptyMessage(TABLE_GOODS_CHANGED);
                }
                if (type == TableDataListener.TYPE_ADD) {
                    handler.sendEmptyMessage(TABLE_GOODS_CHANGED);
                }
                if (type == TableDataListener.TYPE_DELETE
                        || type == TableDataListener.TYPE_RAW_DELETE) {
                    return;
                }
            }
        };
        TableCategoryBean.registerContentObserver(TableCategoryBean.TABLE_NAME,categoryDataListener);
        TableGoodsDetailBean.registerContentObserver(TableGoodsDetailBean.TABLE_NAME,dataListener);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideSoftInputFromWindow();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        BaseDbBean.unregisterContentObserver(TableGoodsDetailBean.TABLE_NAME, dataListener);
        BaseDbBean.unregisterContentObserver(TableCategoryBean.TABLE_NAME, dataListener);
        super.onDestroy();
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
                if(shopingCartList!=null&&shopingCartList.size()>0){
                    StringBuilder builder = new StringBuilder();
                    for (TableGoodsDetailBean goodsDetailBean:shopingCartList){
                        builder.append(goodsDetailBean.toString());
                    }
                    Toast.makeText(this, "去购物车"+builder.toString(), Toast.LENGTH_SHORT).show();
                }
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


    private void hideSoftInputFromWindow() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etProSearch.getWindowToken(), 0);
    }
    private void setShopingListSumCount(TableGoodsDetailBean bean){
        if(shopingCartList==null){
            shopingCartList = new ArrayList<>();
        }else {
            shopingCartList.clear();
        }
        int totalShopingNum =0;
        if(proList!=null &&proList.size()>0){
            for (TableGoodsDetailBean goodsDetailBean:proList){
                if(goodsDetailBean.getAddGoodsCount()>0){
                    shopingCartList.add(goodsDetailBean);
                    totalShopingNum+=goodsDetailBean.getAddGoodsCount();
                }
            }
        }
        if (totalShopingNum > 0) {
            tvSumShopNum.setVisibility(View.VISIBLE);
            tvSumShopNum.setText(String.valueOf(totalShopingNum));
        }else {
            tvSumShopNum.setVisibility(View.GONE);
        }
    }
    @Override
    public void addProOnClick(int position, TableGoodsDetailBean bean) {
        int goodsCount = bean.getAddGoodsCount();
        goodsCount+=1;
        if(proList!=null&&proList.size()>0){
            proList.get(position).setAddGoodsCount(goodsCount);
        }
        proListAdapter.setProductListData(proList);
        proListAdapter.notifyDataSetChanged();
        bean.setAddGoodsCount(goodsCount);
        setShopingListSumCount(bean);
        Toast.makeText(this, "点击position==" + position + "::bean==" + bean.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void reduceProOnClick(int position, TableGoodsDetailBean bean) {
        if(bean.getAddGoodsCount()>0){
            int goodsCount = bean.getAddGoodsCount();
            goodsCount-=1;

            if(proList!=null&&proList.size()>0){
                proList.get(position).setAddGoodsCount(goodsCount);
            }
            proListAdapter.setProductListData(proList);
            proListAdapter.notifyDataSetChanged();
            bean.setAddGoodsCount(goodsCount);
            setShopingListSumCount(bean);
        }
        Toast.makeText(this, "点击position==" + position + "::bean==" + bean.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void categoryItemOnClick(int position, String categoryId, String categoryName) {
        currentCName = categoryName;
        categoryListAdapter.setCurrentPosition(position);
        categoryListAdapter.notifyDataSetInvalidated();
        hideSoftInputFromWindow();
        getGoodsDataFromDB(categoryName);
    }
}
