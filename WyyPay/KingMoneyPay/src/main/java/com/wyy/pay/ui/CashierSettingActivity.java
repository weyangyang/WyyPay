package com.wyy.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyy.pay.R;
import com.wyy.pay.utils.RemoveZeroType;

import xtcore.utils.PreferenceUtils;

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
		updateDiscountNoText();
		updateZeroNoText();
	}

	private void updateDiscountNoText() {
		boolean isDiscountNo = PreferenceUtils.getPrefBoolean(this,PreferenceUtils.SP_DISCOUNT_SWITCH,false);
		if(isDiscountNo){
			tvDiscount.setText("开启");
		}else{
			tvDiscount.setText("关闭");
		}
	}

	private void updateZeroNoText() {
		boolean isZeroNo = PreferenceUtils.getPrefBoolean(this,PreferenceUtils.SP_REMOVE_ZERO_SWITCH,false);
		if(isZeroNo){
			int type = PreferenceUtils.getPrefInt(this,PreferenceUtils.SP_REMOVE_ZERO_TYPE,-1);
			if(type == RemoveZeroType.FILE_TO_JIAO.ordinal()){
				tvRemoveZero.setText(getString(R.string.text_to_jiao));
			}else if(type == RemoveZeroType.REMOVE_FEN.ordinal()){
				tvRemoveZero.setText("抹分");
			}else if(type == RemoveZeroType.REMOVE_JIAO.ordinal()){
				tvRemoveZero.setText("抹角");
			}else {
				tvRemoveZero.setText("开启");
			}

		}else {
			tvRemoveZero.setText("关闭");
		}
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode ==RESULT_OK ){
			switch (requestCode){
				case DISCOUNT_REQUEST_COUNT:
					updateDiscountNoText();
					break;
				case REMOVE_ZERO_REQUEST_COUNT:
					updateZeroNoText();
					break;
			}
		}
	}

	private static final int DISCOUNT_REQUEST_COUNT = 122;
private static final int REMOVE_ZERO_REQUEST_COUNT = 123;
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.rlDiscountSetting: //优惠折扣设置
				Intent intent = new Intent(this,DiscountSettingActivity.class);
				startActivityForResult(intent,DISCOUNT_REQUEST_COUNT);
				break;
			case R.id.rlRemoveZeroSetting: //抹零设置
				 intent = new Intent(this,RemoveZeroSettingActivity.class);
				startActivityForResult(intent,REMOVE_ZERO_REQUEST_COUNT);
				break;
		}
	}
}