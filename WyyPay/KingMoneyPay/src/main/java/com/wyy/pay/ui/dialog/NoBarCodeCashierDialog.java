package com.wyy.pay.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.SubstringUtils;
import com.wyy.pay.utils.Utils;

/**
 * Created by liyusheng on 16/11/24.
 */

public class NoBarCodeCashierDialog extends Dialog implements View.OnClickListener {
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
    private InfoCallback callback;
    private TextView tvMoneySumCount;
    private StringBuilder builder;
    private Context mContext;
    public NoBarCodeCashierDialog(Context context, int themeResId, InfoCallback callback) {
        super(context,themeResId);
        this.mContext = context;
        this.callback = callback;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_no_barcode_cashier);
        builder = new StringBuilder();
        initView();
    }



    private void initView() {
        tvMoneySumCount = (TextView) findViewById(R.id.tvMoneySumCount);
        TextView tvTitle = (TextView) findViewById(R.id.tvNavTitle);
        tvTitle.setText("无码收银");
        for (int i=0;i<16;i++){
            View v = findViewById(R.id.view1 +i);
            v.setOnClickListener(this);
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
    private boolean checkBuilderLengthOut(String inputText){
        String temp = builder.toString();

        if(temp.contains("."))
        {
           String beforeTemp = SubstringUtils.substringBefore(temp,".");
           String afterTemp = SubstringUtils.substringAfter(temp,".");
            if(afterTemp.length()>1){
                Toast.makeText(mContext,"订单金额只保留小数点后两位！",Toast.LENGTH_SHORT).show();
                return true;
            }
            if(beforeTemp.length()>4&&afterTemp.length()>1){
                Toast.makeText(mContext,"订单金额只保留小数点后两位！",Toast.LENGTH_SHORT).show();
                return true;
            }

        }else if(!temp.contains(".")&&builder.length()>0&&builder.length()>4&&!".".equals(inputText)){
            Toast.makeText(mContext,"单个订单金额超过限制，请重新输入！",Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(temp.contains(".")&&builder.length()>0&&builder.length()>6){
            Toast.makeText(mContext,"订单金额只保留小数点后两位！",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    String goodsPrice = "0";
    String tempText = "";
    private  void appendNumText(String text){
        if(tempText.contains(".")&&text.equals(".")){
            return;
        }
if(".".equals(text)&&builder.length()==0){
    builder.append("0");
}
        if(TextUtils.isEmpty(text)){
            setDefaultText();
            return;
        }

        builder.append(text);
        tempText = text;
        String result =  builder.toString();
        if(".".equals(result.substring(0))){
            String temp = builder.toString();
            builder.delete(0,builder.length());
            builder.append("0"+temp);
        }
        if(".".equals(result.substring(result.length()-1))){
            result +="00";
        }
        goodsPrice = result;
        tvMoneySumCount.setText(String.format("￥\r\r%s", result));
    }
    private void setDefaultText(){
        tvMoneySumCount.setText(String.format("￥\r\r%s", "0.00"));
    }
    @Override
    public void show() {
        super.show();

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
            case ADD_SHOPING_TO_CART:
                addShoping2Cart();
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
            case ADD_SHOPING_TO_CART2:
                addShoping2Cart();
                break;

        }
    }

    private void addNumber(TextView v) {
        int le = builder.toString().length();
        if(!checkBuilderLengthOut(v.getText().toString().trim())) {
            //clearAll();
            appendNumText(v.getText().toString().trim());
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

    private void addShoping2Cart() {
        if(this.callback!=null&&!"0".equals(goodsPrice)&&!".00".equals(goodsPrice)&&!"0.".equals(goodsPrice)&&!"0.00".equals(goodsPrice)){
            callback.noBarcodeCashierAddShoping2Cart(goodsPrice);
            goodsPrice = "0";
            clearAll();
        }else {
            Toast.makeText(mContext,"添加的订单金额不能是0！",Toast.LENGTH_SHORT).show();
        }
    }

    public interface InfoCallback {
         void noBarcodeCashierAddShoping2Cart(String price);

    }
}
