package com.wyy.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.bean.StatementsDiscountBean;
import com.wyy.pay.bean.TableCategoryBean;
import com.wyy.pay.bean.TableDiscountNumBean;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.SubstringUtils;
import com.wyy.pay.utils.Utils;

import java.util.ArrayList;

import xtcore.utils.PreferenceUtils;


public class CashierActivity extends BaseActivity implements View.OnClickListener, View.OnLongClickListener, StatementDiscountPopWindow.DiscountPopWindowListener {
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
    private TextView tvDiscount;
    private RelativeLayout rlDiscount;
    private StringBuilder builder;
    private Button btnDelete;
    private double totalMoney=0.00;
    private double tempMoney=0.00;
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
        tvDiscount = (TextView) findViewById(R.id.tvDiscount);
        tvMoneySumCount = (TextView) findViewById(R.id.tvMoneySumCount);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        rlDiscount = (RelativeLayout) findViewById(R.id.rlDiscount);
        setDiscountGone();
        tvNavLeft.setText("订单");
        tvNavLeft.setTag("17");
        tvNavTitle.setText("收款");
        tvNavRight.setText("优惠券");
        tvNavRight.setTag("18");
        for (int i=0;i<16;i++){
            View v = findViewById(R.id.view1 +i);
            v.setOnClickListener(this);
            if(3==i){
                v.setOnLongClickListener(this);
            }
        }
    }

    @Override
    public void initData() {
    }
    private void addNumber(TextView v) {
        int le = builder.toString().length();
        if(!checkBuilderLengthOut(v.getText().toString().trim())) {
            //clearAll();
            appendNumText(v.getText().toString().trim());
        }
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
    /**
     * 设置优惠信息显示
     * @param isShow 是否显示
     * @param type type=1 整单折扣，type =2整单优惠
     * @param number 折扣或优惠数字
     */
    private void setDiscountShow(boolean isShow,int type,double number){
        rlDiscount.setVisibility(isShow?View.VISIBLE:View.GONE);
        if(!isShow){
            tempMoney =0.00;
        }else {
            switch (type){
                case 1:
                    tvDiscount.setText(String.format("%.1f折",number));
                    double tDiscount = totalMoney*((number/10));
                    String tDiscountPrice = String.format("%.2f",tDiscount);
                    tempMoney = Double.parseDouble(tDiscountPrice);
                    break;
                case 2:
                case 3:
                    tvDiscount.setText(String.format("减%.2f元",number));
                   tempMoney = totalMoney - number;
                    break;
            }


        }

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
    public void clearAll(){
        if(builder!=null){
            builder.delete(0,builder.length());
            appendNumText(builder.toString());
        }else {
            setDefaultText();
        }
    }
    private void setDefaultText(){
        tvShowMoneyDetail.setText("");
        tvMoneySumCount.setText(String.format("￥\r\r%s", "0.00"));
        totalMoney = 0.00;
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
        totalMoney = Double.parseDouble(Utils.getNumSumWithString(result));
        tvMoneySumCount.setText(String.format("￥\r\r%s", Utils.getNumSumWithString(result)));
    }
    @Override
    public void initListener() {
        tvNavLeft.setOnClickListener(this);
        tvNavRight.setOnClickListener(this);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDiscountGone();
            }
        });
    }
    public void setDiscountGone(){
        setDiscountShow(false,0,0);
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
            case DELETE:
                deleteMoneyDetailText();
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
            case ALIPAY:
                toWeixinOrAlipay(ConstantUtils.PAY_TYPE_ALIPAY);
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
            case WEIXIN_PAY:
                toWeixinOrAlipay(ConstantUtils.PAY_TYPE_WEXIN);
                break;
            case NUMBER_0:
                addNumber((TextView) v);
                break;
            case POINT:
                addNumber((TextView) v);
                break;
            case ADD:
                addNumber((TextView) v);
                break;
            case LOCAL_CASHHIER:
                if(totalMoney<=0){
                    Toast.makeText(this,"付款金额要大于0!",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(this,CashPayActivity.class);
                if(tempMoney>0 && tempMoney<totalMoney){

                    intent.putExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,tempMoney);
                }else {

                    intent.putExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,totalMoney);
                }
                this.startActivity(intent);
                break;
            case TV_NAV_LEFT:
                Toast.makeText(this,(String)v.getTag(),Toast.LENGTH_SHORT).show();
                break;
            case TV_NAV_RIGHT:
                if(totalMoney>0){
                    selectDiscount();
                }else {
                    Toast.makeText(this,"订单金额不能为0!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void selectDiscount() {
        StatementDiscountPopWindow popWindow = new StatementDiscountPopWindow(this);
        ArrayList<StatementsDiscountBean> arrayListZhenZ = new ArrayList<>();
        ArrayList<StatementsDiscountBean> arrayListZhenJ = new ArrayList<>();
        boolean isDiscount = PreferenceUtils.getPrefBoolean(this,PreferenceUtils.SP_DISCOUNT_SWITCH,false);
        if(isDiscount){
            String orderBy = TableDiscountNumBean.COLUMN_CREATE_CATEGORY_TIEM+" DESC";
            ArrayList<TableDiscountNumBean> tableList = new TableDiscountNumBean().query(null, TableCategoryBean.COLUMN_USER_ID +"=?",new String[]{Utils.get6MD5WithString("18501053570")},null,null,orderBy);
            if(tableList!=null&&tableList.size()>0){
                for (TableDiscountNumBean bean:tableList){
                    if(!"+".equals(bean.getShowText())){
                        StatementsDiscountBean discountBean = new StatementsDiscountBean();
                        discountBean.setType(bean.getType());
                        discountBean.setNumber(bean.getDiscountNum());
                        if(bean.getType()==1){
                            arrayListZhenZ.add(discountBean);
                        }else if(bean.getType()==2){
                            arrayListZhenJ.add(discountBean);
                        }
                    }
                }
            }
        }
        popWindow.setDiscountListData(arrayListZhenZ,arrayListZhenJ);
        popWindow.showPopupWindow(tvNavTitle);
        popWindow.setDiscuntPopWListener(CashierActivity.this);
        tvNavRight.setText("");
        tvNavRight.setBackgroundResource(R.drawable.ic_nav_back);
    }

    private void toWeixinOrAlipay(int payType) {
        String strMoney = tvMoneySumCount.getText().toString().trim();
        strMoney =  SubstringUtils.substringAfter(strMoney,"￥");
        double money = Double.parseDouble(strMoney);
        if(money<=0){
            Toast.makeText(this,"付款金额不能为0!",Toast.LENGTH_SHORT).show();
            return;
        }

       Intent intent = new Intent(this,ScanPayActivity.class);
        intent.putExtra(ConstantUtils.INTENT_KEY_PAY_TYPE,payType);
        if(tempMoney>0 && tempMoney<money){
            intent.putExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,tempMoney);
        }else {

            intent.putExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,money);
        }
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        switch (Integer.parseInt((String)v.getTag())){
            case DELETE:
                clearAll();
                return true;
        }
        return false;
    }

    @Override
    public void onItemSelected(int type, double number) {
        if(type==1){
            setDiscountShow(true,type,number);
            return;
        }
        if(totalMoney >number && type==2||type==3){
            setDiscountShow(true,type,number);
        }else {
            Toast.makeText(this,"整单减少的金额已大于商品总价！",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDiscountPopWindowDismiss() {
        tvNavRight.setText("选择优惠");
        tvNavRight.setBackground(null);
    }
}
