package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MenuAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuAccountFragment newInstance(String param1, String param2) {
        MenuAccountFragment fragment = new MenuAccountFragment();
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
    private Button btnLogout,btnAddress, btnChangePassword, btnManagement;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_account, container, false);
        initUI(view);
        checkRole();
        eventListener();
        return view;
    }

    private void checkRole() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(!user.getDisplayName().equals("admin")) btnManagement.setEnabled(false);
    }


    private void eventListener() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                        .setTitle("Do you really want to log out?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.signOut();
                                Intent intent = new Intent(requireActivity(), SignInActivity.class);
                                startActivity(intent);
                                Toast.makeText(requireActivity(), "You are logged out", Toast.LENGTH_SHORT).show();
                                getActivity().finishAffinity();
                            }
                        }).setNegativeButton("Cancel",null).show();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordAccountFragment cpf = new ChangePasswordAccountFragment();
                getParentFragmentManager().beginTransaction()
                        .add(R.id.layout_menu_account,cpf)
                        .addToBackStack(null)
                        .commit();
            }
        });
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressAccountFragment aaf = new AddressAccountFragment();
                getParentFragmentManager().beginTransaction()
                        .add(R.id.layout_menu_account,aaf)
                        .addToBackStack(null)
                        .commit();
            }
        });
        btnManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManagementAccountFragment maf = new ManagementAccountFragment();
                getParentFragmentManager().beginTransaction()
                        .add(R.id.layout_menu_account,maf)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void initUI(View view) {
        btnLogout = view.findViewById(R.id.btn_logout);
        btnAddress = view.findViewById(R.id.btn_address);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnManagement = view.findViewById(R.id.btn_management);
        mAuth = FirebaseAuth.getInstance();
    }
}