package com.example.project.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.FoodDetailsActivity;
import com.example.project.Object.Food;
import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerHomeAdapter extends RecyclerView.Adapter<ViewPagerHomeAdapter.PhotoViewHolder> {
    private List<Food> list;

    public void setData(List<Food> mList){
        list= mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_viewpager,parent,false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Food food = list.get(position);
        if(food.getImageFood()==null) return;
        Glide.with(holder.itemView.getContext())
                .load(food.getImageFood().get(0))
                .into(holder.imgViewPager);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.itemView.getContext(), FoodDetailsActivity.class);
                i.putExtra("foodName",food.getFoodName());
                i.putExtra("price", food.getPrice());
                i.putExtra("discount",food.getDiscount());
                i.putExtra("details",food.getDetails());


                i.putStringArrayListExtra("imageFood",new ArrayList<>(food.getImageFood()));

                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list!=null) return list.size();
        return 0;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgViewPager;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgViewPager = itemView.findViewById(R.id.img_viewpager);

        }
    }

}
