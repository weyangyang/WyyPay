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
import com.wyy.pay.bean.ProductBean;
import com.wyy.pay.utils.BaseOptions;

import java.util.List;

/**
 * Created by liyusheng on 16/11/14.
 */

public class ProductListAdapter extends BaseAdapter{
    private List <ProductBean>productList;
    private Context mContext;
    private ProductItemOnClickListener itemOnClickListener;
    public void setProductItemOnClickListener(ProductItemOnClickListener listener){
        this.itemOnClickListener = listener;
    }
    public ProductListAdapter(Context context){
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
            convertView = View.inflate(mContext, R.layout.item_product_list, null);
            holder = new ViewHolder();
            holder.ivProImg = (ImageView) convertView.findViewById(R.id.ivProImg);
            holder.tvProName = (TextView) convertView.findViewById(R.id.tvProName);
            holder.tvStockCount = (TextView) convertView.findViewById(R.id.tvProStock);
            holder.tvProPrice = (TextView) convertView.findViewById(R.id.tvProPrice);

            ImageLoader.getInstance().displayImage(bean.getImgUrl(), holder.ivProImg,BaseOptions.getInstance().getProductImgOptions());
            holder.tvProName.setText(bean.getProName());
            holder.tvProPrice.setText(String.valueOf(bean.getProPrice()));

            holder.tvStockCount.setText(String.valueOf(bean.getProStockCount()));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemOnClickListener!=null){
                        itemOnClickListener.onItemClick(position,bean);
                    }
                }
            });
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
    public  interface ProductItemOnClickListener{
        void onItemClick(int position, ProductBean bean);
    }
    private static class ViewHolder{
        private ImageView ivProImg;
        private TextView tvProName;
        private TextView tvStockCount;
        private TextView tvProPrice;
    }
}
