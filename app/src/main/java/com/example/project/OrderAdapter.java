package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderCode.setText("Order code: " + order.getOrderCode());
        holder.name.setText("Name: " + order.getName());
        holder.phone.setText("Number Phone: " + order.getPhone());
        holder.address.setText("Addresses: " + order.getAddress());
        holder.billDetails.setText("Bill: " + order.getBillDetails());
        holder.date.setText("Date: " + order.getDate());
        holder.totalBill.setText("Total Bill: " + order.getTotalBill());
        holder.paymentMethod.setText("Payment: " + order.getPaymentMethod());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderCode, name, phone, address, billDetails, date, totalBill, paymentMethod;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderCode = itemView.findViewById(R.id.orderCode);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            address = itemView.findViewById(R.id.address);
            billDetails = itemView.findViewById(R.id.billDetails);
            date = itemView.findViewById(R.id.date);
            totalBill = itemView.findViewById(R.id.totalBill);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
        }
    }
}
