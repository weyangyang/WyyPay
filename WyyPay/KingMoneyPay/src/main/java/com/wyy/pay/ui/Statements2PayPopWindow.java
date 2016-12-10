package com.wyy.pay.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wyy.pay.R;
import com.wyy.pay.adapter.OrderProductListAdapter;
import com.wyy.pay.bean.TableGoodsDetailBean;
import com.wyy.pay.ui.dialog.CustomDialog;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.XListView;

import java.util.ArrayList;

/**
 * Created by liyusheng on 16/12/10.
 */

public class Statements2PayPopWindow extends PopupWindow {
    private Activity mActivity;
    private View parentView;
    private  View spaceView;
    private TextView tvPayMoney;
    private TextView tvDismissW;
    private TextView tvWeixinPay;
    private TextView tvAliPay;
    private TextView tvMoneyPay;
    private double payMoney =0;

    public Statements2PayPopWindow(Activity activity,double payMoney){
       this.mActivity = activity;
        this.payMoney = payMoney;
       initView();
       setPopConfig();
       initListener();

   }

    private void initListener() {
        tvDismissW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Statements2PayPopWindow.this.isShowing()) {
                    Statements2PayPopWindow.this.dismiss();
                }
            }
        });
        spaceView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (Statements2PayPopWindow.this.isShowing()) {
                    Statements2PayPopWindow.this.dismiss();
                }
            }
        });
        tvWeixinPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.on2PayMoney(1,payMoney);
                }
                dismiss();
            }
        });
        tvAliPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.on2PayMoney(2,payMoney);
                }
                dismiss();
            }
        });
        tvMoneyPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.on2PayMoney(3,payMoney);
                }
                dismiss();
            }
        });
    }

    private void setPopConfig() {
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();//刷新状态
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);//点击back或其他地方消失，onDismissListener
       // this.setAnimationStyle(R.style.AnimationPreview);
        //获取自身的长宽高
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parentView= inflater.inflate(R.layout.popup_window_statements2pay,null);
        spaceView = parentView.findViewById(R.id.spaceView);
        tvPayMoney = (TextView) parentView.findViewById(R.id.tvPayMoney);
        tvPayMoney.setText(String.format("￥%.2f",payMoney));
        tvDismissW = (TextView) parentView.findViewById(R.id.tvDismissW);
        tvWeixinPay = (TextView) parentView.findViewById(R.id.tvWeixinPay);
        tvAliPay = (TextView) parentView.findViewById(R.id.tvAliPay);
        tvMoneyPay = (TextView) parentView.findViewById(R.id.tvMoneyPay);

        setContentView(parentView);
    }

    public void showPopupWindow(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        showAsDropDown(v, 0,  -location[1]);

    }


    @Override
    public void dismiss() {
        super.dismiss();
    }
    private  Statements2PayPopWindowListener listener;
    public void set2PayWindowListener(Statements2PayPopWindowListener listener){
        this.listener= listener;
    }
    public interface Statements2PayPopWindowListener{
        void on2PayMoney(int type,double payMoney);
    }
}
