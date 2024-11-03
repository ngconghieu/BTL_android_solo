package com.example.project;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project.Object.InputValidate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangePasswordAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordAccountFragment newInstance(String param1, String param2) {
        ChangePasswordAccountFragment fragment = new ChangePasswordAccountFragment();
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

    private EditText edtOldPassword, edtNewPassword, edtRetypePassword;
    private Button btnConfirm;
    private ImageView imgvHidePassword, imgvHidePassword1,imgvHidePassword2;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password_account, container, false);
        initUI(view);
        eventListener();
        return view;
    }

    private void eventListener() {
        imgvHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtOldPassword.getInputType() == 129) {
                    edtOldPassword.setInputType(1);
                    imgvHidePassword.setImageResource(R.drawable.ic_app_hidepassword);
                }else{
                    edtOldPassword.setInputType(129);
                    imgvHidePassword.setImageResource(R.drawable.ic_app_showpassword);
                }
            }
        });
        imgvHidePassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtNewPassword.getInputType() == 129) {
                    edtNewPassword.setInputType(1);
                    imgvHidePassword1.setImageResource(R.drawable.ic_app_hidepassword);
                }else{
                    edtNewPassword.setInputType(129);
                    imgvHidePassword1.setImageResource(R.drawable.ic_app_showpassword);
                }
            }
        });
        imgvHidePassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtRetypePassword.getInputType() == 129) {
                    edtRetypePassword.setInputType(1);
                    imgvHidePassword2.setImageResource(R.drawable.ic_app_hidepassword);
                }else{
                    edtRetypePassword.setInputType(129);
                    imgvHidePassword2.setImageResource(R.drawable.ic_app_showpassword);
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String oldPassword = edtOldPassword.getText().toString(),
                    newPassword = edtNewPassword.getText().toString(),
                    retypePassword = edtRetypePassword.getText().toString();
                FirebaseUser user = mAuth.getCurrentUser();

                //check text oldPassword
                if(InputValidate.isValidPassword(oldPassword)) {
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), oldPassword);
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //dung Old password
                                        //check text newPassword
                                        if(InputValidate.isValidPassword(newPassword)) {
                                            if(newPassword.equals(retypePassword)) {
                                                user.updatePassword(newPassword)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    //update success
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(requireActivity(), "Successfully updated new password", Toast.LENGTH_SHORT).show();
                                                                    getParentFragmentManager().beginTransaction()
                                                                            .remove(ChangePasswordAccountFragment.this).commit();
                                                                } else {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(requireActivity(), "Cannot update new password", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }else{
                                                progressDialog.dismiss();
                                                Toast.makeText(requireActivity(), "Re type password is incorrect", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            //sai text newPassword
                                            progressDialog.dismiss();
                                            Toast.makeText(requireActivity(), "New Password is invalid", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        //sai oldPassword
                                        progressDialog.dismiss();
                                        Toast.makeText(requireActivity(), "Old Password is incorrect", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    //sai text password
                    progressDialog.dismiss();
                    Toast.makeText(requireActivity(), "Old Password is Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initUI(View v) {
        edtOldPassword = v.findViewById(R.id.edt_old_password);
        edtNewPassword = v.findViewById(R.id.edt_new_password);
        edtRetypePassword = v.findViewById(R.id.edt_retype_password);
        imgvHidePassword = v.findViewById(R.id.imgv_hide_icon);
        imgvHidePassword1 = v.findViewById(R.id.imgv_hide_icon1);
        imgvHidePassword2 = v.findViewById(R.id.imgv_hide_icon2);
        btnConfirm = v.findViewById(R.id.btn_confirm);
        progressDialog = new ProgressDialog(requireActivity());
        mAuth = FirebaseAuth.getInstance();

    }
}