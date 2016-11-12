package com.wyy.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.SubstringUtils;
import com.wyy.pay.utils.Utils;


public class CashierActivity extends BaseActivity implements View.OnClickListener {
    private static final int NUMBER_7 = 1;
    private static final int NUMBER_8 = 2;
    private static final int NUMBER_9 = 3;
    private static final int DELETE = 4;
    private static final int NUMBER_4 = 5;
    private static final int NUMBER_5 = 6;
    private static final int NUMBER_6 = 7;
    private static final int ALIPAY = 8;
    private static final int NUMBER_1 = 9;
    private static final int NUMBER_2 = 10;
    private static final int NUMBER_3 = 11;
    private static final int WEIXIN_PAY = 12;
    private static final int NUMBER_0 = 13;
    private static final int POINT = 14;
    private static final int ADD = 15;
    private static final int LOCAL_CASHHIER = 16;//现金
    private static final int TV_NAV_LEFT = 17;//tvNavLeft
    private static final int TV_NAV_RIGHT = 18;//tvNavRight

    private TextView tvShowMoneyDetail;
    private TextView tvMoneySumCount;
    private StringBuilder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cashier);
        super.onCreate(savedInstanceState);
        builder = new StringBuilder();
        initView();
        initData();
        initListener();

    }

    @Override
    public void initView() {
        tvShowMoneyDetail = (TextView) findViewById(R.id.tvShowMoneyDetail);
        tvMoneySumCount = (TextView) findViewById(R.id.tvMoneySumCount);
        tvNavLeft.setText("订单");
        tvNavLeft.setTag("17");
        tvNavTitle.setText("收款");
        tvNavRight.setText("优惠券");
        tvNavRight.setTag("18");
        for (int i=0;i<16;i++){
            View v = findViewById(R.id.view1 +i);
            v.setOnClickListener(this);
        }
    }

    @Override
    public void initData() {
    }
    private void deleteMoneyDetailText(){
        String result =  builder.toString();
        if(!TextUtils.isEmpty(result)&&!result.contains("+")){
            result ="";
        }else {
            result = SubstringUtils.substringBeforeLast(result,"+");
        }
        builder.delete(0,builder.length());
        appendNumText(result);
    }
    private void setDefaultText(){
        tvShowMoneyDetail.setText("");
        tvMoneySumCount.setText(String.format("￥\r\r%s", "0.00"));
    }
    private  void appendNumText(String text){

        if(TextUtils.isEmpty(text)){
            setDefaultText();
            return;
        }
        builder.append(text);
        String result =  builder.toString();
        if("+".equals(result.substring(0))){
            setDefaultText();
            return;
        }
        if(".".equals(result.substring(result.length()-1))){
            result +="00";
        }
        tvShowMoneyDetail.setText(result);
        tvMoneySumCount.setText(String.format("￥\r\r%s", Utils.getNumSumWithString(result)));
    }
    @Override
    public void initListener() {
        tvNavLeft.setOnClickListener(this);
        tvNavRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (Integer.parseInt((String)v.getTag())){

            case NUMBER_7:
                appendNumText(((TextView)v).getText().toString().trim());
                break;
            case NUMBER_8:
                appendNumText(((TextView)v).getText().toString().trim());
                break;
            case NUMBER_9:
                appendNumText(((TextView)v).getText().toString().trim());
                break;
            case DELETE:
                deleteMoneyDetailText();
                break;
            case NUMBER_4:
                appendNumText(((TextView)v).getText().toString().trim());
                break;
            case NUMBER_5:
                appendNumText(((TextView)v).getText().toString().trim());
                break;
            case NUMBER_6:
                appendNumText(((TextView)v).getText().toString().trim());
                break;
            case ALIPAY:
                toWeixinOrAlipay(ConstantUtils.PAY_TYPE_ALIPAY);
                break;
            case NUMBER_1:
                appendNumText(((TextView)v).getText().toString().trim());
                break;
            case NUMBER_2:
                appendNumText(((TextView)v).getText().toString().trim());
                break;
            case NUMBER_3:
                appendNumText(((TextView)v).getText().toString().trim());
                break;
            case WEIXIN_PAY:
                toWeixinOrAlipay(ConstantUtils.PAY_TYPE_WEXIN);
                break;
            case NUMBER_0:
                appendNumText(((TextView)v).getText().toString().trim());
                break;
            case POINT:
                appendNumText(((TextView)v).getText().toString().trim());
                break;
            case ADD:
                appendNumText(((TextView)v).getText().toString().trim());
                break;
            case LOCAL_CASHHIER:
                Toast.makeText(this,(String)v.getTag(),Toast.LENGTH_SHORT).show();
                break;
            case TV_NAV_LEFT:
                Toast.makeText(this,(String)v.getTag(),Toast.LENGTH_SHORT).show();
                break;
            case TV_NAV_RIGHT:
                Toast.makeText(this,(String)v.getTag(),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void toWeixinOrAlipay(int payType) {
        String strMoney = tvMoneySumCount.getText().toString().trim();
        strMoney =  SubstringUtils.substringAfter(strMoney,"￥");
        double money = Double.parseDouble(strMoney);
        if(money<=0){
            Toast.makeText(this,"付款金额不能为0!",Toast.LENGTH_SHORT).show();
        }
       Intent intent = new Intent(this,ScanPayActivity.class);
        intent.putExtra(ConstantUtils.INTENT_KEY_PAY_TYPE,payType);
        intent.putExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,money);
        startActivity(intent);
    }
}
