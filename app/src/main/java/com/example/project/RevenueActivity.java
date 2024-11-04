package com.example.project;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.RevenueAdapter;
import com.example.project.Object.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RevenueActivity extends AppCompatActivity {

    private RecyclerView rcvRevenue;
    private TextView tvPrice;
    private RevenueAdapter adapter;
    private List<Order> orderList;
    private DatabaseReference refOrder;
    NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_revenue);
        initUI();
        loadData();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadData() {
        orderList = new ArrayList<>();
        refOrder = FirebaseDatabase.getInstance().getReference("order");
        refOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                int totalRevenue=0;
                for(DataSnapshot data:snapshot.getChildren()){
                    Order order = data.getValue(Order.class);
                    if(order.getState().equals("Completed")) {
                        orderList.add(new Order(order.getOrderId(), order.getOrderTime(), order.getTotalPrice()));
                        totalRevenue += order.getTotalPrice();
                    }
                }tvPrice.setText(numberFormat.format(totalRevenue)+" VND");
                adapter.setData(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initUI() {
        rcvRevenue = findViewById(R.id.rcv_revenue);
        tvPrice = findViewById(R.id.tv_price_revenue);
        adapter = new RevenueAdapter();
        rcvRevenue.setLayoutManager(new LinearLayoutManager(this));
        rcvRevenue.setAdapter(adapter);
    }
}