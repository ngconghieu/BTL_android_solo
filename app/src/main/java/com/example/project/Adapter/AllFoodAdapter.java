package com.example.project.Adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.FoodDetailsActivity;
import com.example.project.Object.Food;
import com.example.project.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AllFoodAdapter extends  RecyclerView.Adapter<AllFoodAdapter.AllFoodViewHolder>{
    private List<Food> food;
    public void setData(List<Food> mFood){
        food = mFood;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AllFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_all_food,parent,false);
        return new AllFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllFoodViewHolder holder, int position) {
        Food mFood = food.get(position);
        if(mFood==null) return;
        String foodName = mFood.getFoodName();
        int discount = mFood.getDiscount(),
                price = mFood.getPrice();
        int totalPrice = price-discount;
        if(discount!=0) holder.tvDiscount.setVisibility(View.VISIBLE);
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);

        holder.tvDiscount.setText(numberFormat.format(price)+ " VND");
        holder.tvPrice.setText(numberFormat.format(totalPrice)+" VND");
        holder.tvFoodName.setText(foodName);

        Glide.with(holder.itemView.getContext())
                .load(mFood.getImageFood().get(0))
                .into(holder.imgvAllFood);
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.itemView.getContext(), FoodDetailsActivity.class);
                i.putExtra("foodName",mFood.getFoodName());
                i.putExtra("price", mFood.getPrice());
                i.putExtra("discount",mFood.getDiscount());
                i.putExtra("details",mFood.getDetails());


                i.putStringArrayListExtra("imageFood",new ArrayList<>(mFood.getImageFood()));

                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(food!=null) return food.size();
        return 0;
    }

    public class AllFoodViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgvAllFood;
        private TextView tvFoodName, tvDiscount, tvPrice;
        public AllFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgvAllFood = itemView.findViewById(R.id.imgv_all_food);
            tvFoodName = itemView.findViewById(R.id.tv_food_name_home);
            tvDiscount = itemView.findViewById(R.id.tv_food_discount_home);
            tvDiscount.setPaintFlags(tvDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvDiscount.setVisibility(View.GONE);
            tvPrice = itemView.findViewById(R.id.tv_price_home);
        }
    }
}
