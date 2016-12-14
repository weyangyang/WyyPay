package com.wyy.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.adapter.RCorderListAdapter;
import com.wyy.pay.bean.OrderListDataBean;
import com.wyy.pay.bean.TableOrderListBean;
import com.wyy.pay.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderListActivity extends BaseActivity implements OrderListFilterPopWindow.FilterOnClickListener {
	private RecyclerView rcOrderListView;
	private RCorderListAdapter adapter;
	private ArrayList<OrderListDataBean> dataList;
	private ArrayList<TableOrderListBean> tableOrderList;
	public static final int SELECT_ALL= 100;
	public static final int TYPE_WEIXIN= 1;
	public static final int TYPE_ALIPAY= 2;
	public static final int TYPE_CASH= 3;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_order_list);
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
	}
	@Override
	public void initView() {
		rcOrderListView = (RecyclerView) findViewById(R.id.rcOrderListView);
		adapter = new RCorderListAdapter(this);
		rcOrderListView.setLayoutManager(new LinearLayoutManager(this));
		rcOrderListView.setAdapter(adapter);
		tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
		tvNavTitle.setText("账单流水");
		tvNavRight.setText("筛选");
	}

	@Override
	public void initData() {
		dataList = new ArrayList<>();
//		if(Utils.checkUserIsLogin(this)){
//			TableOrderListBean tableBean = new TableOrderListBean();
//			tableOrderList = tableBean.query(null,TableOrderListBean.COLUMN_USER_ID,new String[]{Utils.get6MD5WithString(BaseApplication.getUserName())},null,null,null);
//		}else {
//			Intent intent = new Intent(this,LoginActivity.class);
//			startActivity(intent);
//		}
		tableOrderList = new ArrayList<>();
		for (int i=1;i<25;i++){
			TableOrderListBean bean = new TableOrderListBean();
			if(i<7){
				bean.setOrderNo("22332234"+i);
				bean.setCreateTime("0"+i+":12:1"+i);
				bean.setMoney(31*i);
				bean.setCreateDate("16-12-08");
				bean.setPayType(1);
				tableOrderList.add(bean);
			}
			if(i>7&&i<16){
				bean.setOrderNo("22887634"+i);
				bean.setCreateTime("0"+i+":12:1"+i);
				bean.setMoney(14*i);
				bean.setCreateDate("16-12-16");
				bean.setPayType(2);
				tableOrderList.add(bean);
			}
			if(i>15){
				bean.setOrderNo("228777634"+i);
				bean.setCreateTime("0"+i+":12:1"+i);
				bean.setMoney(84*i);
				bean.setCreateDate("16-12-17");
				bean.setPayType(3);
				tableOrderList.add(bean);
			}
		}
		coverOrderListData(1);
		if(dataList.size()>0){
			adapter.setListData(dataList);
			adapter.notifyDataSetChanged();
		}else {
			Toast.makeText(this,"没有数据",Toast.LENGTH_SHORT).show();
		}

	}

	private void coverOrderListData(int type) {
		if(tableOrderList!=null&& tableOrderList.size()>0){
			HashMap<String,String> titleMap = new HashMap<>();
			for (TableOrderListBean tablelistBean :tableOrderList){
				if(tablelistBean.getPayType() ==type||type==SELECT_ALL){
					titleMap.put(tablelistBean.getCreateDate(),tablelistBean.getCreateDate());
				}
			}
			for (String key : titleMap.keySet()) {
				String createD = titleMap.get(key);
				double tt = 0;
				for (TableOrderListBean tableOrderListBean :tableOrderList){
					if(createD.equals(tableOrderListBean.getCreateDate())){
						tt += tableOrderListBean.getMoney();
					}
				}
				OrderListDataBean dataBean = new OrderListDataBean();
				dataBean.setCreateDate(key);
				dataBean.setTitleStatus(true);
				dataBean.setMoney(tt);
				dataList.add(dataBean);
				tt =0;
				for (TableOrderListBean listBean :tableOrderList){
					if(key.equals(listBean.getCreateDate())){
						coverOrderDataBean(listBean);
					}

				}

			}

		}
	}

	private void coverOrderDataBean(TableOrderListBean tableOrderListBean) {
		OrderListDataBean dataBean = new OrderListDataBean();
		dataBean.setCreateDate(tableOrderListBean.getCreateDate());
		dataBean.setTitleStatus(false);
		dataBean.setCreateTime(tableOrderListBean.getCreateTime());
		dataBean.setOrderNo(tableOrderListBean.getOrderNo());
		dataBean.setMoney(tableOrderListBean.getMoney());
		dataBean.setPayType(tableOrderListBean.getPayType());
		dataList.add(dataBean);
	}

	@Override
	public void initListener() {
		tvNavRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				OrderListFilterPopWindow filterPopWindow = new OrderListFilterPopWindow(OrderListActivity.this);
				filterPopWindow.showPopupWindow(tvNavRight);
				filterPopWindow.setFilterMpopWindowOnClickListener(OrderListActivity.this);
			}
		});
		tvNavLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				OrderListActivity.this.finish();
			}
		});
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onOrderListFilterClick(int type) {
		if(dataList==null){
			dataList = new ArrayList<>();
		}else {
			dataList.clear();
		}
		coverOrderListData(type);
		if(dataList.size()>0){
			adapter.setListData(dataList);
			adapter.notifyDataSetChanged();
		}
	}
}