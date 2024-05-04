package com.example.smartcam;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class registerpage extends AppCompatActivity {

    TextInputEditText iedtEmail2, iedtPassword2, iedtUsername;
    Button btnsignUp;
    TextView tvsignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerpage);
        iedtUsername = findViewById(R.id.input_username);
        iedtEmail2 = findViewById(R.id.input_email2);
        iedtPassword2 = findViewById(R.id.input_passwd2);
        btnsignUp = findViewById(R.id.btn_signup);
        tvsignIn = findViewById(R.id.tv_signin);

        tvsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registerpage.this, log_in.class);
                startActivity(intent);
                finish();
            }
        });
        btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email2, password2, username;
                email2 = iedtEmail2.getText().toString().trim();
                password2 = iedtPassword2.getText().toString().trim();
                username = iedtUsername.getText().toString().trim();
                //kiểm tra kết email2, password2, username không empty
                if (TextUtils.isEmpty(email2)) {
                    Toast.makeText(registerpage.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password2)) {
                    Toast.makeText(registerpage.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password2.length() < 7) {

                    Toast.makeText(registerpage.this, "Password should greater than 7 digits", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(registerpage.this, "Please enter username", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

}