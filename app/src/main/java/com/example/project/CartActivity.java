package com.example.project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private ArrayList<CartItem> cartItems;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = new ArrayList<>();
        cartItems.add(new CartItem("Pizza", 150000, R.drawable.ic_cart_pizza));
        cartItems.add(new CartItem("Pho", 80000, R.drawable.ic_cart_pho));
        cartItems.add(new CartItem("Com Tam", 100000, R.drawable.ic_cart_comtam));

        cartAdapter = new CartAdapter(cartItems);
        recyclerView.setAdapter(cartAdapter);

        // Tính toán tổng tiền
        calculateTotal();
    }

    private void calculateTotal() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice();
        }
        TextView totalTextView = findViewById(R.id.totalTextView);
        totalTextView.setText("Total: " + total + " VNĐ");
    }
}
