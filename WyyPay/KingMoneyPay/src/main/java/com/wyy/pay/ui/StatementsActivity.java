package com.wyy.pay.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyy.pay.R;
import com.wyy.pay.view.XListView;

public class StatementsActivity extends BaseActivity implements View.OnClickListener {
	private RelativeLayout rlDiscount;//用于显示优惠信息
	private TextView tvDiscount;//显示优惠信息
	private Button btnDelete;//删除优惠信息
	private Button tvOrderToPay;//收款
	private TextView tvDiscountTypeT,tvDiscountType,tvDiscountTM,tvDiscountM;
	private double tempPayTotal =0;
	private TextView tvCount;//显示总购物数量
	private TextView tvSum;//合计多少钱
	private TextView tvOrderTotalMoney;//应收多少钱
	private XListView statementsListView;//显示商品列表
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_statements);
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
	}
	@Override
	public void initView() {
		statementsListView = (XListView) findViewById(R.id.statementsListView);
		//TODO:XXX 加载adapter
		tvOrderTotalMoney = (TextView) findViewById(R.id.tvOrderTotalMoney);
		tvSum = (TextView) findViewById(R.id.tvSum);
		tvCount = (TextView) findViewById(R.id.tvCount);
		tvDiscountM = (TextView) findViewById(R.id.tvDiscountM);
		tvDiscountTM = (TextView) findViewById(R.id.tvDiscountTM);
		tvDiscountType = (TextView) findViewById(R.id.tvDiscountType);
		tvDiscountTypeT = (TextView) findViewById(R.id.tvDiscountTypeT);
		tvDiscount = (TextView) findViewById(R.id.tvDiscount);
		rlDiscount  = (RelativeLayout) findViewById(R.id.rlDiscount);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		tvOrderToPay = (Button) findViewById(R.id.tvOrderToPay);

		tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
		tvNavTitle.setText("结算");
	}

	@Override
	public void initData() {
		//TODO:XXX 获取intent data

		setDiscountGone();
		tvCount.setText("");
		tvSum.setText("");
		tvOrderTotalMoney.setText("");
	}

	@Override
	public void initListener() {
		btnDelete.setOnClickListener(this);
		tvOrderToPay.setOnClickListener(this);
		tvNavLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				StatementsActivity.this.finish();
			}
		});
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 设置优惠信息显示
	 * @param isShow 是否显示
	 * @param type type=1 整单折扣，type =2整单优惠
	 * @param text 折扣或优惠数字
     */
	private void setDiscountShow(boolean isShow,int type,float text){
		rlDiscount.setVisibility(isShow?View.VISIBLE:View.GONE);
		tvDiscountTypeT.setVisibility(isShow?View.VISIBLE:View.GONE);
		tvDiscountType.setVisibility(isShow?View.VISIBLE:View.GONE);
		tvDiscountTM.setVisibility(isShow?View.VISIBLE:View.GONE);
		tvDiscountM.setVisibility(isShow?View.VISIBLE:View.GONE);
		if(isShow && type==1){
			tvDiscount.setText(String.format("%s折",text));
			tvDiscountType.setText(String.format("整单%s折",text));
			tvDiscountM.setText(String.format("￥%s",tempPayTotal*(text/10)));
		}else if(isShow && type==2){
			tvDiscountM.setText(String.format("￥%s",text));
			tvDiscount.setText(String.format("减%s元",text));
			tvDiscountType.setText(String.format("整单减%s元",text));
		}
	}
	public void setDiscountGone(){
		setDiscountShow(false,0,0);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btnDelete:
				setDiscountGone();
				break;
			case R.id.tvOrderToPay:
				//TODO:XX弹出框选择支付方式
				break;
		}
	}
}