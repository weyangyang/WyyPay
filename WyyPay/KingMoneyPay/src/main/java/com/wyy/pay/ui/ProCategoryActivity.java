package com.wyy.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.adapter.ProCategoryManageListAdapter;
import com.wyy.pay.bean.TableCategoryBean;
import com.wyy.pay.ui.dialog.CustomProgressDialog;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.XListView;

import java.util.ArrayList;
import java.util.List;

import db.utils.TableDataListener;

public class ProCategoryActivity extends BaseActivity implements View.OnClickListener, ProCategoryManageListAdapter.CManageItemOnClickListener, AddCategoryPopWindow.AddCgOnClickListener {
    private XListView categoryListView;
    private List<TableCategoryBean> proCategoryList;//商品分类list
    private Button btnAddCategory;//新增分类按钮
    private ProCategoryManageListAdapter adapter;
    private boolean isEdit =false;
    private boolean isCanClick=false;//item 是否可点击
    private List<TableCategoryBean> beanList;
    private TableDataListener<TableCategoryBean> dataListener;
    private  CustomProgressDialog initDialog;
    private static final int ADD_NEW_CATEGORY =11;
    private static final int EDIT_CATEGORY =12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pro_category);
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        tvNavTitle.setText("分类管理");
        tvNavRight.setText("编辑");
        tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
        categoryListView = (XListView) findViewById(R.id.categoryMainListView);
        btnAddCategory = (Button) findViewById(R.id.btnAddCategory);
        categoryListView.setPullLoadEnable(false);
        categoryListView.setPullRefreshEnable(false);
    }

    @Override
    public void initData() {
        Intent intent= getIntent();
        //intent.putExtra(ConstantUtils.INTENT_KEY_FROM_ACTIVITY_TYPE,ConstantUtils.FROM_PRO_DETAIL_ACTIVITY);
        int type = intent.getIntExtra(ConstantUtils.INTENT_KEY_FROM_ACTIVITY_TYPE,0);
        if(ConstantUtils.FROM_PRO_DETAIL_ACTIVITY ==type){
            isCanClick = true;
        }
        adapter = new ProCategoryManageListAdapter(this);
        getDataFromDB();
        adapter.setCategoryListData(beanList);
        categoryListView.setAdapter(adapter);
        adapter.setCManageItemOnClickListener(this);

    }
    private static final int TABLE_CATEGORY_CHANGED = 0;
    private static final int INIT_COMPLETE = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TABLE_CATEGORY_CHANGED:
                    getDataFromDB();
                    break;
                case INIT_COMPLETE:
                    if(initDialog!=null){
                        initDialog.dismiss();
                    }
                    if(beanList==null){
                        beanList = new ArrayList<>();
                    }
                    adapter.setCategoryListData(beanList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    @Override
    public void initListener() {
        tvNavRight.setOnClickListener(this);
        tvNavLeft.setOnClickListener(this);
        btnAddCategory.setOnClickListener(this);
        dataListener = new TableDataListener<TableCategoryBean>(handler) {
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
        TableCategoryBean.registerContentObserver(TableCategoryBean.TABLE_NAME,dataListener);
    }

    public synchronized void getDataFromDB() {
        if (initDialog == null) {
            initDialog = CustomProgressDialog.createLoadingDialog(this);
        }
        if (!initDialog.isShowing()) {
            initDialog.show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                TableCategoryBean bean = new TableCategoryBean();
                String orderBy = TableCategoryBean.COLUMN_CREATE_CATEGORY_TIEM+" ASC";
                beanList =  bean.query(null,TableCategoryBean.COLUMN_USER_ID +"=?",new String[]{Utils.get6MD5WithString("18501053570")},null,null,orderBy);
                handler.sendEmptyMessage(INIT_COMPLETE);
            }
        }).start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNavRight://编辑
                isEdit = !isEdit;
                if(isEdit){
                    tvNavRight.setText("完成");
                }else {
                    tvNavRight.setText("编辑");
                }
                if(adapter!=null){
                    adapter.setEdit(isEdit);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.tvNavLeft:
               ProCategoryActivity.this.finish();
                break;
            case R.id.btnAddCategory://新增分类
                addCategoryPopWindow(ADD_NEW_CATEGORY);
                break;
        }
    }

    private void addCategoryPopWindow(int fromType) {
        AddCategoryPopWindow popupWindow = new AddCategoryPopWindow(ProCategoryActivity.this,fromType);
        popupWindow.showPopupWindow(btnAddCategory);
        popupWindow.setAddCgOnClickListener(this);
    }


    @Override
    public void onItemViewClick(int position, TableCategoryBean bean) {
        if(isCanClick&&!isEdit){
            Intent intent = new Intent();
            intent.putExtra(ConstantUtils.INTENT_KEY_PRODUCT_CATEGORY,bean.getCategoryName());
            setResult(RESULT_OK,intent);
            this.finish();
        }
    }

    @Override
    public void btnDeleteOnClick(int position, TableCategoryBean bean) {
        if(bean==null){
            return;
        }
        if(beanList!=null&&beanList.size()>0){
            beanList.remove(position);
            if(adapter==null){
                adapter = new ProCategoryManageListAdapter(ProCategoryActivity.this);
            }
            adapter.setCategoryListData(beanList);
            adapter.notifyDataSetChanged();
            //更新数据库
            bean.rawDelete(TableCategoryBean.COLUMN_USER_ID+" =? AND " +TableCategoryBean.COLUMN_CATEGORY_ID+" =?",new String[]{Utils.get6MD5WithString("18501053570"),Utils.get6MD5WithString(bean.getCategoryName())});
        }

    }
    private String oldCategoryName;//编辑前的某item分类名称
    @Override
    public void btnEditOnClick(int position, TableCategoryBean bean) {
        //弹出框ß
        this.oldCategoryName = bean.getCategoryName();
        addCategoryPopWindow(EDIT_CATEGORY);
    }

    @Override
    public void btnOKOnClick(String categoryName,int fromType) {
        if (TextUtils.isEmpty(categoryName)) {
            return;
        }
        TableCategoryBean bean =null;
        boolean isSucc;
        switch (fromType){
            case ADD_NEW_CATEGORY:
                 bean = getTableCategoryBean(categoryName);
                 isSucc = bean.insert(true,TableCategoryBean.COLUMN_CATEGORY_ID,Utils.get6MD5WithString(categoryName));
                if(isSucc){
                    Toast.makeText(this,ProCategoryActivity.this.getString(R.string.text_add_category_name_succ),Toast.LENGTH_SHORT).show();
                }
                break;
            case EDIT_CATEGORY:
                String oldName = oldCategoryName;
                bean = getTableCategoryBean(categoryName);
                isSucc =  bean.insert(true,TableCategoryBean.COLUMN_CATEGORY_ID,Utils.get6MD5WithString(oldName));
                if(isSucc){
                    Toast.makeText(this,ProCategoryActivity.this.getString(R.string.text_update_category_name_succ),Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isEdit){
                isEdit = !isEdit;
                    tvNavRight.setText("编辑");
                if(adapter!=null){
                    adapter.setEdit(isEdit);
                    adapter.notifyDataSetChanged();
                }
               return true;
            }
            ProCategoryActivity.this.finish();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @NonNull
    private TableCategoryBean getTableCategoryBean(String categoryName) {
        TableCategoryBean bean = new TableCategoryBean();
        bean.setUserId(Utils.get6MD5WithString("18501053570"));
        bean.setCreateTime(System.currentTimeMillis());
        bean.setCategoryName(categoryName);
        bean.setCategoryId(Utils.get6MD5WithString(categoryName));
        return bean;
    }
}
