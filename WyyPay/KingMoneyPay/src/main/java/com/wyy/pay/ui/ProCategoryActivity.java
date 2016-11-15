package com.wyy.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.adapter.OrderCategoryListAdapter;
import com.wyy.pay.adapter.ProCategoryManageListAdapter;
import com.wyy.pay.bean.ProCategoryBean;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class ProCategoryActivity extends BaseActivity implements View.OnClickListener, ProCategoryManageListAdapter.CManageItemOnClickListener {
    private XListView categoryListView;
    private List<ProCategoryBean> proCategoryList;//商品分类list
    private Button btnAddCategory;//新增分类按钮
    private ProCategoryManageListAdapter adapter;
    private boolean isEdit =false;
    private boolean isCanClick=false;//item 是否可点击
    private List<ProCategoryBean> beanList;
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
         beanList = new ArrayList<>();

        for(int i=0;i<14;i++){
            if(i==0){
                ProCategoryBean  bean = new ProCategoryBean();
                bean.setCategoryName("默认分类");
                bean.setCategoryId(Utils.get6MD5WithString("默认分类"));
                beanList.add(bean);
            }else{

                ProCategoryBean  bean = new ProCategoryBean();
                bean.setCategoryName("分类"+i);
                bean.setCategoryId(Utils.get6MD5WithString("分类"+i));
                beanList.add(bean);
            }
        }
        adapter.setCategoryListData(beanList);
        categoryListView.setAdapter(adapter);
        adapter.setCManageItemOnClickListener(this);

    }

    @Override
    public void initListener() {
        tvNavRight.setOnClickListener(this);
        tvNavLeft.setOnClickListener(this);
        btnAddCategory.setOnClickListener(this);
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
                Toast.makeText(this, "新增分类", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onItemViewClick(int position, ProCategoryBean bean) {
        if(isCanClick){
            Intent intent = new Intent();
            intent.putExtra(ConstantUtils.INTENT_KEY_PRODUCT_CATEGORY,bean.getCategoryName());
            setResult(RESULT_OK,intent);
            this.finish();
        }
    }

    @Override
    public void btnDeleteOnClick(int position, ProCategoryBean bean) {
        if(bean==null){
            return;
        }
        if(beanList!=null&&beanList.size()>0){
            for(int i=0;i<beanList.size();i++){
                ProCategoryBean cBean = beanList.get(i);
                if(bean.getCategoryName().equals(cBean.getCategoryName())){
                    beanList.remove(i);
                    break;
                }
            }
            if(adapter==null){
                adapter = new ProCategoryManageListAdapter(ProCategoryActivity.this);
            }
            adapter.setCategoryListData(new ArrayList<ProCategoryBean>());
            adapter.notifyDataSetChanged();
            adapter.setCategoryListData(beanList);
            adapter.notifyDataSetChanged();
            //更新数据库
        }

    }

    @Override
    public void btnEditOnClick(int position, ProCategoryBean bean) {
        //弹出框ß
    }
}
