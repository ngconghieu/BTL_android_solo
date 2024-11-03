package com.example.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project.Object.InputValidate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnSignin;
    private TextView tvForgotPassword, tvSignup;
    private ImageView imgvHideIcon;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
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
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Signing in...");
                progressDialog.show();
                String email = edtEmail.getText().toString().trim(),
                    password = edtPassword.getText().toString().trim();

                if(InputValidate.isValidEmail(email) && InputValidate.isValidPassword(password)){
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Intent i = new Intent(SignInActivity.this,MainActivity.class);
                                        startActivity(i);
                                        Toast.makeText(SignInActivity.this, "You are Signed In", Toast.LENGTH_SHORT).show();
                                        finish();

                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(SignInActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    progressDialog.dismiss();
                    if(!InputValidate.isValidEmail(email))
                        Toast.makeText(SignInActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    else if(!InputValidate.isValidPassword(password))
                        Toast.makeText(SignInActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tao alertDialog send email

                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);

                View dialogView = getLayoutInflater()
                        .inflate(R.layout.fragment_forgot_password,null);
                builder.setView(dialogView);
                alertDialog = builder.create();
                alertDialog.show();
                //check btn send email
                EditText edtSendEmail = dialogView.findViewById(R.id.edt_send_email);
                Button btnSendEmail = dialogView.findViewById(R.id.btn_send_email);
                btnSendEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendEmailForgotPassword(edtSendEmail);
                    }
                });
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

    }
    //xu ly khi click on btnSendEmail
    private void sendEmailForgotPassword(EditText edtSendEmail) {
        String SendEmail = edtSendEmail.getText().toString();

        if(InputValidate.isValidEmail(SendEmail)){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.sendPasswordResetEmail(SendEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.setMessage("An email reset password was just send to "+SendEmail);
                                progressDialog.show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        alertDialog.dismiss();
                                        Toast.makeText(SignInActivity.this, "Sent Email", Toast.LENGTH_SHORT).show();
                                    }
                                },5000);

//                                Toast.makeText(SignInActivity.this,
//                                        "An email reset password was just send to "+SendEmail, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignInActivity.this, "Email doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            edtSendEmail.setError("Invalid Email");
        }
    }

    private void initUI() {
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnSignin = findViewById(R.id.btn_signin);
        tvSignup = findViewById(R.id.tv_signup);
        imgvHideIcon = findViewById(R.id.imgv_hide_icon);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(SignInActivity.this);

    }
}