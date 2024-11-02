package com.example.project.Adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.AddFoodActivity;
import com.example.project.Object.Food;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemFoodManagementAdapter extends RecyclerView.Adapter<ItemFoodManagementAdapter.ItemViewHolder> {
    private List<Food> mListFood;

    public void setData(List<Food> list) {
        mListFood = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_food_management, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Food food = mListFood.get(position);
        if (food == null) return;

        if (food.getImageFood() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(food.getImageFood().get(0))
                    .error(R.drawable.ic_account_management_food_management)
                    .into(holder.imgvFoodManagement);
        }

        //StrikeThrough
//        String tex = holder.tvDiscountManagement.getText().toString();
//        SpannableString spannableString = new SpannableString(tex);
//        spannableString.setSpan(new StrikethroughSpan(),0,tex.length(),0);
//        holder.tvDiscountManagement.setText(spannableString);

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);

        holder.tvFoodNameManageMent.setText((food.getFoodName()));
        holder.tvPriceManagement.setText(numberFormat.format(food.getPrice())+" VND");
        holder.tvDiscountManagement.setText(numberFormat.format(food.getDiscount())+" VND");
        holder.tvDetailsManagement.setText(food.getDetails());

        //click on item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddFoodActivity.class);
                i.putExtra("foodName", food.getFoodName());
                i.putExtra("price", food.getPrice());
                i.putExtra("details", food.getDetails());
                i.putExtra("discount", food.getDiscount());

                ArrayList<String> imgList = new ArrayList<>(food.getImageFood());

                if(!imgList.isEmpty() && imgList != null) {
                    i.putStringArrayListExtra("imageFood", imgList);
                }

                view.getContext().startActivity(i);
            }
        });
        //click on item delete btn
        holder.imgvDeleteFoodManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    new AlertDialog.Builder(holder.itemView.getContext())
                            .setTitle("Delete food")
                            .setMessage("Are you sure to delete this food?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mListFood.remove(pos);
                                    notifyItemRemoved(pos);
                                    notifyItemRangeChanged(pos, mListFood.size());
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("foods");
                                    ref.child(food.getFoodName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(holder.itemView.getContext(), "Succeed", Toast.LENGTH_SHORT).show();
                                                
                                                //delete from storage
                                                for(int i = 0; i<food.getImageFood().size();i++) {
                                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference(food.getFoodName() + "/"+i);
                                                    storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(holder.itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(holder.itemView.getContext(), "delete from storage failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                                }
                            }).setNegativeButton("No", null).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListFood != null) return mListFood.size();
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgvFoodManagement, imgvDeleteFoodManagement;
        private TextView tvFoodNameManageMent, tvPriceManagement,
                tvDiscountManagement, tvDetailsManagement;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgvFoodManagement = itemView.findViewById(R.id.imgv_food_management);
            tvFoodNameManageMent = itemView.findViewById(R.id.tv_food_name_management);
            tvDiscountManagement = itemView.findViewById(R.id.tv_discount_management);
            tvDiscountManagement.setPaintFlags(tvDiscountManagement.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvPriceManagement = itemView.findViewById(R.id.tv_price_management);
            tvDetailsManagement = itemView.findViewById(R.id.tv_details_management);
            imgvDeleteFoodManagement = itemView.findViewById(R.id.imgv_delete_food_management);
        }
    }
}
