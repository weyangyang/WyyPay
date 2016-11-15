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

public class ProManagePopWindow extends PopupWindow implements View.OnClickListener {
    private Activity mActivity;
    private ProMpopWindowOnClickListener mPopListener;
    private  int screenW;
   public ProManagePopWindow(Activity activity){
       this.mActivity = activity;
       LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View view = inflater.inflate(R.layout.pop_window_pro_manage,null);
        screenW = mActivity.getWindowManager().getDefaultDisplay().getWidth();
       setContentView(view);
       TextView tvAddPro = (TextView) view.findViewById(R.id.tvPopAddPro);
       tvAddPro.setOnClickListener(this);
       TextView tvEditPro = (TextView) view.findViewById(R.id.tvEditPro);
       tvEditPro.setOnClickListener(this);
       TextView tvAddCategory = (TextView) view.findViewById(R.id.tvAddCategory);
       tvAddCategory.setOnClickListener(this);
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
            case R.id.tvPopAddPro:
                mPopListener.tvAddProOnClick();
                this.dismiss();
                break;
            case R.id.tvEditPro:
                mPopListener.tvEditProOnClick();
                this.dismiss();
                break;
            case R.id.tvAddCategory:
                mPopListener.tvCategoryManageOnClick();
                this.dismiss();
                break;
        }
    }
    public void setProMpopWindowOnClickListener(ProMpopWindowOnClickListener listener){
        this.mPopListener = listener;
    }
    public interface ProMpopWindowOnClickListener{
        public void tvAddProOnClick();
        public void tvEditProOnClick();
        public void tvCategoryManageOnClick();
    }
}