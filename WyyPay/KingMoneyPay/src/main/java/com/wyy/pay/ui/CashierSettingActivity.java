package com.wyy.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyy.pay.R;

public class CashierSettingActivity extends BaseActivity implements View.OnClickListener {
	private RelativeLayout rlRemoveZeroSetting;
	private RelativeLayout rlDiscountSetting;
	private TextView tvDiscount,tvRemoveZero;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_cashier_setting);
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
	}
	@Override
	public void initView() {
		tvDiscount = (TextView) findViewById(R.id.tvDiscount);
		tvRemoveZero = (TextView) findViewById(R.id.tvRemoveZero);
		rlRemoveZeroSetting = (RelativeLayout) findViewById(R.id.rlRemoveZeroSetting);
		rlDiscountSetting = (RelativeLayout) findViewById(R.id.rlDiscountSetting);
		tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
		tvNavTitle.setText("收银设置");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initListener() {
		rlRemoveZeroSetting.setOnClickListener(this);
		rlDiscountSetting.setOnClickListener(this);
		tvNavLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CashierSettingActivity.this.finish();
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
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.rlDiscountSetting: //优惠折扣设置
				break;
			case R.id.rlRemoveZeroSetting: //抹零设置
				Intent intent = new Intent(this,RemoveZeroSettingActivity.class);
				startActivity(intent);
				break;
		}
	}
}