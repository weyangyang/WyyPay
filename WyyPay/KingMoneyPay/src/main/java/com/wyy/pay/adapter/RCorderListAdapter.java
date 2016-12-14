package com.wyy.pay.adapter;
import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

import com.wyy.pay.R;
import com.wyy.pay.bean.OrderListDataBean;

import java.util.ArrayList;

/**
 * Created by liyusheng on 16/12/13.
 */
public class RCorderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    public enum ITEM_TYPE {
        ITEM1,
        ITEM2
    }
    private ArrayList<OrderListDataBean> orderList;
    public void setListData(ArrayList<OrderListDataBean> orderList){
        this.orderList = orderList;
    }
    public RCorderListAdapter(Context context){
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM1.ordinal()) {
            return new Item1ViewHolder(mLayoutInflater.inflate(R.layout.item_order_list_title, parent, false));
        } else {
            return new Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_order_list_item_money, parent, false));
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderListDataBean bean = orderList.get(position);
        if (holder instanceof Item1ViewHolder) {
            ((Item1ViewHolder) holder).itemDate.setText(bean.getCreateDate());
            ((Item1ViewHolder) holder).itemTotalMoney.setText(String.format("￥%s",bean.getMoney()));
        } else if (holder instanceof Item2ViewHolder) {
            switch (bean.getPayType()){
                case 1:
                    ((Item2ViewHolder) holder).itemTypeLogo.setBackgroundResource(R.drawable.icon_bill_wechat);
                    ((Item2ViewHolder) holder).itemTypeTitle.setText("微信收款");
                    ((Item2ViewHolder) holder).itemTypeTime.setText(bean.getCreateDate());
                    ((Item2ViewHolder) holder).itemTotalMoney.setText(String.format("￥%s",bean.getMoney()));

                    break;
                case 2:
                    ((Item2ViewHolder) holder).itemTypeLogo.setBackgroundResource(R.drawable.icon_bill_alipay);
                    ((Item2ViewHolder) holder).itemTypeTitle.setText("支付宝收款");
                    ((Item2ViewHolder) holder).itemTypeTime.setText(bean.getCreateDate());
                    ((Item2ViewHolder) holder).itemTotalMoney.setText(String.format("￥%s",bean.getMoney()));
                    break;
                case 3:
                    ((Item2ViewHolder) holder).itemTypeLogo.setBackgroundResource(R.drawable.icon_bill_cash);
                    ((Item2ViewHolder) holder).itemTypeTitle.setText("现金收款");
                    ((Item2ViewHolder) holder).itemTypeTime.setText(bean.getCreateDate());
                    ((Item2ViewHolder) holder).itemTotalMoney.setText(String.format("￥%s",bean.getMoney()));
                    break;
            }

        }
    }
    @Override
    public int getItemViewType(int position) {
        if(orderList==null||orderList.size()==0){
            return 0;
        }
        return orderList.get(position).isTitle()? ITEM_TYPE.ITEM1.ordinal() : ITEM_TYPE.ITEM2.ordinal();
    }
    @Override
    public int getItemCount() {
        return (orderList == null||orderList.size()==0) ? 0 : orderList.size();
    }
    //item1 的ViewHolder
    public static class Item1ViewHolder extends RecyclerView.ViewHolder{
        TextView itemDate;
        TextView itemTotalMoney;
        public Item1ViewHolder(View itemView) {
            super(itemView);
            itemDate=(TextView)itemView.findViewById(R.id.itemDate);
            itemTotalMoney=(TextView)itemView.findViewById(R.id.itemTotalMoney);
        }
    }
    //item2 的ViewHolder
    public static class Item2ViewHolder extends RecyclerView.ViewHolder{
        TextView itemTypeLogo;
        TextView itemTypeTitle;
        TextView itemTypeTime;
        TextView itemTotalMoney;
        public Item2ViewHolder(View itemView) {
            super(itemView);
            itemTypeLogo=(TextView)itemView.findViewById(R.id.itemTypeLogo);
            itemTypeTitle=(TextView)itemView.findViewById(R.id.itemTypeTitle);
            itemTypeTime=(TextView)itemView.findViewById(R.id.itemTypeTime);
            itemTotalMoney=(TextView)itemView.findViewById(R.id.itemTotalMoney);
        }
    }
}