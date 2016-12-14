package com.wyy.pay.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wyy.pay.R;
import com.wyy.pay.utils.Utils;

/**
 * Created by liyusheng on 16/11/12.
 */

public class OrderListFilterPopWindow extends PopupWindow implements View.OnClickListener {
    private Activity mActivity;
    private FilterOnClickListener mPopListener;
    private  int screenW;
   public OrderListFilterPopWindow(Activity activity){
       this.mActivity = activity;
       LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View view = inflater.inflate(R.layout.pop_window_order_list_filter,null);
        screenW = mActivity.getWindowManager().getDefaultDisplay().getWidth();
       setContentView(view);
       TextView tvFilterWeixinPay = (TextView) view.findViewById(R.id.tvFilterWeixinPay);
       tvFilterWeixinPay.setOnClickListener(this);
       TextView tvFilterAlipay = (TextView) view.findViewById(R.id.tvFilterAlipay);
       tvFilterAlipay.setOnClickListener(this);
       TextView tvFilterCash = (TextView) view.findViewById(R.id.tvFilterCash);
       tvFilterCash.setOnClickListener(this);
       TextView tvNoFilter = (TextView) view.findViewById(R.id.tvNoFilter);
       tvNoFilter.setOnClickListener(this);
       this.setWidth(screenW/2- Utils.dip2px(mActivity,50.0f));
       this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
       this.setFocusable(true);
       this.setOutsideTouchable(true);
       this.update();//刷新状态
       ColorDrawable dw = new ColorDrawable(0000000000);
       this.setBackgroundDrawable(dw);//点击back或其他地方消失，onDismissListener
       this.setAnimationStyle(R.style.AnimationPreview);

   }
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, Utils.dip2px(mActivity,14));
        } else {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if(this.mPopListener==null){
            this.dismiss();
            return;
        }
        switch (v.getId()){
            case R.id.tvFilterWeixinPay:
                mPopListener.onOrderListFilterClick(OrderListActivity.TYPE_WEIXIN);
                this.dismiss();
                break;
            case R.id.tvFilterAlipay:
                mPopListener.onOrderListFilterClick(OrderListActivity.TYPE_ALIPAY);
                this.dismiss();
                break;
            case R.id.tvFilterCash:
                mPopListener.onOrderListFilterClick(OrderListActivity.TYPE_CASH);
                this.dismiss();
                break;
            case R.id.tvNoFilter:
                mPopListener.onOrderListFilterClick(OrderListActivity.SELECT_ALL);
                this.dismiss();
                break;
        }
    }
    public void setFilterMpopWindowOnClickListener(FilterOnClickListener listener){
        this.mPopListener = listener;
    }
    public interface FilterOnClickListener{
        public void onOrderListFilterClick(int type);
    }
}