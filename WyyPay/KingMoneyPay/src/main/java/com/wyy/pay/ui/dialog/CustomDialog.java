package com.wyy.pay.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wyy.pay.R;

/**
 * Created by liyusheng on 16/11/24.
 */

public class CustomDialog extends Dialog{
    private TextView tvTitle;
    private TextView tvContent;
    private Button btnOk;
    private Button btnCancel;
    private String strTitle;
    private String strContent;
    private String strCancel;
    private InfoCallback callback;
    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public void setStrContent(String strContent) {
        this.strContent = strContent;
    }

    public void setStrCancel(String strCancel) {
        this.strCancel = strCancel;
    }

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    public CustomDialog(Context context,int themeResId,InfoCallback callback) {
        super(context,themeResId);
        this.callback = callback;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_layout);
        initView();
        initListener();
    }

    private void initListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.this.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.btnOkOnClick();
                }
                CustomDialog.this.dismiss();
            }
        });
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
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
         void btnOkOnClick();

    }
}
