package com.wyy.pay.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wyy.pay.R;
import com.wyy.pay.bean.TableCategoryBean;

import java.util.List;

/**
 * Created by liyusheng on 16/11/12.
 */

public class OrderCategoryListAdapter extends BaseAdapter{
    private List categoryList;
    private Context mContext;
    private int currentPosition;
    private ProCategoryListAdapter.CategoryItemOnClickListener itemOnClickListener;
    public void setItemOnClickListener(ProCategoryListAdapter.CategoryItemOnClickListener itemOnClickListener){
        this.itemOnClickListener = itemOnClickListener;
    }
    public void setCurrentPosition(int position){
        this.currentPosition = position;
    }
    public OrderCategoryListAdapter(Context context){
        this.mContext = context;
    }
    public void setCategoryListData(List<TableCategoryBean> beanList){
        this.categoryList = beanList;
    }
    @Override
    public int getCount() {
        return (categoryList==null)?0:categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return (categoryList==null)?null:categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ViewHolder holder =null;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView ==null){
            convertView = View.inflate(mContext, R.layout.item_order_category_list, null);
            holder = new ViewHolder();
            holder.tvOrderCategoryItemView = (TextView) convertView.findViewById(R.id.tvOrderCategoryItemView);
            holder.tvOrderSumCount = (TextView) convertView.findViewById(R.id.tvOrderSumCount);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TableCategoryBean bean  = (TableCategoryBean) categoryList.get(position);
        holder.tvOrderCategoryItemView.setText(bean.getCategoryName());
        if(TextUtils.isEmpty(bean.getProSumCount()+"")){
            holder.tvOrderSumCount.setVisibility(View.GONE);
        }else if(0==bean.getProSumCount()){
            holder.tvOrderSumCount.setVisibility(View.GONE);
        }else {
            holder.tvOrderSumCount.setVisibility(View.VISIBLE);
            holder.tvOrderSumCount.setText(String.valueOf(bean.getProSumCount()));

        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemOnClickListener!=null)
                    itemOnClickListener.categoryItemOnClick( position,bean.getCategoryId(),bean.getCategoryName());
            }
        });
        if (position == currentPosition) {
            convertView.setBackgroundResource(R.drawable.item_category_selected);
        }
        else {
            convertView.setBackgroundResource(R.drawable.item_category_normal);
        }
        convertView.setTag(holder);
        return convertView;
    }
    public  interface CategoryItemOnClickListener{
         void categoryItemOnClick(int position, String categoryId, String categoryName);
    }
    private static class ViewHolder{
        private TextView tvOrderCategoryItemView;
        private TextView tvOrderSumCount;
    }
}
