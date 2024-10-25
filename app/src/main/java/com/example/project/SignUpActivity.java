package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmai, edtPassword, edtReTypePassword,
                    edtUserCode;
    private Button btnSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        linkComponent();
        event();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void event() {
        btnSignup.setOnClickListener(new View.OnClickListener() {
            String username = edtUsername.getText().toString().trim();
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void linkComponent() {
        edtEmai = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtReTypePassword = findViewById(R.id.edt_retype_password);
        edtUserCode = findViewById(R.id.edt_usercode);
        btnSignup = findViewById(R.id.btn_signup);

    }
}