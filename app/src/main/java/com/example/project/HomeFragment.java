package com.example.project;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.Adapter.AllFoodAdapter;
import com.example.project.Adapter.ViewPagerHomeAdapter;
import com.example.project.Object.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private List<Food> listFood;
    private ViewPager2 viewPager2;
    private List<Food> listImage;
    private DatabaseReference ref;
    private ViewPagerHomeAdapter viewPagerHomeAdapter;
    private RecyclerView rcvAllFood;
    private AllFoodAdapter adapter;
    private TextView allfoodtextonly;
    private EditText edtSearch;
    private ProgressDialog progressDialog;
    private List<Food> filteredFoodList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);
        eventListener();
        getData();
        return view;
    }

    private void eventListener() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterFoodList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void filterFoodList(String query) {
        filteredFoodList = new ArrayList<>();
        for (Food food : listFood) {
            if (food.getFoodName().toLowerCase().contains(query.toLowerCase())) {
                filteredFoodList.add(food);
            }
        }
        adapter.setData(filteredFoodList);
        if (query.isEmpty()) {
            viewPager2.setVisibility(View.VISIBLE);
            allfoodtextonly.setVisibility(View.VISIBLE);
        } else {
            viewPager2.setVisibility(View.GONE);
            allfoodtextonly.setVisibility(View.GONE);
        }
    }


    private void initUI(View v) {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        edtSearch = v.findViewById(R.id.edt_search);
        allfoodtextonly = v.findViewById(R.id.tvallfoodonlytext);
        viewPager2 = v.findViewById(R.id.view_pager_food);
        viewPagerHomeAdapter = new ViewPagerHomeAdapter();
        viewPager2.setAdapter(viewPagerHomeAdapter);
        listFood = new ArrayList<>();

        //reload viewpager
        Handler mHandler = new Handler();
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                if(viewPager2.getCurrentItem()==listImage.size()-1){
                    viewPager2.setCurrentItem(0);
                }else {
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                }
            }
        };
        //set auto viewpager
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 3000);
            }
        });

        rcvAllFood = v.findViewById(R.id.rcv_all_foods);
        adapter = new AllFoodAdapter();
        rcvAllFood.setAdapter(adapter);
        rcvAllFood.setLayoutManager(new GridLayoutManager(requireContext(),2));

    }


    private void getData() {
        progressDialog.show();
        ref = FirebaseDatabase.getInstance().getReference("foods");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listFood.clear();
                for(DataSnapshot data:snapshot.getChildren()){
                    Food food = data.getValue(Food.class);
                    listFood.add(food);
                }
                filteredFoodList = new ArrayList<>(listFood);
                adapter.setData(filteredFoodList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listImage = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("foods");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data:snapshot.getChildren()){
                    Food food = data.getValue(Food.class);
                    listImage.add(food);
                }
                if(listImage.isEmpty()) {
                    allfoodtextonly.setTextSize(26);
                    allfoodtextonly.setText("We're out of food for now");
                    edtSearch.setVisibility(View.GONE);
                }else{
                    allfoodtextonly.setTextSize(16);
                    allfoodtextonly.setText("All Foods");
                    edtSearch.setVisibility(View.VISIBLE);
                }
                viewPagerHomeAdapter.setData(listImage);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}