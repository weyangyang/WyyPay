package com.wyy.pay.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.adapter.OrderCategoryListAdapter;
import com.wyy.pay.adapter.OrderProductListAdapter;
import com.wyy.pay.adapter.ProCategoryListAdapter;
import com.wyy.pay.bean.TableCategoryBean;
import com.wyy.pay.bean.TableGoodsDetailBean;
import com.wyy.pay.ui.dialog.CustomProgressDialog;
import com.wyy.pay.ui.dialog.NoBarCodeCashierDialog;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.ClearEditText;
import com.wyy.pay.view.XListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import db.utils.BaseDbBean;
import db.utils.TableDataListener;

public class ProOrderActivity extends BaseActivity implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener,OrderProductListAdapter.OrderProductItemOnClickListener, ProCategoryListAdapter.CategoryItemOnClickListener, ShopingCartPopWindow.ShopingCartPopWindowListener, NoBarCodeCashierDialog.InfoCallback {
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
    private ShopingCartPopWindow cartPopWindow;//购物车列表window
    private View viewBD;//用于显示cart popup window
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
       viewBD =  findViewById(R.id.viewOrderBottomDevide);
        tvSumShopNum = (TextView) findViewById(R.id.tvSumShopNum);
        tvOrderTotalMoney = (TextView) findViewById(R.id.tvOrderTotalMoney);
        tvOrderToPay = (TextView) findViewById(R.id.tvOrderToPay);

    }

    @Override
    public void initData() {
        clearAddGoodsCount4DB();
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
private static final int TO_SCAN_ADD_SHOPING_REQUEST_CODE = 112;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && TO_SCAN_ADD_SHOPING_REQUEST_CODE ==requestCode){
            if(data!=null){
                if(shopingCartList!=null&&shopingCartList.size()>0){
                    shopingCartList.clear();
                }else {
                    shopingCartList = new ArrayList<>();
                }
               shopingCartList = (ArrayList<TableGoodsDetailBean>) data.getSerializableExtra(ConstantUtils.INTENT_KEY_SHOPING_CART_LIST);
                if(shopingCartList.size()>0){
                    int totalShopingNum =0;
                    for (TableGoodsDetailBean cartBean :shopingCartList){
                        totalShopingNum += cartBean.getAddGoodsCount();
                    }
                    if (totalShopingNum > 0) {
                        tvSumShopNum.setVisibility(View.VISIBLE);
                        tvSumShopNum.setText(String.valueOf(totalShopingNum));
                    }else {
                        tvSumShopNum.setVisibility(View.GONE);
                    }
                    //updateProListData(shopingCartList);
                    for (TableGoodsDetailBean bean:shopingCartList){
                        if(!bean.getGoodsName().contains("无码商品"))
                        bean.insert(true,TableGoodsDetailBean.COLUMN_GOODS_ID,bean.getGoodsId());
                    }
                    if(cartPopWindow!=null){
                        cartPopWindow.setGoodsListData(shopingCartList);
                    }

//                    else {
//                        cartPopWindow = new ShopingCartPopWindow(ProOrderActivity.this);
//                        cartPopWindow.showPopupWindow(viewBD);
//                        cartPopWindow.setGoodsListData(shopingCartList);
//                    }
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tvNavRight:
                intent = new Intent(ProOrderActivity.this, ScanPayActivity.class);
                intent.putExtra(ConstantUtils.INTENT_KEY_PAY_TYPE, ConstantUtils.PAY_TYPE_SCAN_PRO);
                if(shopingCartList==null){
                    shopingCartList = new ArrayList<>();
                }
                intent.putExtra(ConstantUtils.INTENT_KEY_SHOPING_CART_LIST,(Serializable)shopingCartList);
                startActivity(intent);

                break;
            case R.id.tvNavLeft:
                NoBarCodeCashierDialog noBarCodeCashierDialog = new NoBarCodeCashierDialog(this,R.style.DefaultDialog,this);
                noBarCodeCashierDialog.show();
//                intent = new Intent(ProOrderActivity.this, ScanPayActivity.class);
//                intent.putExtra(ConstantUtils.INTENT_KEY_PAY_TYPE, ConstantUtils.PAY_TYPE_ALIPAY);
//                intent.putExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY, 100.00f);
//                startActivity(intent);
                break;
            case R.id.ivShopingCart://去购物车
                updatShopingCartList4DB();
                if(shopingCartList!=null&&shopingCartList.size()>0&& checkAddGoodsCount(shopingCartList)){
                     cartPopWindow = new ShopingCartPopWindow(this);
                    cartPopWindow.setGoodsListData(shopingCartList);
                    cartPopWindow.showPopupWindow(viewBD);
                    cartPopWindow.setCartListener(this);
                    ivShopingCart.setVisibility(View.GONE);
                    tvSumShopNum.setVisibility(View.GONE);
                }
                break;
            case R.id.tvOrderToPay://去结算
                updatShopingCartList4DB();
                if(shopingCartList.size()>0& checkAddGoodsCount(shopingCartList)){
                    intent = new Intent(this,StatementsActivity.class);
                    intent.putExtra(ConstantUtils.INTENT_KEY_SHOPING_CART_LIST,(Serializable)shopingCartList);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"您的购物车里没有商品，请添加后再结算！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean checkAddGoodsCount(ArrayList<TableGoodsDetailBean> shopingCartList) {
        int addGoodsCount =0;
        ArrayList<TableGoodsDetailBean> tempList = new ArrayList<>();
        tempList.addAll(shopingCartList);
        for(TableGoodsDetailBean bean :shopingCartList){
            addGoodsCount+= bean.getAddGoodsCount();
            if(bean.getAddGoodsCount()==0){
                tempList.remove(bean);
            }
        }
        shopingCartList.clear();

        if(addGoodsCount>0){
            shopingCartList.addAll(tempList);
            return true;
        }
        tempList.clear();
        return false;
    }

    private void updatShopingCartList4DB() {
        if(shopingCartList==null){
            shopingCartList = new ArrayList<>();
        }else {
            shopingCartList.clear();
        }
        ArrayList<TableGoodsDetailBean> beenList =  new TableGoodsDetailBean().query(null,null,null,null,null,null);
        if(beenList!=null&&beenList.size()>0){
            for (TableGoodsDetailBean bean :beenList){
                if(bean.getAddGoodsCount()>0){
                    shopingCartList.add(bean);
                }
            }
        }
        if(noBarcodeCashierList!=null&&noBarcodeCashierList.size()>0){
            shopingCartList.addAll(noBarcodeCashierList);
        }
        shopingCartSort();
    }

    private void shopingCartSort() {
        ArrayList<TableGoodsDetailBean> templistData = new ArrayList<>();
        templistData.addAll(shopingCartList);
        if(templistData.size()>0){

                Comparator comp = new Comparator() {
                    public int compare(Object o1, Object o2) {
                        TableGoodsDetailBean p1 = (TableGoodsDetailBean) o1;
                        TableGoodsDetailBean p2 = (TableGoodsDetailBean) o2;
//                        if (p1.getAddGoods2CartTime() < p2.get proList.get(position).setAddGoods2CartTime(System.currentTimeMillis());Goods2CartTime())
//                            return -1;
//                        else if (p1.getAddGoods2CartTime() == p2.getAddGoods2CartTime())
//                            return 0;
//                        else if (p1.getAddGoods2CartTime() > p2.getAddGoods2CartTime())
//                            return 1;
                        return (int)(p2.getAddGoods2CartTime() - p1.getAddGoods2CartTime());
                    }
                };
                Collections.sort(templistData, comp);
                if(shopingCartList.size()>0){
                    shopingCartList.clear();
                }
                for (TableGoodsDetailBean bean : templistData){
                    shopingCartList.add(bean);
                }
                templistData.clear();
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
    @Override
    public void addProOnClick(int position, TableGoodsDetailBean bean) {
        int goodsCount = bean.getAddGoodsCount();
        goodsCount+=1;
        if(proList!=null&&proList.size()>0){
            proList.get(position).setAddGoods2CartTime(System.currentTimeMillis());
            proList.get(position).setAddGoodsCount(goodsCount);
        }
        proListAdapter.setProductListData(proList);
        proListAdapter.notifyDataSetChanged();
        bean.setAddGoodsCount(goodsCount);
        bean.setAddGoods2CartTime(System.currentTimeMillis());
        bean.insert(true,TableGoodsDetailBean.COLUMN_GOODS_ID,bean.getGoodsId());
        updateCartCount4DB();
        updatShopingCartList4DB();
//        setShopingListSumCount(bean);

    }

    @Override
    public void reduceProOnClick(int position, TableGoodsDetailBean bean) {
        if(bean.getAddGoodsCount()>0){
            int goodsCount = bean.getAddGoodsCount();
            goodsCount-=1;

            if(proList!=null&&proList.size()>0){
                proList.get(position).setAddGoods2CartTime(System.currentTimeMillis());
                proList.get(position).setAddGoodsCount(goodsCount);
            }
            proListAdapter.setProductListData(proList);
            proListAdapter.notifyDataSetChanged();
            bean.setAddGoodsCount(goodsCount);
            bean.setAddGoods2CartTime(System.currentTimeMillis());
            bean.insert(true,TableGoodsDetailBean.COLUMN_GOODS_ID,bean.getGoodsId());
            updateCartCount4DB();
            updatShopingCartList4DB();
           // setShopingListSumCount(bean);
        }
    }

    @Override
    public void categoryItemOnClick(int position, String categoryId, String categoryName) {
        currentCName = categoryName;
        categoryListAdapter.setCurrentPosition(position);
        categoryListAdapter.notifyDataSetInvalidated();
        hideSoftInputFromWindow();
        getGoodsDataFromDB(categoryName);

    }

    @Override
    public void onCartWindowDismiss() {
//        showCart();
        ivShopingCart.setVisibility(View.VISIBLE);
        updateCartCount4DB();
        updateTotalMoneyShow();
    }


    @Override
    public void cartItemAddOnClick(int position, TableGoodsDetailBean bean) {
        if(shopingCartList!=null&&shopingCartList.size()>0 ){
            shopingCartList.get(position).setAddGoodsCount(bean.getAddGoodsCount());
            cartPopWindow.setGoodsListData(shopingCartList);
        }
        if(bean.getGoodsName().contains("无码商品")){
            updateNoBarcodeCashierListData(bean);
        }else {
            bean.insert(true,TableGoodsDetailBean.COLUMN_GOODS_ID,bean.getGoodsId());
        }
        updateTotalMoneyShow();
//        updateCartCount4DB();
        //updateProListData(bean);
    }

    @Override
    public void cartItemReduceOnClick(int position, TableGoodsDetailBean bean) {
        if(shopingCartList!=null&&shopingCartList.size()>0){
            if(bean.getAddGoodsCount()>0){
                shopingCartList.get(position).setAddGoodsCount(bean.getAddGoodsCount());
            }else {
                shopingCartList.remove(position);
                if(shopingCartList.size()==0){
                    cartPopWindow.dismiss();
                }
            }
            cartPopWindow.setGoodsListData(shopingCartList);
        }else {
            cartPopWindow.dismiss();
        }
        if(bean.getGoodsName().contains("无码商品")){
            updateNoBarcodeCashierListData(bean);
        }else {
            bean.insert(true,TableGoodsDetailBean.COLUMN_GOODS_ID,bean.getGoodsId());
        }
        updateTotalMoneyShow();
        // updateProListData(bean);
//        updateCartCount4DB();
    }

    private void updateNoBarcodeCashierListData(TableGoodsDetailBean bean) {
        ConcurrentHashMap<String,TableGoodsDetailBean> goodsMap = new ConcurrentHashMap<String,TableGoodsDetailBean>();
        if(noBarcodeCashierList!=null&&noBarcodeCashierList.size()>0){
            if(bean.getAddGoodsCount()>0){
                noBarcodeCashierList.add(bean);
            }
            for (TableGoodsDetailBean goodsDetailBean:noBarcodeCashierList){
                goodsMap.put(goodsDetailBean.getGoodsId(),goodsDetailBean);
            }
            noBarcodeCashierList.clear();
            for (Map.Entry<String, TableGoodsDetailBean> entry : goodsMap.entrySet()) {
                noBarcodeCashierList.add(entry.getValue());

            }
            goodsMap.clear();
        }
//        if(noBarcodeCashierList!=null&&noBarcodeCashierList.size()>0){
//            ArrayList<TableGoodsDetailBean> tempList = new ArrayList<>();
//            for (TableGoodsDetailBean goodsDetailBean:noBarcodeCashierList){
//                if(goodsDetailBean.getGoodsId().equals(bean.getGoodsId())){
//                    goodsDetailBean.setAddGoodsCount(bean.getAddGoodsCount());
//                    tempList.add(goodsDetailBean);
//                }else {
//                    tempList.add(goodsDetailBean);
//                }
//            }
//            noBarcodeCashierList.clear();
//            noBarcodeCashierList.addAll(tempList);
//            tempList.clear();
//        }
    }

    @Override
    public void clearCartList() {
        if(shopingCartList!=null&&shopingCartList.size()>0){
            shopingCartList.clear();
            clearAddGoodsCount4DB();
            updateCartCount4DB();
            //updateProListData(new TableGoodsDetailBean());
        }
    }
private void updateCartCount4DB(){
    Cursor cursor =  new TableGoodsDetailBean().queryCursor(new String[]{TableGoodsDetailBean.COLUMN_GOODS_ADD_COUNT,TableGoodsDetailBean.COLUMN_GOODS_PRICE},null,null,null,null,null);
    int totalGoodsCount =0;
    double totalGoodsPrice = 0.00;
    if(cursor!=null){
        try {

            while (cursor.moveToNext()) {
              int index =  cursor.getColumnIndex(TableGoodsDetailBean.COLUMN_GOODS_ADD_COUNT);
               int goodsCount =  cursor.getInt(index);
                totalGoodsCount+=goodsCount;
                int pIndex =  cursor.getColumnIndex(TableGoodsDetailBean.COLUMN_GOODS_PRICE);
                double price = cursor.getDouble(pIndex);
                if(goodsCount>0){
                    totalGoodsPrice += goodsCount*price;
                }
            }

        } catch (Exception e) {
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    if(noBarcodeCashierList!=null&&noBarcodeCashierList.size()>0)
    {
        for(TableGoodsDetailBean bean:noBarcodeCashierList){
            totalGoodsCount+=bean.getAddGoodsCount();
            totalGoodsPrice += bean.goodsPrice;
        }
    }
    tvOrderTotalMoney.setText(String.format("合计：￥%s",String.format("%.2f",totalGoodsPrice)));
    if (totalGoodsCount > 0) {
        tvSumShopNum.setVisibility(View.VISIBLE);
        tvSumShopNum.setText(String.valueOf(totalGoodsCount));
    }else {
        tvSumShopNum.setVisibility(View.GONE);
    }



}
    private void clearAddGoodsCount4DB() {
        ArrayList<TableGoodsDetailBean> beenList =  new TableGoodsDetailBean().query(null,null,null,null,null,null);
        if(beenList!=null &&beenList.size()>0){
            for (TableGoodsDetailBean bean:beenList){
                bean.setAddGoodsCount(0);
                bean.setAddGoods2CartTime(-1);
                bean.insert(true,TableGoodsDetailBean.COLUMN_GOODS_ID,bean.getGoodsId());
            }
        }
        if(noBarcodeCashierList!=null&&noBarcodeCashierList.size()>0){
            noBarcodeCashierList.clear();
        }
        tvOrderTotalMoney.setText(String.format("合计：￥%s",0.00));
    }
    private ArrayList<TableGoodsDetailBean> noBarcodeCashierList = new ArrayList<>();
    int noBarcodeCount=200;
    @Override
    public void noBarcodeCashierAddShoping2Cart(String price) {
        noBarcodeCount+=1;
        String goodsName = "无码商品"+noBarcodeCount;
        String categoryName = "无码商品";
        TableGoodsDetailBean bean = new TableGoodsDetailBean();
        bean.setAddGoodsCount(1);
        bean.setGoodsPrice(Double.parseDouble(price));
        bean.setUserId(Utils.get6MD5WithString("18501053570"));
        bean.setGoodsName(goodsName);
        bean.setGoodsId(goodsName);
        bean.setGoodsImgUrl("abc");
        bean.setGoodsCreateTime(System.currentTimeMillis());
        bean.setGoodsCid(Utils.get6MD5WithString(categoryName));
        bean.setGoodsCName(categoryName);
        bean.setAddGoods2CartTime(System.currentTimeMillis());
        noBarcodeCashierList.add(bean);
        int totalGoodsCount = Integer.parseInt(tvSumShopNum.getText().toString().trim());
        totalGoodsCount+=1;
        if (totalGoodsCount > 0) {
            tvSumShopNum.setVisibility(View.VISIBLE);
            tvSumShopNum.setText(String.valueOf(totalGoodsCount));
        }else {
            tvSumShopNum.setVisibility(View.GONE);
        }
        shopingCartList.addAll(noBarcodeCashierList);
        shopingCartSort();
        updateTotalMoneyShow();
    }

    private void updateTotalMoneyShow() {
        double totalMoney=0;
        ConcurrentHashMap<String,TableGoodsDetailBean> goodsMap = new ConcurrentHashMap<String,TableGoodsDetailBean>();
        if(shopingCartList!=null&&shopingCartList.size()>0){
            if(noBarcodeCashierList!=null&&noBarcodeCashierList.size()>0){
                shopingCartList.addAll(noBarcodeCashierList);
            }
            for (TableGoodsDetailBean goodsDetailBean:shopingCartList){
                goodsMap.put(goodsDetailBean.getGoodsId(),goodsDetailBean);
            }
            shopingCartList.clear();
            for (Map.Entry<String, TableGoodsDetailBean> entry : goodsMap.entrySet()) {
                shopingCartList.add(entry.getValue());
                totalMoney+= entry.getValue().getAddGoodsCount() * entry.getValue().getGoodsPrice();

            }
            goodsMap.clear();
        }
        if(totalMoney>0){
            ivShopingCart.setBackgroundResource(R.drawable.ic_cart_have_shoping);
        }else {
            ivShopingCart.setBackgroundResource(R.drawable.ic_cart_no_shoping);
        }
        tvOrderTotalMoney.setText(String.format("合计：￥%s",String.format("%.2f",totalMoney)));
    }
}
