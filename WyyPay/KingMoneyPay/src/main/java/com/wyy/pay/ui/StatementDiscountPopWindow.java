package com.wyy.pay.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.adapter.StatementsDiscountAdapter;
import com.wyy.pay.bean.StatementsDiscountBean;
import com.wyy.pay.bean.TableUserBean;
import com.wyy.pay.engine.RequestEngine;
import com.wyy.pay.engine.XTAsyncTask;
import com.wyy.pay.ui.dialog.CustomDialog;
import com.wyy.pay.utils.BaseOptions;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.ClearEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import netutils.engine.NetReqCallBack;
import xtcore.utils.PreferenceUtils;
import xtcore.utils.SystemUtils;

/**
 * Created by liyusheng on 16/11/24.
 */

public class StatementDiscountPopWindow extends PopupWindow implements StatementsDiscountAdapter.ItemOnClickListener {
    private Activity mActivity;
    private int popupWidth;
    private int popupHeight;
    private View parentView;
    private  View spaceView;
    private  RecyclerView rclZhenZ;
    private TextView tvDiscountTips;
    private TextView tvZhenZ;
    private View viewZhenZ;

    private  RecyclerView rclZhenJ;
    private TextView tvZhenJ;
    private View viewZhenJ;

    private ClearEditText edtZhenU;
    private Button btnZhenU;
    private RecyclerView rclZhenU;

    private ArrayList<StatementsDiscountBean> zheKList;
    private ArrayList<StatementsDiscountBean> zhenDList;
    private ArrayList<StatementsDiscountBean> serverList;
    private StatementsDiscountAdapter adapter1;
    private StatementsDiscountAdapter adapter2;
    private StatementsDiscountAdapter adapter3;
    private CustomDialog mCustomDialog;

    public void setDiscountListData(ArrayList<StatementsDiscountBean> zheKList,ArrayList<StatementsDiscountBean> zhenDList) {
        this.zheKList = zheKList;
        this.zhenDList = zhenDList;
        initData();
    }

    public StatementDiscountPopWindow(Activity activity){
       this.mActivity = activity;
        if(zheKList==null){
            zheKList = new ArrayList<>();
        }else {
            zheKList.clear();
        }
        if(zhenDList==null){
            zhenDList = new ArrayList<>();
        }else {
            zhenDList.clear();
        }
        if(serverList==null){
            serverList = new ArrayList<>();
        }else {
            serverList.clear();
        }
       initView();
       setPopConfig();
       initListener();

   }

    private void initData() {
        boolean isDiscount = PreferenceUtils.getPrefBoolean(mActivity,PreferenceUtils.SP_DISCOUNT_SWITCH,false);
            //显示tips
        tvDiscountTips.setVisibility(isDiscount?View.GONE:View.VISIBLE);
        if(zheKList.size()>0){
            adapter1.setDiscountListData(zheKList);
            adapter1.notifyDataSetChanged();
        }else {
            tvZhenZ.setVisibility(View.GONE);
            viewZhenZ.setVisibility(View.GONE);
            rclZhenZ.setVisibility(View.GONE);
        }
        if(zhenDList.size()>0){
            adapter2.setDiscountListData(zhenDList);
            adapter2.notifyDataSetChanged();
        }else {
            tvZhenJ.setVisibility(View.GONE);
            viewZhenJ.setVisibility(View.GONE);
            rclZhenJ.setVisibility(View.GONE);
        }
            rclZhenU.setVisibility(serverList.size()>0?View.VISIBLE:View.GONE);
    }

    private void initListener() {
        btnZhenU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String phone =   edtZhenU.getText().toString().trim();
               if(!TextUtils.isEmpty(phone)&& Utils.isPhoneNumber(phone)){
                   if(Utils.checkUserIsLogin(mActivity)){
                       String userName = BaseApplication.getUserName();
                       String wyyCode = BaseApplication.getWyyCode();
                       if(SystemUtils.checkAllNet(mActivity)){
                           getUserDiscountWithPhoneNum(phone,userName,wyyCode);
                       }else {
                           Toast.makeText(mActivity,mActivity.getString(R.string.text_net_error),Toast.LENGTH_SHORT).show();
                       }
                   }else {
                       Intent intent = new Intent(mActivity,LoginActivity.class);
                       mActivity.startActivity(intent);
                       StatementDiscountPopWindow.this.dismiss();
                   }



               }else {
                   Toast.makeText(mActivity,"您输入的手机号不正确，请重新输入！",Toast.LENGTH_SHORT).show();
               }
            }
        });
        spaceView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (StatementDiscountPopWindow.this.isShowing()) {
                    StatementDiscountPopWindow.this.dismiss();
                }
            }
        });
    }

    private void getUserDiscountWithPhoneNum(final String phone, final String userName, final String wyyCode) {
      new XTAsyncTask() {
          @Override
          protected void onPreExectue() {
              if(mCustomDialog==null)
                  mCustomDialog = CustomDialog.createLoadingDialog(mActivity,
                          "正在获取优惠券...", true);
          }

          @Override
          protected void doInbackgroud() {
              RequestEngine.getInstance().getDiscountWithPhone(userName, wyyCode, phone, new NetReqCallBack() {
                  @Override
                  public void getSuccData(int statusCode, final String strJson, String strUrl) {
                      mActivity.runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              try {
                                  JSONObject mJSONObject = new JSONObject(strJson);
                                  int code = mJSONObject.optInt("Code");
                                  String message = mJSONObject.optString("Message");
                                  if(code==1){
                                      //获取优惠券成功
                                      JSONArray array = mJSONObject.optJSONArray("Obj");
                                      if(array!=null&&array.length()>0){
                                          if(serverList==null){
                                              serverList = new ArrayList<StatementsDiscountBean>();
                                          }
                                          for (int i=0;i<array.length();i++){
                                              StatementsDiscountBean bean = new StatementsDiscountBean();
                                              JSONObject aObj = array.getJSONObject(i);
                                             double discount =  aObj.optDouble("value");
                                              bean.setNumber(discount);
                                              bean.setType(3);
                                              serverList.add(bean);
                                          }
                                          mActivity.runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  if(serverList.size()>0){
                                                      rclZhenU.setVisibility(View.VISIBLE);
                                                      adapter3.setDiscountListData(serverList);
                                                      adapter3.notifyDataSetChanged();
                                                  }
                                              }
                                          });

                                      }


                                  }else {
                                      if(!TextUtils.isEmpty(message))
                                          Toast.makeText(mActivity,message,Toast.LENGTH_SHORT).show();
                                  }
                              } catch (JSONException e) {
                                  Toast.makeText(mActivity,"服务器异常，请稍后再试",Toast.LENGTH_SHORT).show();
                              }
                          }
                      });

                  }
              });
          }

          @Override
          protected void onPostExecute() {
              if(null!=mActivity&& mCustomDialog.isShowing())
                  mCustomDialog.dismiss();
          }
      }.execute();
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
        popupHeight = parentView.getMeasuredHeight();
        popupWidth = parentView.getMeasuredWidth();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parentView= inflater.inflate(R.layout.popup_window_statements_discount,null);
        spaceView = parentView.findViewById(R.id.spaceView);





        tvDiscountTips = (TextView) parentView.findViewById(R.id.tvDiscountTips);
        tvZhenZ = (TextView) parentView.findViewById(R.id.tvZhenZ);
        viewZhenZ = parentView.findViewById(R.id.viewZhenZ);
        rclZhenZ = (RecyclerView) parentView.findViewById(R.id.rclZhenZ);
        adapter1 = new StatementsDiscountAdapter(mActivity);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mActivity);
        layoutManager1.setOrientation(OrientationHelper.HORIZONTAL);
        rclZhenZ.setLayoutManager(layoutManager1);
        rclZhenZ.setItemAnimator(new DefaultItemAnimator());
        rclZhenZ.setAdapter(adapter1);
        adapter1.setItemOnClickListener(this);

        tvZhenJ = (TextView) parentView.findViewById(R.id.tvZhenJ);
        viewZhenJ = parentView.findViewById(R.id.viewZhenJ);
        rclZhenJ = (RecyclerView) parentView.findViewById(R.id.rclZhenJ);
        adapter2 = new StatementsDiscountAdapter(mActivity);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(mActivity);
        layoutManager2.setOrientation(OrientationHelper.HORIZONTAL);
        rclZhenJ.setLayoutManager(layoutManager2);
        rclZhenJ.setItemAnimator(new DefaultItemAnimator());
        rclZhenJ.setAdapter(adapter2);
        adapter2.setItemOnClickListener(this);

        btnZhenU = (Button) parentView.findViewById(R.id.btnZhenU);
        edtZhenU = (ClearEditText) parentView.findViewById(R.id.edtZhenU);
        rclZhenU = (RecyclerView) parentView.findViewById(R.id.rclZhenU);
        adapter3 = new StatementsDiscountAdapter(mActivity);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(mActivity);
        layoutManager3.setOrientation(OrientationHelper.HORIZONTAL);
        rclZhenU.setLayoutManager(layoutManager3);
        rclZhenU.setItemAnimator(new DefaultItemAnimator());
        rclZhenU.setAdapter(adapter3);
        adapter3.setItemOnClickListener(this);
        setContentView(parentView);
    }

    public void showPopupWindow(View v) {
      //  int[] location = new int[2];
      //  v.getLocationOnScreen(location);
        showAsDropDown(v,0,Utils.dip2px(mActivity,8.0f));
        //在控件上方显示
       // showAtLocation(v, Gravity.NO_GRAVITY, (location[0]) - popupWidth / 2, location[1] - popupHeight);

        //这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以
//        showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight-Utils.dip2px(mActivity,48.0f));

        /**
         * //因为ppw提供了在某个控件下方的方法，所以有些时候需要直接定位在下方时并不用上面的这个方法
        showAsDropDown(v);    // 以触发弹出窗的view为基准，出现在view的正下方，弹出的pop_view左上角正对view的左下角  偏移量默认为0,0
         showAsDropDown(v, xoff, yoff);    // 有参数的话，就是一view的左下角进行偏移，xoff正的向左，负的向右. yoff没测，也应该是正的向下，负的向上
         showAsDropDown(parent, xoff, yoff, gravity) //parent:传你当前Layout的id; gravity:Gravity.BOTTOM（以屏幕左下角为参照）... 偏移量会以它为基准点 当x y为0,0是出现在底部居中
         */

    }
    private DiscountPopWindowListener listener;
    public void setDiscuntPopWListener(DiscountPopWindowListener listener){
        this.listener = listener;
    }
    public  interface DiscountPopWindowListener{
        void onItemSelected(int type, double number);
        void onDiscountPopWindowDismiss();
    }


    @Override
    public void dismiss() {
        super.dismiss();
        if(listener!=null){
            listener.onDiscountPopWindowDismiss();
        }
    }

    @Override
    public void onItemClick(int type, double number) {
        if(this.listener!=null){
            this.listener.onItemSelected(type,number);
        }
        dismiss();
    }
}
