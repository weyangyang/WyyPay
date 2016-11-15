package com.wyy.pay.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.ClearEditText;

/**
 * Created by liyusheng on 16/11/15.
 */

public class AddCategoryPopWindow extends PopupWindow implements View.OnClickListener {
    private Activity mActivity;
    private AddCgOnClickListener mPopListener;
    private ClearEditText editCategory;
    private int fromType=0;
    private int screenW=0;
   public AddCategoryPopWindow(Activity activity,int fromType){
       this.mActivity = activity;
       this.fromType = fromType;
       LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View view = inflater.inflate(R.layout.pop_window_add_category,null);
        screenW = mActivity.getWindowManager().getDefaultDisplay().getWidth();
       setContentView(view);
       editCategory = (com.wyy.pay.view.ClearEditText) view.findViewById(R.id.editCategory);

       Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
       btnCancel.setOnClickListener(this);
       Button btnOK = (Button) view.findViewById(R.id.btnOK);
       btnOK.setOnClickListener(this);
       this.setWidth(screenW/2+Utils.dip2px(mActivity,70.0f));
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
            int xoff = (screenW -screenW/2 -Utils.dip2px(mActivity,70.0f))/2+Utils.dip2px(mActivity,10.0f);
            this.showAsDropDown(parent, xoff, -Utils.dip2px(mActivity,400.0f));
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
            case R.id.btnCancel:
                this.dismiss();
                break;
            case R.id.btnOK:
                String categoryName = editCategory.getText().toString().trim();
                if(TextUtils.isEmpty(categoryName)){
                    Toast.makeText(mActivity,"分类名称不能为空！",Toast.LENGTH_SHORT).show();
                }else {
                    mPopListener.btnOKOnClick(categoryName,fromType);
                    this.dismiss();
                }
                break;
        }
    }
    public void setAddCgOnClickListener(AddCgOnClickListener listener){
        this.mPopListener = listener;
    }
    public interface AddCgOnClickListener{
        public void btnOKOnClick(String categoryName,int fromType);
    }
}