package com.example.project.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class AddImageFoodAdapter extends RecyclerView.Adapter<AddImageFoodAdapter.AddImageViewHolder>{

    private ArrayList<Uri> uriArrayList;

    public AddImageFoodAdapter(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public AddImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_add_food_management, parent,false);
        return new AddImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddImageViewHolder holder, int position) {
        //holder.imgAddFoodGallery.setImageURI(uriArrayList.get(position));
        Uri currentUri = uriArrayList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(currentUri)
                .into(holder.imgAddFoodGallery);
    }

    @Override
    public int getItemCount() {
        if(uriArrayList!=null) return uriArrayList.size();
        return 0;
    }

    public class AddImageViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgAddFoodGallery;
        public AddImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAddFoodGallery = itemView.findViewById(R.id.imgv_add_food_gallery);
        }
    }
}
