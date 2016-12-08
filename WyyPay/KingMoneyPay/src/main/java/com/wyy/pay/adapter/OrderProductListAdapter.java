package com.wyy.pay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.pay.R;
import com.wyy.pay.bean.TableGoodsDetailBean;
import com.wyy.pay.utils.BaseOptions;

import java.util.List;

/**
 * Created by liyusheng on 16/11/13.
 */

public class OrderProductListAdapter extends BaseAdapter{
    private List <TableGoodsDetailBean>productList;
    private Context mContext;
    private OrderProductItemOnClickListener itemOnClickListener;
    public void setOrderProductItemOnClickListener(OrderProductItemOnClickListener listener){
        this.itemOnClickListener = listener;
    }
    public OrderProductListAdapter(Context context){
        this.mContext = context;
    }
    public void setProductListData(List<TableGoodsDetailBean> beanList){
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
        final TableGoodsDetailBean bean  = (TableGoodsDetailBean) productList.get(position);

        if(convertView ==null){
            convertView = View.inflate(mContext, R.layout.item_order_product_list, null);
            holder = new ViewHolder();
            holder.ivOrderProImg = (ImageView) convertView.findViewById(R.id.ivOrderProImg);
            holder.tvOrderProName = (TextView) convertView.findViewById(R.id.tvOrderProName);
            holder.tvOrderAdd = (Button) convertView.findViewById(R.id.tvOrderAdd);
            holder.tvItemOrderCount = (TextView) convertView.findViewById(R.id.tvItemOrderCount);
            holder.tvOrderReduce = (Button) convertView.findViewById(R.id.tvOrderReduce);
            holder.tvItemOrderPrice = (TextView) convertView.findViewById(R.id.tvItemOrderPrice);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(bean.getGoodsImgUrl().contains("http:")||bean.getGoodsImgUrl().contains("file://")){
            ImageLoader.getInstance().displayImage(bean.getGoodsImgUrl(), holder.ivOrderProImg,BaseOptions.getInstance().getProductImgOptions());
        }else {
            ImageLoader.getInstance().displayImage("file://"+bean.getGoodsImgUrl(), holder.ivOrderProImg,BaseOptions.getInstance().getProductImgOptions());
        }
        holder.tvOrderProName.setText(bean.getGoodsName());
        holder.tvItemOrderPrice.setText(String.format("￥%.2f",bean.getGoodsPrice()));
        if(bean.getAddGoodsCount()>0){
            holder.tvOrderReduce.setEnabled(true);
            //设置不同的背景色
            holder.tvOrderReduce.setBackgroundResource(R.drawable.ic_jianhao_cheng);
        }else {
            holder.tvOrderReduce.setBackgroundResource(R.drawable.ic_jianhao_hui);
            holder.tvOrderReduce.setEnabled(false);
            //设置不同的背景色

        }
        holder.tvItemOrderCount.setText(String.valueOf(bean.getAddGoodsCount()));
        holder.tvOrderAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemOnClickListener!=null)
//                    bean.setAddGoodsCount(goodsCount);
                    itemOnClickListener.addProOnClick(position,bean);
            }
        });
        holder.tvOrderReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemOnClickListener!=null)
//                    bean.setAddGoodsCount(goodsCount);
                    itemOnClickListener.reduceProOnClick(position,bean);
            }
        });
        convertView.setTag(holder);
        return convertView;
    }
    public  interface OrderProductItemOnClickListener{
        void addProOnClick(int position,TableGoodsDetailBean bean);
        void reduceProOnClick(int position,TableGoodsDetailBean bean);
    }
    private static class ViewHolder{
        private ImageView ivOrderProImg;
        private TextView tvOrderProName;
        private Button tvOrderAdd;
        private TextView tvItemOrderCount;
        private Button tvOrderReduce;
        private TextView tvItemOrderPrice;
    }
}
