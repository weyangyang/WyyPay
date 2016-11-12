package com.wyy.pay.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.adapter.ProCategoryListAdapter;
import com.wyy.pay.bean.ProCategoryBean;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.XListView;

import java.util.ArrayList;
import java.util.List;


public class ProManageActivity extends BaseActivity implements View.OnClickListener, ProManagePopWindow.ProMpopWindowOnClickListener, AdapterView.OnItemClickListener {
    private LinearLayout llProManageBottom;
    private TextView tvProMcancel;
    private TextView tvProMcomplete;
    private TextView tvProMDelete;
    private XListView categoryListView;
    private ProCategoryListAdapter categoryListAdapter;

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
        llProManageBottom = (LinearLayout) findViewById(R.id.llProManageBottom);
        tvProMcancel = (TextView) findViewById(R.id.tvProMcancel);
        tvProMcomplete = (TextView) findViewById(R.id.tvProMcomplete);
        tvProMDelete = (TextView) findViewById(R.id.tvProMDelete);
        categoryListView = (XListView) findViewById(R.id.categoryList);
        categoryListView.setPullLoadEnable(false);
        categoryListView.setPullRefreshEnable(false);
        categoryListView.setOnItemClickListener(this);

    }

    @Override
    public void initData() {
         categoryListAdapter = new ProCategoryListAdapter(this);
        List<ProCategoryBean> beanList = new ArrayList<ProCategoryBean>();
        for (int i=0;i<6;i++){
            ProCategoryBean bean = new ProCategoryBean();
            String categoryName = "默认分类"+i;
            bean.setCategoryId(Utils.get6MD5WithString(categoryName));
            bean.setCategoryName(categoryName);
            beanList.add(bean);
        }
        categoryListAdapter.setCategoryListData(beanList);
        categoryListView.setAdapter(categoryListAdapter);
}

    @Override
    public void initListener() {
        tvNavLeft.setOnClickListener(this);
        tvNavRight.setOnClickListener(this);
        tvProMcancel.setOnClickListener(this);
        tvProMcomplete.setOnClickListener(this);
        tvProMDelete.setOnClickListener(this);

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
        Toast.makeText(this,"11",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void tvEditProOnClick() {
        llProManageBottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void tvAddCategoryOnClick() {
        Toast.makeText(this,"13",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void tvEditCategoryOnClick() {
        Toast.makeText(this,"14",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,"position=="+position,Toast.LENGTH_SHORT).show();
        categoryListAdapter.setCurrentPosition(position);

    }


}
