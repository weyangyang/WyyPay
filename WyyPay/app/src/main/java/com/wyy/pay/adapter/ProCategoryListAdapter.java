package com.wyy.pay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyy.pay.R;
import com.wyy.pay.bean.ProCategoryBean;

import java.util.List;

/**
 * Created by liyusheng on 16/11/12.
 */

public class ProCategoryListAdapter extends BaseAdapter{
    private List categoryList;
    private Context mContext;
    private int currentPosition;
    public void setCurrentPosition(int position){
        this.currentPosition = position;
    }
    public ProCategoryListAdapter(Context context){
        this.mContext = context;
    }
    public void setCategoryListData(List<ProCategoryBean> beanList){
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
            convertView = View.inflate(mContext, R.layout.item_pro_category_list, null);
            holder = new ViewHolder();
            holder.itemCategoryView = (TextView) convertView.findViewById(R.id.tvCategoryItemView);
            holder.item_category_main_rl = (RelativeLayout) convertView.findViewById(R.id.item_category_main_rl);
            convertView.setTag(holder);
            final ProCategoryBean bean  = (ProCategoryBean) categoryList.get(position);
            holder.itemCategoryView.setText(bean.getCategoryName());

//            holder.itemCategoryView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(itemOnClickListener!=null)
//                        itemOnClickListener.categoryItemOnClick( position,bean.getCategoryId(),bean.getCategoryName());
//                }
//            });
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
    public  interface CategoryItemOnClickListener{
         void categoryItemOnClick(int position,String categoryId,String categoryName);
    }
    private static class ViewHolder{
        private TextView itemCategoryView;
        private RelativeLayout item_category_main_rl;
    }
}
