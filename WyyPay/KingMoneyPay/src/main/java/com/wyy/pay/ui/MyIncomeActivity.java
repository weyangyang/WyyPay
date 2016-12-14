package com.wyy.pay.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.wyy.pay.R;

public class MyIncomeActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_my_income);
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
	}
	@Override
	public void initView() {
		tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
		tvNavTitle.setText("我的收入");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initListener() {
		tvNavLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyIncomeActivity.this.finish();
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


}