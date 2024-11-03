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
import com.example.project.Object.Food;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemViewHolder>{
    List<Cart> mList;
    public void setData(List<Cart> List){
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

    DatabaseReference ref;
    FirebaseUser user;
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Cart cart = mList.get(position);
        user = FirebaseAuth.getInstance().getCurrentUser();

        int number = cart.getQuantity();
        String foodName = cart.getFoodName();
        int priceOrigin = cart.getSubPrice()/number;
        int subPrice = priceOrigin*number;

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);

        //lay image
        ref = FirebaseDatabase.getInstance().getReference("foods").child(foodName);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food food = snapshot.getValue(Food.class);
                String img = food.getImageFood().get(0);
                Glide.with(holder.itemView.getContext())
                        .load(img)
                        .into(holder.imgvCart);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //gan text
        holder.tvPrice.setText(numberFormat.format(subPrice) + " VND");
        holder.tvName.setText(foodName);
        holder.btnNumberCart.setText(String.valueOf(number));

        //eventListener
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
                                    ref.child(user.getUid()).child(foodName).removeValue();
                                }
                            }).setNegativeButton("No",null).show();
                }
            }
        });
        holder.btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && cart.getQuantity() > 1) {
                    cart.setQuantity(cart.getQuantity() - 1);
                    cart.setSubPrice(priceOrigin * cart.getQuantity());
                    holder.btnNumberCart.setText(String.valueOf(cart.getQuantity()));
                    holder.tvPrice.setText(numberFormat.format(cart.getSubPrice()) + " VND");
                    updateFirebase(foodName, cart.getQuantity(), cart.getSubPrice());
                }
            }
        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    cart.setQuantity(cart.getQuantity() + 1);
                    cart.setSubPrice(priceOrigin * cart.getQuantity());
                    holder.btnNumberCart.setText(String.valueOf(cart.getQuantity()));
                    holder.tvPrice.setText(numberFormat.format(cart.getSubPrice()) + " VND");
                    updateFirebase(foodName, cart.getQuantity(), cart.getSubPrice());
                }
            }
        });
    }

    private void updateFirebase(String foodName, int quantity, int subPrice) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cart");
        ref.child(user.getUid()).child(foodName).child("quantity").setValue(quantity);
        ref.child(user.getUid()).child(foodName).child("subPrice").setValue(subPrice);
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
