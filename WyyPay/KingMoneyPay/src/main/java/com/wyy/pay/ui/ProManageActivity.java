package com.wyy.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.adapter.ProCategoryListAdapter;
import com.wyy.pay.adapter.ProductListAdapter;
import com.wyy.pay.bean.TableCategoryBean;
import com.wyy.pay.bean.TableGoodsDetailBean;
import com.wyy.pay.ui.dialog.CustomProgressDialog;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.XListView;

import java.util.ArrayList;
import java.util.List;

import db.utils.BaseDbBean;
import db.utils.TableDataListener;


public class ProManageActivity extends BaseActivity implements View.OnClickListener, ProManagePopWindow.ProMpopWindowOnClickListener, AdapterView.OnItemClickListener, ProductListAdapter.ProductItemOnClickListener {
    private LinearLayout llProManageBottom;
    private TextView tvProMcancel;
    private TextView tvProMcomplete;
    private TextView tvProMDelete;
    private XListView categoryListView;
    private XListView proListView;
    private ProCategoryListAdapter categoryListAdapter;
    private ProductListAdapter mProductListAdapter;
    private List<TableGoodsDetailBean> proList;
    private TableDataListener<TableGoodsDetailBean> dataListener;
    private CustomProgressDialog initDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_product_manage);
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();

    }
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if(categoryListView != null){
//            categoryListView.requestFocus();
//            categoryListView.setItemChecked(0, true);
//            categoryListView.smoothScrollToPosition(0);
//            categoryListView.setSelection(0);
//        }
//    }
    @Override
    public void initView() {
        tvNavLeft.setText("订单");
        tvNavTitle.setText("商品管理");
        tvNavRight.setText("更多");
        llProManageBottom = (LinearLayout) findViewById(R.id.llProManageBottom);
        tvProMcancel = (TextView) findViewById(R.id.tvProMcancel);
        tvProMcomplete = (TextView) findViewById(R.id.tvProMcomplete);
        tvProMDelete = (TextView) findViewById(R.id.tvProMDelete);
        categoryListView = (XListView) findViewById(R.id.categoryList);
        categoryListView.setPullLoadEnable(false);
        categoryListView.setPullRefreshEnable(false);
        categoryListView.setOnItemClickListener(this);

        proListView = (XListView) findViewById(R.id.proListView);
        proListView.setPullLoadEnable(true);
        proListView.setPullRefreshEnable(true);

    }
    private String currentCName ="默认分类";//当前分类名称
    private static final int TABLE_CATEGORY_CHANGED = 0;
    private static final int INIT_COMPLETE = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TABLE_CATEGORY_CHANGED:
                    getDataFromDB(currentCName);
                    break;
                case INIT_COMPLETE:
                    if(initDialog!=null){
                        initDialog.dismiss();
                    }
                    if(proList==null){
                        proList = new ArrayList<>();
                        //显示商品数据为空界面
                    }
                    mProductListAdapter.setProductListData(proList);
                    mProductListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    /**
     * 从数据库获取数据
     * @param goodsCname 商品分类名称
     */
    private void getDataFromDB(final String goodsCname) {
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
                handler.sendEmptyMessage(INIT_COMPLETE);
            }
        }).start();
    }

    @Override
    public void initData() {
         categoryListAdapter = new ProCategoryListAdapter(this);
        TableCategoryBean tableCategoryBean = new TableCategoryBean();
        String orderBy = TableCategoryBean.COLUMN_CREATE_CATEGORY_TIEM+" ASC";
        List<TableCategoryBean> beanList =  tableCategoryBean.query(null,TableCategoryBean.COLUMN_USER_ID +"=?",new String[]{Utils.get6MD5WithString("18501053570")},null,null,orderBy);

        categoryListAdapter.setCategoryListData(beanList);
        categoryListView.setAdapter(categoryListAdapter);
        categoryListView.setItemChecked(1,true);
        categoryListView.setActivated(true);
        categoryListView.setFocusable(true);
        categoryListView.requestFocusFromTouch();
        categoryListView.performItemClick(categoryListView.getAdapter().getView(1,null,null),1,1);
        categoryListView.setSelection(1);

        mProductListAdapter = new ProductListAdapter(this);
        proList = new ArrayList<TableGoodsDetailBean>();
        getDataFromDB(currentCName);

//        for (int i = 0; i < 10; i++) {
//            TableGoodsDetailBean bean = new TableGoodsDetailBean();
//            bean.setCategoryId("categoryId" + i);
//            if (i<5) {
//                bean.setProStockCount(10*i);
//            } else {
//                bean.setProStockCount(20*i);
//            }
//
//            bean.setImgUrl("http://www.baidu.com");
//            bean.setProName("商品" + i);
//            bean.setProNo("No" + i * 9);
//            bean.setProPrice(18.00);
//            bean.setCategoryName("商品分类"+i);
//            proList.add(bean);
//        }
//        mProductListAdapter.setProductListData(proList);
//        proListView.setAdapter(mProductListAdapter);
        mProductListAdapter.setProductItemOnClickListener(this);
}

    @Override
    protected void onDestroy() {
        BaseDbBean.unregisterContentObserver(TableGoodsDetailBean.TABLE_NAME,
                dataListener);
        super.onDestroy();
    }

    @Override
    public void initListener() {
        tvNavLeft.setOnClickListener(this);
        tvNavRight.setOnClickListener(this);
        tvProMcancel.setOnClickListener(this);
        tvProMcomplete.setOnClickListener(this);
        tvProMDelete.setOnClickListener(this);
        dataListener = new TableDataListener<TableGoodsDetailBean>(handler) {
            @Override
            public void onDataChanged(int type, TableGoodsDetailBean obj) {
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
        TableCategoryBean.registerContentObserver(TableGoodsDetailBean.TABLE_NAME,dataListener);

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
            case R.id.tvProMcancel:
                llProManageBottom.setVisibility(View.GONE);
                break;
            case R.id.tvProMcomplete:
                llProManageBottom.setVisibility(View.GONE);
                break;
            case R.id.tvProMDelete:
                llProManageBottom.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void tvAddProOnClick() {
        Intent intent = new Intent(this,ProDetailActivity.class);
        intent.putExtra(ConstantUtils.INTENT_KEY_FROM_ACTIVITY_TYPE,ConstantUtils.FROM_POPUP_WINDOW_ADD_PRODUCT);
        startActivity(intent);

    }

    @Override
    public void tvEditProOnClick() {
        llProManageBottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void tvCategoryManageOnClick() {
        Intent intent = new Intent(this,ProCategoryActivity.class);
        intent.putExtra(ConstantUtils.INTENT_KEY_FROM_ACTIVITY_TYPE,ConstantUtils.FROM_POPUP_WINDOW_CATEGORY_MANAGE);
        startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,"position=="+position,Toast.LENGTH_SHORT).show();
        categoryListAdapter.setCurrentPosition(position);
        categoryListAdapter.notifyDataSetInvalidated();

    }


    @Override
    public void onItemClick(int position, TableGoodsDetailBean bean) {
        Intent intent = new Intent(this,ProDetailActivity.class);
        intent.putExtra(ConstantUtils.INTENT_KEY_FROM_ACTIVITY_TYPE,ConstantUtils.FROM_PRODUCT_MANAGE_ACTIVITY_PRODUCT_LIST_ITEM);
        intent.putExtra(ConstantUtils.INTENT_KEY_PRODUCT_BEAN,bean);
        startActivity(intent);
    }
}
