package com.wyy.pay.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyy.pay.R;

/**
 * Created by liyusheng on 16/12/8.
 */

public class DiscountTagView extends RelativeLayout{
    private TextView tvDiscount;
    private Button deleteBtn;
    public DiscountTagView(Context context) {
        super(context);
        RelativeLayout convertView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_discount, this);
         tvDiscount = (TextView) convertView.findViewById(R.id.tvDiscount);
         deleteBtn = (Button) convertView.findViewById(R.id.btnDelete);
    }
    public void setTextBackground(int resId){
        tvDiscount.setBackgroundResource(resId);
    }
    public void setDeleteBtnVisibility(boolean isShow){
        if(isShow){

            deleteBtn.setVisibility(View.VISIBLE);
        }else {
            deleteBtn.setVisibility(View.GONE);
        }
    }
    public void setDeleteBtnOnClickListener(OnClickListener listener){
        deleteBtn.setOnClickListener(listener);
    }
    public void setTextViewOnClickListener(OnClickListener listener){
        tvDiscount.setOnClickListener(listener);
    }
    public void setText(String text){
        tvDiscount.setText(text);
    }
    public String getText(){
        return tvDiscount.getText().toString().trim();
    }
}
