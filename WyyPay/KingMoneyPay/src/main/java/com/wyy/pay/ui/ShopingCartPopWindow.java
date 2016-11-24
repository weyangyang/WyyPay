package com.wyy.pay.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * Created by liyusheng on 16/11/24.
 */

public class ShopingCartPopWindow extends PopupWindow implements OrderProductListAdapter.OrderProductItemOnClickListener {
    private Activity mActivity;
    private int popupWidth;
    private int popupHeight;
    private View parentView;
    private  View spaceView;
    private ImageView ivShopingCart;
    private XListView xlistView;//购物车列表
    private TextView tvClearCartList;//清空列表
    private TextView tvSumShopNum;//显示购物数量
    private ArrayList<TableGoodsDetailBean> goodsListData;
    private OrderProductListAdapter adapter;
    private ShopingCartPopWindowListener cartListener;

    public void setCartListener(ShopingCartPopWindowListener cartListener) {
        this.cartListener = cartListener;
    }

    public void setGoodsListData(ArrayList<TableGoodsDetailBean> goodsListData) {
        this.goodsListData = goodsListData;
        initData();
    }

    public ShopingCartPopWindow(Activity activity){
       this.mActivity = activity;
       initView();
       setPopConfig();
       initListener();

   }

    private void initData() {
        if(goodsListData==null){
            goodsListData = new ArrayList<>();
        }
        if(adapter==null){
            adapter = new OrderProductListAdapter(mActivity);
            xlistView.setAdapter(adapter);
        }
        if(goodsListData.size()>0){
            int totalShopingNum =0;
            for (TableGoodsDetailBean goodsDetailBean : goodsListData){
                totalShopingNum+= goodsDetailBean.getAddGoodsCount();
            }
            if (totalShopingNum > 0) {
                tvSumShopNum.setVisibility(View.VISIBLE);
                tvSumShopNum.setText(String.valueOf(totalShopingNum));
            }else {
                tvSumShopNum.setVisibility(View.GONE);
            }
        }
        adapter.setProductListData(goodsListData);
        adapter.notifyDataSetChanged();
    }

    private void initListener() {
        tvClearCartList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //弹出框
                CustomDialog customDialog = new CustomDialog(mActivity,R.style.DefaultDialog, new CustomDialog.InfoCallback() {
                    @Override
                    public void btnOkOnClick() {
                        if(cartListener!=null){
                            cartListener.clearCartList();
                        }
                        ShopingCartPopWindow.this.dismiss();
                    }

                });
                customDialog.show();

            }
        });
        ivShopingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopingCartPopWindow.this.dismiss();
            }
        });
        adapter = new OrderProductListAdapter(mActivity);
        xlistView.setAdapter(adapter);
        xlistView.setPullLoadEnable(false);
        xlistView.setPullRefreshEnable(false);
        adapter.setOrderProductItemOnClickListener(this);
        spaceView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (ShopingCartPopWindow.this.isShowing()) {
                    ShopingCartPopWindow.this.dismiss();
                }
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
        popupHeight = parentView.getMeasuredHeight();
        popupWidth = parentView.getMeasuredWidth();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        parentView= inflater.inflate(R.layout.popup_window_shoping_cart,null);
        spaceView = parentView.findViewById(R.id.spaceView);
        ivShopingCart = (ImageView) parentView.findViewById(R.id.ivShopingCart);
        tvSumShopNum = (TextView) parentView.findViewById(R.id.tvSumShopNum);
        tvClearCartList = (TextView) parentView.findViewById(R.id.tvClearCartList);
        xlistView = (XListView) parentView.findViewById(R.id.xlvShopingCart);
        setContentView(parentView);
    }

    public void showPopupWindow(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        showAsDropDown(v, 0,  -location[1]-Utils.dip2px(mActivity,88.5f));
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


    @Override
    public void addProOnClick(int position, TableGoodsDetailBean bean) {
        if(cartListener!=null){
            int goodsCount = bean.getAddGoodsCount();
            goodsCount+=1;
            bean.setAddGoodsCount(goodsCount);
            cartListener.cartItemAddOnClick(position,bean);
        }
    }

    @Override
    public void reduceProOnClick(int position, TableGoodsDetailBean bean) {
        if(cartListener!=null){
            int goodsCount = bean.getAddGoodsCount();
            goodsCount-=1;
            bean.setAddGoodsCount(goodsCount);
            cartListener.cartItemReduceOnClick(position,bean);
        }
    }

    @Override
    public void dismiss() {
        if(cartListener!=null){
            cartListener.onCartWindowDismiss();
        }
        super.dismiss();
    }

    //    /**
//     * 设置显示在v上方（以v的中心位置为开始位置）
//     * @param v
//     */
//    public void showPopupWindowUp(View v) {
//        //获取需要在其上方显示的控件的位置信息
//        int[] location = new int[2];
//        v.getLocationOnScreen(location);
//        //在控件上方显示
//        showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
//    }
    public interface ShopingCartPopWindowListener{
        void onCartWindowDismiss();
        void cartItemAddOnClick(int position, TableGoodsDetailBean bean);
        void cartItemReduceOnClick(int position, TableGoodsDetailBean bean);
        void clearCartList();
    }
}
