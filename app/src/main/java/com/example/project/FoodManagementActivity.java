package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Adapter.ItemFoodManagementAdapter;
import com.example.project.Object.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FoodManagementActivity extends AppCompatActivity {

    private Button btnAddFood;
    private RecyclerView rcvFood;
    private ItemFoodManagementAdapter itemFoodManagementAdapter;
    private DatabaseReference ref;
    private List<Food> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_management);
        initUI();
        loadData();
        eventListener();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void eventListener() {
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FoodManagementActivity.this, AddFoodActivity.class);
                startActivity(i);
            }
        });
    }

    private void initUI() {
        list = new ArrayList<>();
        btnAddFood = findViewById(R.id.btn_add_food_management);
        rcvFood = findViewById(R.id.rcv_food_management);
        itemFoodManagementAdapter = new ItemFoodManagementAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvFood.setLayoutManager(linearLayoutManager);
        rcvFood.setAdapter(itemFoodManagementAdapter);
    }
    private void loadData(){
        ref = FirebaseDatabase.getInstance().getReference("foods");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(list!=null) list.clear();
                for(DataSnapshot data: snapshot.getChildren()){
                    Food food = data.getValue(Food.class);
                    if(food.getImageFood()== null){
                    } else
                        list.add(new Food(food.getFoodName(),food.getDetails(),food.getPrice(),food.getDiscount(),food.getImageFood()));
                } itemFoodManagementAdapter.setData(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodManagementActivity.this, "Can't load food", Toast.LENGTH_SHORT).show();
            }
        });
//        list.add(new Food("haha","hehe",2000,3000));
//        itemFoodManagementAdapter.setData(list);
    }
}