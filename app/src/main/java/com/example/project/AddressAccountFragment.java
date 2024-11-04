package com.example.project;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.Object.Address;
import com.example.project.Object.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddressAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressAccountFragment newInstance(String param1, String param2) {
        AddressAccountFragment fragment = new AddressAccountFragment();
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
    private EditText edtUsername, edtPhoneNumber, edtAddress;
    private Button btnConfirm;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String userName, address, phoneNumber;
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address_account, container, false);
        initUI(view);
        loadData();
        eventListener();
        return view;
    }

    private void eventListener() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = edtUsername.getText().toString();
                address = edtAddress.getText().toString();
                phoneNumber = edtPhoneNumber.getText().toString();

                if(userName.isEmpty())
                    Toast.makeText(requireActivity(), "Please enter username", Toast.LENGTH_SHORT).show();
                else if (phoneNumber.isEmpty())
                    Toast.makeText(requireActivity(), "Please enter phone numbers", Toast.LENGTH_SHORT).show();
                else if (address.isEmpty())
                    Toast.makeText(requireActivity(), "Please enter address", Toast.LENGTH_SHORT).show();
                else {
                    //day du lieu len database
                    DatabaseReference ref = database.getReference("users");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Address mAddress = new Address(userName, address, phoneNumber);
                    ref.child(user.getUid()).child("address").setValue(mAddress);
                    Toast.makeText(requireContext(), "Address saved", Toast.LENGTH_SHORT).show();
                    //close fragment
                    getParentFragmentManager().beginTransaction()
                            .remove(AddressAccountFragment.this)
                            .commit();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void initUI(View v) {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        edtUsername = v.findViewById(R.id.edt_username);
        edtPhoneNumber = v.findViewById(R.id.edt_phone_number);
        edtAddress = v.findViewById(R.id.edt_address);
        btnConfirm = v.findViewById(R.id.btn_confirm_address);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }
    private void loadData(){
        progressDialog.show();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference ref = database.getReference("users").child(user.getUid()); // Lấy reference đến node của user hiện tại

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if
                (snapshot.hasChild("address")) {
                    Address mAddress = snapshot.child("address").getValue(Address.class);
                    edtAddress.setText(mAddress.getAddress());
                    edtUsername.setText(mAddress.getUserName());
                    edtPhoneNumber.setText(mAddress.getPhoneNumber());
                }progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
            }
        });
    }
}