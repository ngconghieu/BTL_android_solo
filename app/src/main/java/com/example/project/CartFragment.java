package com.example.project;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.CartAdapter;
import com.example.project.Object.Address;
import com.example.project.Object.Cart;
import com.example.project.Object.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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

    BottomSheetDialog bottomSheetDialog;
    private RecyclerView rcvCart;
    private Button btn_order;
    private TextView tvPrice, tvEmpty;
    private CartAdapter adapter;
    DatabaseReference ref;
    FirebaseUser user;
    private int totalPrice;
    NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        initUI(v);
        loadData();
        eventListener();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        progressDialog.show();
        List<Cart> list = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("cart").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                totalPrice = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    Cart cart = data.getValue(Cart.class);
                    list.add(cart);
                    totalPrice += cart.getSubPrice();
                }
                if (list.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                } else tvEmpty.setVisibility(View.GONE);
                adapter.setData(list);
                tvPrice.setText(numberFormat.format(totalPrice) + " VND");
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void eventListener() {
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View vi = getLayoutInflater().inflate(R.layout.layout_bottomsheet_order, null);
                bottomSheetDialog = new BottomSheetDialog(requireContext());
                bottomSheetDialog.setContentView(vi);
                bottomSheetDialog.show();
                sheetZone(vi);
            }
        });
    }

    private void initUI(View v) {
        tvEmpty = v.findViewById(R.id.tvempty);
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        btn_order = v.findViewById(R.id.btn_order);
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.FRANCE);
        adapter = new CartAdapter();
        rcvCart = v.findViewById(R.id.rcv_cart);
        btn_order = v.findViewById(R.id.btn_order);
        tvPrice = v.findViewById(R.id.tv_total_price_cart);
        rcvCart.setLayoutManager(new LinearLayoutManager(requireContext()));

        user = FirebaseAuth.getInstance().getCurrentUser();
        rcvCart.setAdapter(adapter);
    }


    //sheet zone
    private TextView tvPriceSheet, tvaddress, tvCartSheet;
    private EditText edtFullname, edtPhoneNumber, edtAddress;
    private Button btnCancel, btnOrder;
    private Spinner spnPayment, spnOrder;
    DatabaseReference refe;
    List<Cart> cartList;
    String currentDateTime;
    String fullName, address, payment, orderingMethod;
    String phoneNumber;
    DatabaseReference addressRef;
    FirebaseUser mUser;

    private void sheetZone(View v) {
        cartList = new ArrayList<>();
        tvaddress = v.findViewById(R.id.tvaddress);
        tvCartSheet = v.findViewById(R.id.tv_details_cart_sheet);
        tvPriceSheet = v.findViewById(R.id.tv_price_order_sheet);
        edtFullname = v.findViewById(R.id.edt_fullname_order_sheet);
        edtPhoneNumber = v.findViewById(R.id.edt_phone_number_order_sheet);
        edtAddress = v.findViewById(R.id.edt_address_order_sheet);
        btnOrder = v.findViewById(R.id.btn_order_sheet);
        btnCancel = v.findViewById(R.id.btn_cancel_order_sheet);
        spnPayment = v.findViewById(R.id.spn_payment_order_sheet);
        spnOrder = v.findViewById(R.id.spn_order_method_sheet);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //dateTime
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        currentDateTime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;

        loadSheetData();
        eventListenerSheet();
    }

    private void loadSheetData() {
        progressDialog.show();

        //payment
        ArrayAdapter<CharSequence> adap = ArrayAdapter.createFromResource(
                requireContext(), R.array.payment_method, android.R.layout.simple_spinner_dropdown_item);
        spnPayment.setAdapter(adap);

        //ordering
        ArrayAdapter<CharSequence> adap1 = ArrayAdapter.createFromResource(
                requireContext(), R.array.ordering_method, android.R.layout.simple_spinner_dropdown_item);
        spnOrder.setAdapter(adap1);

        // Load address if avail
        addressRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        addressRef.child("address").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Address address = snapshot.getValue(Address.class);
                    if (address != null) {
                        edtFullname.setText(address.getUserName());
                        edtPhoneNumber.setText(address.getPhoneNumber());
                        edtAddress.setText(address.getAddress());
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

        //event ordering method
        spnOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItem().toString().equals("in-store dining")) {
                    tvaddress.setVisibility(View.GONE);
                    edtAddress.setVisibility(View.GONE);
                } else {
                    tvaddress.setVisibility(View.VISIBLE);
                    edtAddress.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        tvPriceSheet.setText(numberFormat.format(totalPrice) + " VND");

        //show cart
        refe = FirebaseDatabase.getInstance().getReference("cart").child(user.getUid());
        refe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                StringBuilder cartText = new StringBuilder();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Cart cart = data.getValue(Cart.class);
                    if (cart != null) {
                        cartList.add(cart);
                        String tmp = "- " + cart.getFoodName() + " (" + numberFormat.format(cart.getSubPrice()) + ") - Quantity: " + cart.getQuantity();
                        cartText.append(tmp).append("\n");
                    }
                }
                tvCartSheet.setText(cartText.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void eventListenerSheet() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fullName = edtFullname.getText().toString().trim();
                phoneNumber = edtPhoneNumber.getText().toString().trim();
                address = edtAddress.getText().toString().trim();
                payment = spnPayment.getSelectedItem().toString();
                orderingMethod = spnOrder.getSelectedItem().toString();

                if (fullName.isEmpty()) edtFullname.setError("required");
                else if (phoneNumber.isEmpty()) edtPhoneNumber.setError("required");
                else if (orderingMethod.equals("delivery") && address.isEmpty()) edtAddress.setError("required");
                else {
                    progressDialog.show();
                    DatabaseReference refOrder = FirebaseDatabase.getInstance().getReference("order");
                    refOrder.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String tmp = "order" + snapshot.getChildrenCount();
                            Address adr = new Address(fullName, address, phoneNumber);
                            addressRef.child("address").setValue(adr);
                            if (orderingMethod.equals("in-store dining")) adr.setAddress("");
                            Order order = new Order(tmp, payment, orderingMethod, currentDateTime, "Pending", mUser.getUid() , totalPrice, cartList, adr);

                            refOrder.child(tmp).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        refe = FirebaseDatabase.getInstance().getReference("cart");
                                        refe.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(requireContext(), "Order successfully", Toast.LENGTH_SHORT).show();
                                                    bottomSheetDialog.dismiss();
                                                } else
                                                    Toast.makeText(requireContext(), "There're unknown error founded", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                    } else {
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

}