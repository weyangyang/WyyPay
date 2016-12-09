package com.wyy.pay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyy.pay.R;
import com.wyy.pay.bean.TableCategoryBean;
import com.wyy.pay.bean.TableGoodsDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyusheng on 16/12/9.
 */

public class StatementsListAdapter extends BaseAdapter{
    private List<TableGoodsDetailBean> goodsList;
    private Context mContext;
    public StatementsListAdapter(Context context){
        this.mContext = context;
    }
    public void setGoodsListData(List<TableGoodsDetailBean> goodsList){
        if(this.goodsList==null){
            this.goodsList = new ArrayList<>();
        }
        this.goodsList = goodsList;
    }
    @Override
    public int getCount() {
        return (goodsList==null)?0:goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return (goodsList==null)?null:goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ViewHolder holder =null;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView ==null){
            convertView = View.inflate(mContext, R.layout.item_statements, null);
            holder = new ViewHolder();
            holder.itemGoodsName = (TextView) convertView.findViewById(R.id.tvItemGoodsName);
            holder.itemGoodsPrice = (TextView) convertView.findViewById(R.id.tvItemGoodsPrice);
            holder.itemGoodsCount = (TextView) convertView.findViewById(R.id.tvItemGoodsCount);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String goodsName = goodsList.get(position).getGoodsName();
            holder.itemGoodsName.setText(goodsName);
        double goodsPrice = goodsList.get(position).getGoodsPrice();
            holder.itemGoodsPrice.setText(String.format("￥%.2f",goodsPrice));
            int count = goodsList.get(position).getAddGoodsCount();
        holder.itemGoodsCount.setText(String.valueOf(count));
        convertView.setTag(holder);
        return convertView;
    }

    private static class ViewHolder{
        private TextView itemGoodsName;
        private TextView itemGoodsPrice;
        private TextView itemGoodsCount;
    }
}
