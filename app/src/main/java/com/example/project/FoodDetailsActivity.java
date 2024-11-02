package com.example.project;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Adapter.AddImageFoodAdapter;
import com.example.project.Object.Cart;
import com.example.project.Object.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodDetailsActivity extends AppCompatActivity {

    private ImageView imgvMain;
    private TextView tvName, tvPrice, tvDiscount, tvDetails, tvShowDetails;
    private RecyclerView rcvMoreImg;
    private Button btnAddtoCart;
    private DatabaseReference ref;
    private AddImageFoodAdapter adapter;
    ArrayList<String> imageFood;
    ArrayList<Uri> uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_details);
        initUI();
        loadData();
        eventListener();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void eventListener() {
        btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetOpen();
            }
        });

    }
String foodNameAll;
    private void initUI() {
        btnAddtoCart = findViewById(R.id.btn_add_to_cart);
        imgvMain = findViewById(R.id.imgv_main);
        tvDetails = findViewById(R.id.tv_details_detail);
        tvDiscount = findViewById(R.id.tv_discount_detail);
        uri = new ArrayList<>();
        tvDiscount.setVisibility(View.GONE);
        tvDiscount.setPaintFlags(tvDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvName = findViewById(R.id.tv_food_name_detail);
        tvPrice = findViewById(R.id.tv_price_detail);
        rcvMoreImg = findViewById(R.id.rcv_more_img);
        btnAddtoCart = findViewById(R.id.btn_add_to_cart);
        tvShowDetails = findViewById(R.id.tv_show_details);
        adapter = new AddImageFoodAdapter(uri);
        rcvMoreImg.setLayoutManager(new GridLayoutManager(this, 2));
        rcvMoreImg.setAdapter(adapter);
        rcvMoreImg.setFocusable(false);
        rcvMoreImg.setNestedScrollingEnabled(false);

    }

    private void loadData() {
        String foodName, details;
        int price, discount;
        Intent i = getIntent();

        foodName = i.getStringExtra("foodName");
        foodNameAll=foodName; //use for sheet
        details = i.getStringExtra("details");
        price = i.getIntExtra("price", 0);
        discount = i.getIntExtra("discount", 0);
        imageFood = i.getStringArrayListExtra("imageFood");


        NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);
        if (discount != 0) tvDiscount.setVisibility(View.VISIBLE);
        if (!details.isEmpty()) tvShowDetails.setVisibility(View.VISIBLE);

        tvName.setText(foodName);
        tvDetails.setText(details);

        int totalPrice = price - discount;
        tvPrice.setText(numberFormat.format(totalPrice) + " VND");
        tvDiscount.setText(numberFormat.format(price) + " VND");

        if (imageFood == null) return;
        Glide.with(this)
                .load(imageFood.get(0))
                .into(imgvMain);

        uri.clear();
        //chuyen list string sang list uri
        for (String data : imageFood) uri.add(Uri.parse(data));
        adapter.notifyDataSetChanged();
    }

    //Sheet zone___________________________________________________________________________________________

    Button btnCancel, btnAddToCartSheet, btnSub, btnAdd, btnNumber;
    ImageView imgvSheet;
    TextView tvNameSheet, tvPriceSheet;
    int numberSheet, priceOrigin=0, priceChange;
    NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);
    String imgFood;

    private void sheetOpen() {
        View v = getLayoutInflater().inflate(R.layout.layout_bottomsheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(FoodDetailsActivity.this);
        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.show();

        //initUI sheet
        btnCancel = v.findViewById(R.id.btn_sheet_cancel);
        btnAddToCartSheet = v.findViewById(R.id.btn_sheet_add_to_cart);
        btnSub = v.findViewById(R.id.btn_sub);
        btnAdd = v.findViewById(R.id.btn_add);
        btnNumber = v.findViewById(R.id.btn_number);
        imgvSheet = v.findViewById(R.id.imgv_sheet);
        tvNameSheet = v.findViewById(R.id.tv_food_name_sheet);
        tvPriceSheet = v.findViewById(R.id.tv_price_sheet);

        loadSheet();
        //sub add clicked
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberSheet > 1) {
                    numberSheet -= 1;
                    priceChange = priceOrigin *numberSheet;
                    btnNumber.setText(String.valueOf(numberSheet));
                    tvPriceSheet.setText(numberFormat.format(priceChange) + " VND");
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberSheet += 1;
                priceChange = priceOrigin *numberSheet;
                btnNumber.setText(String.valueOf(numberSheet));
                tvPriceSheet.setText(numberFormat.format(priceChange)+ " VND");
            }
        });
        //add n close
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        btnAddToCartSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference refCart = FirebaseDatabase.getInstance().getReference("cart");
                refCart.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DatabaseReference r = refCart.child("cart"+snapshot.getChildrenCount()).child(foodNameAll);
                        r.child("foodName").setValue(foodNameAll);
                        r.child("quantity").setValue(numberSheet);
                        r.child("subTotal").setValue(priceChange);
                        Toast.makeText(FoodDetailsActivity.this, "Add food successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void loadSheet() {
        numberSheet=1;
        priceChange = priceOrigin;
        ref = FirebaseDatabase.getInstance().getReference("foods");
        ref.child(foodNameAll).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food food = snapshot.getValue(Food.class);
                tvNameSheet.setText(food.getFoodName());
                priceOrigin = food.getPrice() - food.getDiscount();
                priceChange = priceOrigin * numberSheet;
                tvPriceSheet.setText(numberFormat.format(priceOrigin)+" VND");

                Glide.with(FoodDetailsActivity.this)
                        .load(food.getImageFood().get(0))
                        .into(imgvSheet);
                imgFood = food.getImageFood().get(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}