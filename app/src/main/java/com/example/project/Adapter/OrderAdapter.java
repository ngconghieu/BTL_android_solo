package com.example.project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Object.Cart;
import com.example.project.Object.Order;
import com.example.project.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ItemViewHolder> {
    List<Order> orderList;
    public void setData(List<Order> mList){
        orderList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_order,parent,false);
        return new ItemViewHolder(view);
    }

    NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText(order.getOrderId());
        holder.tvOrdering.setText(order.getOrderingMethod());
        holder.tvDate.setText(order.getOrderTime());
        holder.tvPayment.setText(order.getPayment());
        holder.tvName.setText(order.getAddress().getUserName());
        holder.tvPhoneNumber.setText(order.getAddress().getPhoneNumber());
        holder.tvAddress.setText(order.getAddress().getAddress());
        holder.tvPrice.setText(numberFormat.format(order.getTotalPrice())+" VND");

        String name,
                mCart = "";
        int quantity, subPrice;

        List<Cart> cartList = order.getCart();
        for(Cart ca:cartList){
            name = ca.getFoodName();
            quantity = ca.getQuantity();
            subPrice =Integer.parseInt(numberFormat.format(ca.getSubPrice()));
            mCart +=" - "+name + " ("+subPrice+" VND) - Quantity: "+quantity+"\n";
        }
        holder.tvCart.setText(mCart);
    }

    @Override
    public int getItemCount() {
        if(orderList!=null)return orderList.size();
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tvOrderId, tvName, tvPhoneNumber,tvAddress, tvOrdering,
            tvDate, tvPrice, tvPayment, tvCart;
        private Button btnReject, btnAccept;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_code);
            tvName = itemView.findViewById(R.id.tv_full_name_order);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number_order);
            tvAddress = itemView.findViewById(R.id.tv_address_order);
            tvOrdering = itemView.findViewById(R.id.tv_ordering);
            tvDate = itemView.findViewById(R.id.tv_date_time_order);
            tvPrice = itemView.findViewById(R.id.tv_price_order);
            tvPayment = itemView.findViewById(R.id.tv_payment_order);
            btnReject = itemView.findViewById(R.id.btn_reject_order);
            btnAccept = itemView.findViewById(R.id.btn_accept_order);
            tvCart = itemView.findViewById(R.id.tv_food_cart_order);
        }
    }
}
