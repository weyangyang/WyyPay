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
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.ClearEditText;

/**
 * Created by liyusheng on 16/11/24.
 */

public class CustomDiscountDialog extends Dialog{
    private TextView tvTitle;
    private TextView tvContent;
    private Button btnOk;
    private Button btnCancel;
    private String strTitle;
    private String strContent;
    private String strCancel;
    private InfoCallback callback;
    private ClearEditText etDiscount;
    private int type =0; //type==1 整单折扣， type ==2 整单减价
    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public void setStrContent(String strContent) {
        this.strContent = strContent;
    }

    public void setStrCancel(String strCancel) {
        this.strCancel = strCancel;
    }

  public CustomDiscountDialog(Context context, int type,int themeResId, InfoCallback callback) {
        super(context,themeResId);
        this.callback = callback;
        this.type = type;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_discount_layout);
        initView();
        initListener();
    }

    private void initListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDiscountDialog.this.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discount = etDiscount.getText().toString().trim();
                if(!Utils.isDiscountBadNumber(discount)){
                    Toast.makeText(getContext(),"优惠折扣格式不正确！请再次输入",Toast.LENGTH_SHORT).show();
                    etDiscount.setText("");
                    return;
                }else {
                    float temp = Float.parseFloat(discount);
                    if(type==1&&temp>=10 ||temp<1 ){
                        Toast.makeText(getContext(),"整单折扣需1--10范围之间",Toast.LENGTH_SHORT).show();
                        etDiscount.setText("");
                        return;
                    }
                    if(type ==2 &&temp<1 ||temp>5000 ){
                        Toast.makeText(getContext(),"整单减价需1--5000范围之间！",Toast.LENGTH_SHORT).show();
                        etDiscount.setText("");
                        return;
                    }
                    if(callback!=null){
                        callback.btnOkOnClick(String.format("%.1f",temp));
                    }
                    CustomDiscountDialog.this.dismiss();
                }
            }
        });
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        etDiscount = (ClearEditText) findViewById(R.id.etDiscount);
        tvContent = (TextView) findViewById(R.id.tvContent);
        btnCancel = (Button) findViewById(R.id.btnDialogCancel);
        btnOk = (Button) findViewById(R.id.btnDialogOk);
    }

    @Override
    public void show() {
        super.show();
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle.setText(strTitle);
        }
        if(!TextUtils.isEmpty(strContent)) {
            btnOk.setText(strContent);
        }
        if(!TextUtils.isEmpty(strCancel)){
            btnCancel.setText(strCancel);
        }
    }
    public interface InfoCallback {
         void btnOkOnClick(String text);

    }
}
