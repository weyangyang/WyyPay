package com.wyy.pay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wyy.pay.R;
import com.wyy.pay.bean.TableCategoryBean;

import java.util.List;

/**
 * Created by liyusheng on 16/11/14.
 */

public class ProCategoryManageListAdapter extends BaseAdapter{
    private List categoryList;
    private Context mContext;
    private boolean isEdit=false;//是否是编辑状态
    private CManageItemOnClickListener listener;
    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
    public void setCManageItemOnClickListener(CManageItemOnClickListener mCManageItemOnClickListener){
        this.listener = mCManageItemOnClickListener;
    }
    public ProCategoryManageListAdapter(Context context){
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
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_category_manage_list, null);
            holder.tvCategoryName = (TextView) convertView.findViewById(R.id.tvCategoryName);
            holder.btnEditCategory = (Button) convertView.findViewById(R.id.btnEditCategory);
            holder.btnDeleteCategory = (Button) convertView.findViewById(R.id.btnDeleteCategory);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TableCategoryBean bean  = (TableCategoryBean) categoryList.get(position);
        holder.btnDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.btnDeleteOnClick(position,bean);
//                   ProCategoryBean bb = (ProCategoryBean) categoryList.get(position);
//                    String str =bb.toString();
//                categoryList.remove(position);
//                ProCategoryManageListAdapter.this.notifyDataSetChanged();
            }
        });
        holder.btnEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.btnEditOnClick(position,bean);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onItemViewClick(position,bean);
            }
        });
        holder.tvCategoryName.setText(bean.getCategoryName());
        if(isEdit && position!=0){
            holder.btnEditCategory.setVisibility(View.VISIBLE);
            holder.btnDeleteCategory.setVisibility(View.VISIBLE);
        }else {
            holder.btnEditCategory.setVisibility(View.GONE);
            holder.btnDeleteCategory.setVisibility(View.GONE);
        }
        return convertView;
    }
    public  interface CManageItemOnClickListener{
         void onItemViewClick(int position, TableCategoryBean bean);
        void  btnDeleteOnClick(int position,TableCategoryBean bean);
        void  btnEditOnClick(int position,TableCategoryBean bean);
    }
    private static class ViewHolder{
        private TextView tvCategoryName;
        private Button btnDeleteCategory;
        private Button btnEditCategory;
    }
}
