package com.wyy.pay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wyy.pay.R;
import com.wyy.pay.bean.StatementsDiscountBean;

import java.util.ArrayList;
import java.util.List;


public  class StatementsDiscountAdapter extends RecyclerView.Adapter<StatementsDiscountAdapter.ViewHolder> {
    private List<StatementsDiscountBean> discountList;
    private Context mContext;
    private ItemOnClickListener listener;
    public void setDiscountListData(List<StatementsDiscountBean> discountList){
        if(this.discountList==null){
            this.discountList = new ArrayList<>();
        }
        this.discountList = discountList;
    }


        private LayoutInflater mInflater;

        public StatementsDiscountAdapter(Context context)
        {
            mInflater = LayoutInflater.from(context);
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public ViewHolder(View arg0)
            {
                super(arg0);
            }

            TextView tvDiscount;
        }

        @Override
        public int getItemCount()
        {
            return discountList==null?0:discountList.size();
        }

        /**
         * 创建ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View view = mInflater.inflate(R.layout.item_statements_discount,
                    viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.tvDiscount = (TextView) view
                    .findViewById(R.id.tvDiscount);
            return viewHolder;
        }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            final double number = discountList.get(position).getNumber();
        final int type = discountList.get(position).getType();
        String text = "";
        switch (type){
            case 1:
                text = String.format("%s折", String.format("%.1f",number));
                break;
            case 2:
                text = String.format("减%s元", String.format("%.2f",number));
                break;
            case 3:
                text = String.format("减%s元", String.format("%.2f",number));
                break;
        }
        holder.tvDiscount.setText(text);
        holder.tvDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(type,number);
                }
            }
        });
    }
    public void setItemOnClickListener(ItemOnClickListener listener){
        this.listener = listener;
    }
    public  interface ItemOnClickListener{
        void onItemClick(int type, double number);
    }
}


