package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<CartItem> cartItems;

    public CartAdapter(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.foodName.setText(item.getName());
        holder.foodPrice.setText(item.getPrice() + " VNĐ");
        holder.imageView.setImageResource(item.getImageResource());

        holder.increaseButton.setOnClickListener(v -> {
            item.increaseQuantity();
            holder.foodPrice.setText(item.getTotalPrice() + " VNĐ"); // Cập nhật giá
            notifyItemChanged(position);
        });

        holder.decreaseButton.setOnClickListener(v -> {
            item.decreaseQuantity();
            holder.foodPrice.setText(item.getTotalPrice() + " VNĐ"); // Cập nhật giá
            notifyItemChanged(position);
        });

        holder.deleteButton.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size()); // Cập nhật lại danh sách
        });


        // Xử lý sự kiện nút tăng/giảm số lượng và xóa món
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice;
        ImageView imageView;
        Button deleteButton, increaseButton, decreaseButton;

        public ViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            imageView = itemView.findViewById(R.id.imageView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
        }
    }
}
