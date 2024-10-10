package com.example.project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private BottomNavigationView bottom_nav;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.view_pager);
        setUpViewPager();

        bottom_nav = findViewById(R.id.bottom_nav);
        setUpBottomNav();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setUpBottomNav() {
        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.home) viewPager2.setCurrentItem(0);
                else if(id==R.id.cart) viewPager2.setCurrentItem(1);
                else if(id==R.id.history) viewPager2.setCurrentItem(2);
                else viewPager2.setCurrentItem(3);
                return true;
            }
        });
    }

    private void setUpViewPager() {
        ViewPagerAppAdapter vpa = new ViewPagerAppAdapter(this);
        viewPager2.setAdapter(vpa);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        bottom_nav.setSelectedItemId(R.id.home);
                        break;
                    case 1:
                        bottom_nav.setSelectedItemId(R.id.cart);
                        break;
                    case 2:
                        bottom_nav.setSelectedItemId(R.id.history);
                        break;
                    case 3:
                        bottom_nav.setSelectedItemId(R.id.profile);
                        break;
                }
            }
        });
    }
}