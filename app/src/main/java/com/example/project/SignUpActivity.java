package com.example.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.Object.InputValidate;
import com.example.project.Object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class SignUpActivity extends AppCompatActivity {

    private String email, password, reTypePassword, userCode;
    private EditText edtEmai, edtPassword, edtReTypePassword,
                    edtUserCode;
    private ImageView imgvHideIcon, imgvHideIcon1;
    private Button btnSignup;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        initUI();
        eventListener();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void eventListener() {

        imgvHideIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtPassword.getInputType() == 129) {
                    edtPassword.setInputType(1);
                    imgvHideIcon.setImageResource(R.drawable.ic_app_hidepassword);
                }else{
                    edtPassword.setInputType(129);
                    imgvHideIcon.setImageResource(R.drawable.ic_app_showpassword);
                }
            }
        });

        imgvHideIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtReTypePassword.getInputType() == 129) {
                    edtReTypePassword.setInputType(1);
                    imgvHideIcon1.setImageResource(R.drawable.ic_app_hidepassword);
                }else{
                    edtReTypePassword.setInputType(129);
                    imgvHideIcon1.setImageResource(R.drawable.ic_app_showpassword);
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                email = edtEmai.getText().toString().trim();
                password = edtPassword.getText().toString().trim();
                reTypePassword = edtReTypePassword.getText().toString().trim();
                userCode = edtUserCode.getText().toString().trim();
                //check password retype
                if(!password.equals(reTypePassword)){
                    Toast.makeText(SignUpActivity.this, "Re-Type Password doesn't match Password", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
                //check emai password
                if(InputValidate.isValidEmail(email) && InputValidate.isValidPassword(password)){
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(i);
                                    }
                                    else {
                                        Toast.makeText(SignUpActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    //update role user
                                    if(userCode.toLowerCase().equals("admin")) {
                                        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                                .setDisplayName("admin").build();
                                        user.updateProfile(profile);
                                        finish();
                                    }else{
                                        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                                .setDisplayName("customer").build();
                                        user.updateProfile(profile);
                                        finish();
                                    }
                                    setUser();
                                    progressDialog.dismiss();
                                }
                            });
                }else{
                    progressDialog.dismiss();
                    if(!InputValidate.isValidEmail(email))
                        Toast.makeText(SignUpActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                        //edtEmai.setError("Invalid Email");
                    else if(!InputValidate.isValidPassword(password))
                        Toast.makeText(SignUpActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                        //edtPassword.setError("Invalid Password");
                }

            }
        });
    }

    private void setUser() {
        //...
    }

    private void initUI() {
        edtEmai = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtReTypePassword = findViewById(R.id.edt_retype_password);
        edtUserCode = findViewById(R.id.edt_usercode);
        imgvHideIcon = findViewById(R.id.imgv_hide_icon);
        imgvHideIcon1 = findViewById(R.id.imgv_hide_icon1);
        btnSignup = findViewById(R.id.btn_signup);
        progressDialog = new ProgressDialog(this);


    }
}