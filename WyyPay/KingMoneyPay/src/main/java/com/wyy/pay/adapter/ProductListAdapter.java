package com.wyy.pay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.pay.R;
import com.wyy.pay.bean.TableGoodsDetailBean;
import com.wyy.pay.utils.BaseOptions;

import java.util.List;

/**
 * Created by liyusheng on 16/11/14.
 */

public class ProductListAdapter extends BaseAdapter{
    private List <TableGoodsDetailBean>productList;
    private Context mContext;
    private ProductItemOnClickListener itemOnClickListener;
    public void setProductItemOnClickListener(ProductItemOnClickListener listener){
        this.itemOnClickListener = listener;
    }
    public ProductListAdapter(Context context){
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
            convertView = View.inflate(mContext, R.layout.item_product_list, null);
            holder = new ViewHolder();
            holder.ivProImg = (ImageView) convertView.findViewById(R.id.ivProImg);
            holder.tvProName = (TextView) convertView.findViewById(R.id.tvProName);
            holder.tvStockCount = (TextView) convertView.findViewById(R.id.tvProStock);
            holder.tvProPrice = (TextView) convertView.findViewById(R.id.tvProPrice);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(bean.getGoodsImgUrl().contains("http:")||bean.getGoodsImgUrl().contains("file://")){

            ImageLoader.getInstance().displayImage(bean.getGoodsImgUrl(), holder.ivProImg,BaseOptions.getInstance().getProductImgOptions());
        }else {
            ImageLoader.getInstance().displayImage("file://"+bean.getGoodsImgUrl(), holder.ivProImg,BaseOptions.getInstance().getProductImgOptions());

        }
        holder.tvProName.setText(bean.getGoodsName());
        holder.tvProPrice.setText(String.valueOf(bean.getGoodsPrice()));

        holder.tvStockCount.setText(String.valueOf(bean.getGoodsStockCount()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemOnClickListener!=null){
                    itemOnClickListener.onItemClick(position,bean);
                }
            }
        });
        convertView.setTag(holder);

        return convertView;
    }
    public  interface ProductItemOnClickListener{
        void onItemClick(int position, TableGoodsDetailBean bean);
    }
    private static class ViewHolder{
        private ImageView ivProImg;
        private TextView tvProName;
        private TextView tvStockCount;
        private TextView tvProPrice;
    }
}
