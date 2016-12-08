package com.wyy.pay.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.utils.RemoveZeroType;
import com.wyy.pay.view.XTTagContainerLayout;

import java.util.ArrayList;
import java.util.List;

import xtcore.utils.PreferenceUtils;

public class DiscountSettingActivity extends BaseActivity implements View.OnClickListener {
	private CheckBox cbxRemoveZero;
	private LayoutInflater inflater;
	private XTTagContainerLayout modelTag,processTag;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_discount_setting);
		inflater = LayoutInflater.from(this);
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
	}
	@Override
	public void initView() {
		modelTag = (XTTagContainerLayout) findViewById(R.id.modelTag);
		processTag = (XTTagContainerLayout) findViewById(R.id.processTag);
		cbxRemoveZero = (CheckBox) findViewById(R.id.cbxRemoveZero);
		tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
		tvNavTitle.setText("优惠折扣设置");
	}

	@Override
	public void initData() {
		List<String> discountList=new ArrayList<String>();
		discountList.add("5折");
		discountList.add("4折");
		discountList.add("6折");
		discountList.add("9折");
		discountList.add("+");
		List<String> processList=new ArrayList<String>();
		processList.add("减10元");
		processList.add("减20元");
		processList.add("减13元");
		processList.add("减15元");
		processList.add("减60元");
		processList.add("+");
	processTag.setTags(processList);
		processTag.setOnTagClickListener(new XTTagContainerLayout.OnTagClickListener() {
			@Override
			public void onTagClick(int position, String text) {
				processTag.removeTag(position);
			}

			@Override
			public void onTagTextBgOnClick(int position, String text) {
				if("+".equals(text)){
					Toast.makeText(DiscountSettingActivity.this,"弹框添加item",Toast.LENGTH_SHORT).show();
				}
			}
		});
		modelTag.setTags(discountList);
		modelTag.setOnTagClickListener(new XTTagContainerLayout.OnTagClickListener() {
			@Override
			public void onTagClick(int position, String text) {
				modelTag.removeTag(position);
			}

			@Override
			public void onTagTextBgOnClick(int position, String text) {
				if("+".equals(text)){
					Toast.makeText(DiscountSettingActivity.this,"弹框添加item",Toast.LENGTH_SHORT).show();
				}
			}
		});

		modelTag.setSelectAllTagView(cbxRemoveZero.isChecked());
		processTag.setSelectAllTagView(cbxRemoveZero.isChecked());

	}


	@Override
	public void initListener() {
		cbxRemoveZero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				modelTag.setSelectAllTagView(isChecked);
				processTag.setSelectAllTagView(isChecked);
			}
		});
		tvNavLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DiscountSettingActivity.this.finish();
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
		switch ((int)v.getTag()){

		}
	}
}