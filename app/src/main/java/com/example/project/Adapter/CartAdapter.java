package com.example.project.Adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Object.Cart;
import com.example.project.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemViewHolder>{
    List<Cart> mList;
    private void setData(List<Cart> List){
        mList = List;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_cart,parent,false);
        return new ItemViewHolder(view);
    }

    int number, price;
    int subPrice, priceOrigin;
    String img;
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Cart cart = mList.get(position);

//        number = cart.getQuantity();
//        price = cart.getSubPrice()
//        subPrice = price*number;
//        img = cart.getFood().getImageFood().get(0);
//        priceOrigin = price;
//
//        holder.tvName.setText(cart.getFood().getFoodName());
//        NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);
//        holder.btnNumberCart.setText(String.valueOf(number));
//        holder.tvPrice.setText(numberFormat.format(subPrice)+" VND");
//
//        Glide.with(holder.itemView.getContext())
//                .load(img)
//                .into(holder.imgvCart);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if(pos !=RecyclerView.NO_POSITION){
                    new AlertDialog.Builder(holder.itemView.getContext())
                            .setTitle("Delete food")
                            .setMessage("Are you sure to delete this food?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mList.remove(pos);
                                    notifyItemRemoved(pos);
                                    notifyItemRangeChanged(pos,mList.size());
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cart");

                                }
                            });
                }
            }
        });
        holder.btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number > 1) {
                    number -= 1;
                    subPrice = priceOrigin *number;
                    holder.btnNumberCart.setText(String.valueOf(number));
//                    holder.tvPrice.setText(numberFormat.format(subPrice) + " VND");
                }
            }
        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number += 1;
                subPrice = priceOrigin *number;
                holder.btnNumberCart.setText(String.valueOf(number));
//                holder.tvPrice.setText(numberFormat.format(subPrice)+ " VND");
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mList!=null) return mList.size();
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgvCart;
        private TextView tvName, tvPrice;
        private Button btnNumberCart, btnAdd, btnSub, btnDelete;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgvCart = itemView.findViewById(R.id.imgv_cart);
            tvName = itemView.findViewById(R.id.tv_food_name_cart);
            tvPrice = itemView.findViewById(R.id.tv_price_cart);
            btnNumberCart = itemView.findViewById(R.id.btn_number_cart);
            btnAdd = itemView.findViewById(R.id.btn_add_cart);
            btnDelete = itemView.findViewById(R.id.btn_delete_food);
            btnSub = itemView.findViewById(R.id.btn_sub_cart);
        }
    }
}
