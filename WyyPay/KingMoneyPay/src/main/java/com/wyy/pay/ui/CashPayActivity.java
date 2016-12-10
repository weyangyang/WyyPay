package com.wyy.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.SubstringUtils;
import com.wyy.pay.view.ClearEditText;


public class CashPayActivity extends BaseActivity implements View.OnClickListener {
    private static final int NUMBER_7 = 1;
    private static final int NUMBER_8 = 2;
    private static final int NUMBER_9 = 3;
    private static final int DELETE_ALL = 4;
    private static final int NUMBER_4 = 5;
    private static final int NUMBER_5 = 6;
    private static final int NUMBER_6 = 7;
    private static final int DELETE_SINGLE = 8;
    private static final int NUMBER_1 = 9;
    private static final int NUMBER_2 = 10;
    private static final int NUMBER_3 = 11;
    private static final int ADD_SHOPING_TO_CART = 12;//添加
    private static final int NUMBER_0 = 13;
    private static final int POINT = 14;
    private static final int NUMBER_DOUBLE_ZERO = 15;
    private static final int ADD_SHOPING_TO_CART2 = 16;//添加
    private StringBuilder builder;
    private TextView tvMoneySumCount;//应收金额
    private TextView tvShishouMoney;//实收金额
    private TextView tvZhaoZeroMoney;//找零金额
    private double shiSMoney =0;
    private double zhaoLMoney =0;
    private double totalMoney =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cash_cashier);
        super.onCreate(savedInstanceState);
        builder = new StringBuilder();
        initView();
        initData();
        initListener();

    }

    @Override
    public void initView() {
        tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
        tvNavTitle.setText("现金收款");
        tvNavRight.setText("作废");

        tvMoneySumCount = (TextView) findViewById(R.id.tvMoneySumCount);
        tvShishouMoney = (TextView) findViewById(R.id.tvShishouMoney);
        tvZhaoZeroMoney = (TextView) findViewById(R.id.tvZhaoZeroMoney);
        for (int i=0;i<16;i++){
            View v = findViewById(R.id.view1 +i);
            v.setOnClickListener(this);
        }
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        totalMoney = intent.getDoubleExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,0);
        tvMoneySumCount.setText(String.format("￥%.2f",totalMoney));
    }

    @Override
    public void initListener() {
        tvNavLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CashPayActivity.this.finish();
            }
        });
        tvNavRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CashPayActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (Integer.parseInt((String)v.getTag())){

            case NUMBER_7:
                addNumber((TextView) v);
                break;
            case NUMBER_8:
                addNumber((TextView) v);
                break;
            case NUMBER_9:
                addNumber((TextView) v);
                break;
            case DELETE_ALL:
                clearAll();
                break;
            case NUMBER_4:
                addNumber((TextView) v);
                break;
            case NUMBER_5:
                addNumber((TextView) v);
                break;
            case NUMBER_6:
                addNumber((TextView) v);
                break;
            case DELETE_SINGLE:
                deleteSingle();
                break;
            case NUMBER_1:
                addNumber((TextView) v);
                break;
            case NUMBER_2:
                addNumber((TextView) v);
                break;
            case NUMBER_3:
                addNumber((TextView) v);
                break;

            case NUMBER_0:
                addNumber((TextView) v);
                break;
            case POINT:
                addNumber((TextView) v);
                break;
            case NUMBER_DOUBLE_ZERO:
                addNumber((TextView) v);
                break;
            case ADD_SHOPING_TO_CART:
            case ADD_SHOPING_TO_CART2:
                //完成结算
                if(shiSMoney-totalMoney>=0){
                    CashPayActivity.this.finish();
                }else {
                    Toast.makeText(this,"实收金额不能小于应收金额!",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
    private void deleteSingle() {
        if(builder!=null&&builder.length()>0){
            builder.delete(builder.length()-1,builder.length());
            String  result = builder.toString();
            clearAll();
            appendNumText(result);
        }
    }
    public void clearAll(){
        if(builder!=null){
            builder.delete(0,builder.length());
            appendNumText(builder.toString());
        }else {
            setDefaultText();
        }
    }
    private void setDefaultText(){
        shiSMoney = 0.00;
        zhaoLMoney=0.00;
        tvShishouMoney.setText(String.format("￥\r\r%s", "0.00"));
        tvZhaoZeroMoney.setText(String.format("￥\r\r%s", "0.00"));
    }
    private boolean checkBuilderLengthOut(String inputText){
        String temp = builder.toString();

        if(temp.contains("."))
        {
            String beforeTemp = SubstringUtils.substringBefore(temp,".");
            String afterTemp = SubstringUtils.substringAfter(temp,".");
            if(afterTemp.length()>1){
                Toast.makeText(this,"订单金额只保留小数点后两位！",Toast.LENGTH_SHORT).show();
                return true;
            }
            if(beforeTemp.length()>4&&afterTemp.length()>1){
                Toast.makeText(this,"订单金额只保留小数点后两位！",Toast.LENGTH_SHORT).show();
                return true;
            }

        }else if(!temp.contains(".")&&builder.length()>0&&builder.length()>4&&!".".equals(inputText)){
            Toast.makeText(this,"单个订单金额超过限制，请重新输入！",Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(temp.contains(".")&&builder.length()>0&&builder.length()>6){
            Toast.makeText(this,"订单金额只保留小数点后两位！",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private  void appendNumText(String text){
        if(".".equals(text)&&builder.length()==0){
            builder.append("0");
        }
        if(TextUtils.isEmpty(text)){
            setDefaultText();
            return;
        }
        if(".".equals(text)&&builder.toString().contains(".")){
            return;
        }
        builder.append(text);
        String result =  builder.toString();
        if(".".equals(result.substring(0))){
            String temp = builder.toString();
            builder.delete(0,builder.length());
            builder.append("0"+temp);
        }
        if(".".equals(result.substring(result.length()-1))){
            result +="00";
        }
       // goodsPrice = result;
        shiSMoney = Double.parseDouble(result);
        zhaoLMoney = shiSMoney -totalMoney;
        if(zhaoLMoney>0){
            tvZhaoZeroMoney.setText(String.format("￥%.2f", zhaoLMoney));
        }else {
            zhaoLMoney=0.00;
            tvZhaoZeroMoney.setText(String.format("￥%.2f", zhaoLMoney));
        }
        tvShishouMoney.setText(String.format("￥\r\r%s", result));
    }
    private void addNumber(TextView v) {
        int le = builder.toString().length();
        if(!checkBuilderLengthOut(v.getText().toString().trim())) {
            //clearAll();
            appendNumText(v.getText().toString().trim());
        }
    }
}
