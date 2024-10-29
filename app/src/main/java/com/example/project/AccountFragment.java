package com.example.project;

import android.app.AlertDialog;
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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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

    private Button btnLogout;
    private FirebaseAuth mAuth;
    private ImageView imgvAvatar;
    private TextView tvEmail, tvRole;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initUI(view);
        eventListener();
        setInfoUser();
        return view;
    }

    private void setInfoUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        String roleUser = user.getDisplayName();
        tvEmail.setText(user.getEmail());
        tvRole.setText(roleUser);
        if (roleUser.equals("admin")) imgvAvatar.setImageResource(R.mipmap.img_admin);
        else imgvAvatar.setImageResource(R.mipmap.img_user);
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
    }

    private void initUI(View view) {
        btnLogout = view.findViewById(R.id.btn_logout);
        tvEmail = view.findViewById(R.id.tv_email);
        tvRole = view.findViewById(R.id.tv_user_role);
        imgvAvatar = view.findViewById(R.id.imgv_avatar);
        mAuth = FirebaseAuth.getInstance();
    }
}