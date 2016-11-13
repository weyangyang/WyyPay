package com.wyy.pay.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.pay.R;
import com.wyy.pay.bean.OrderCategoryBean;
import com.wyy.pay.bean.ProductBean;
import com.wyy.pay.utils.BaseOptions;

import java.util.List;

/**
 * Created by liyusheng on 16/11/13.
 */

public class OrderProductListAdapter extends BaseAdapter{
    private List <ProductBean>productList;
    private Context mContext;
    private OrderProductItemOnClickListener itemOnClickListener;
    public void setOrderProductItemOnClickListener(OrderProductItemOnClickListener listener){
        this.itemOnClickListener = listener;
    }
    public OrderProductListAdapter(Context context){
        this.mContext = context;
    }
    public void setProductListData(List<ProductBean> beanList){
        this.productList = beanList;
    }
    @Override
    public int getCount() {
        return (productList==null)?0:productList.size();
    }

    @Override
    public Object getItem(int position) {
        return (productList==null)?null:productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ViewHolder holder =null;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ProductBean bean  = (ProductBean) productList.get(position);
        if(convertView ==null){
            convertView = View.inflate(mContext, R.layout.item_order_product_list, null);
            holder = new ViewHolder();
            holder.ivOrderProImg = (ImageView) convertView.findViewById(R.id.ivOrderProImg);
            holder.tvOrderProName = (TextView) convertView.findViewById(R.id.tvOrderProName);
            holder.tvOrderAdd = (TextView) convertView.findViewById(R.id.tvOrderAdd);
            holder.tvItemOrderCount = (TextView) convertView.findViewById(R.id.tvItemOrderCount);
            holder.tvOrderReduce = (TextView) convertView.findViewById(R.id.tvOrderReduce);
            holder.tvItemOrderPrice = (TextView) convertView.findViewById(R.id.tvItemOrderPrice);
            convertView.setTag(holder);
            ImageLoader.getInstance().displayImage(bean.getImgUrl(), holder.ivOrderProImg,BaseOptions.getInstance().getProductImgOptions());
            holder.tvOrderProName.setText(bean.getProName());
            holder.tvItemOrderPrice.setText(String.valueOf(bean.getProPrice()));

            holder.tvOrderAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemOnClickListener!=null)
                        itemOnClickListener.addProOnClick(position,bean);
                }
            });
            holder.tvOrderReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemOnClickListener!=null)
                        itemOnClickListener.reduceProOnClick(position,bean);
                }
            });
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(0==bean.getAddProCount()){
            holder.tvItemOrderCount.setVisibility(View.GONE);
            holder.tvOrderReduce.setVisibility(View.GONE);
        }else {
            holder.tvItemOrderCount.setVisibility(View.INVISIBLE);
            holder.tvOrderReduce.setVisibility(View.INVISIBLE);
            holder.tvItemOrderCount.setText(String.valueOf(bean.getAddProCount()));
        }
        return convertView;
    }
    public  interface OrderProductItemOnClickListener{
        void addProOnClick(int position,ProductBean bean);
        void reduceProOnClick(int position,ProductBean bean);
    }
    private static class ViewHolder{
        private ImageView ivOrderProImg;
        private TextView tvOrderProName;
        private TextView tvOrderAdd;
        private TextView tvItemOrderCount;
        private TextView tvOrderReduce;
        private TextView tvItemOrderPrice;
    }
}
