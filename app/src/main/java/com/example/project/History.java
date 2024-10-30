package com.example.project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách đơn hàng
        orderList = new ArrayList<>();
        orderList.add(new Order("170", "Eljad Eendaz", "+1 (783) 0986 8786", "Select Street, Select City",
                "Pho(80.000 VND)-No:1, Com Tam(100.000 VND)-No:1, Pizza(150.000 VND)-No:1",
                "03-10-2024, 12:45AM", "330,000 VND", "Cash"));
        orderList.add(new Order("213", "Eljad Eendaz", "+1 (783) 0986 8786", "Select Street, Select City",
                "Pho(80.000 VND)-No:2", "03-08-2024, 1:45PM", "160,000 VND", "Cash"));

        // Thiết lập adapter cho RecyclerView
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);

        // Thiết lập BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_page:
                        Toast.makeText(History.this, "Home Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.cart_page:
                        Toast.makeText(History.this, "Cart Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.history_page:
                        Toast.makeText(History.this, "History Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.profile_page:
                        Toast.makeText(History.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
    }
}
