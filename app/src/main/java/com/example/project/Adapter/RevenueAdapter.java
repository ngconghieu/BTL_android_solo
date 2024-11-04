package com.example.project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Object.Order;
import com.example.project.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RevenueAdapter extends RecyclerView.Adapter<RevenueAdapter.ItemViewHolder>{
    List<Order> orderList = new ArrayList<>();
    public void setData(List<Order> mList){
        orderList = mList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_revenue,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Order order = orderList.get(position);

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);
        if(order !=null){
            holder.tvId.setText("" + order.getOrderId());
            holder.tvDate.setText("" + order.getOrderTime());
            holder.tvPrice.setText(numberFormat.format(order.getTotalPrice()) + " VND");
        }

    }

    @Override
    public int getItemCount() {
        if(orderList!=null) return orderList.size();
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvDate, tvPrice;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_order_id);
            tvDate = itemView.findViewById(R.id.tv_order_date);
            tvPrice = itemView.findViewById(R.id.tv_price_order_revenue);
        }
    }
}
