package com.example.smartcam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class log_in extends AppCompatActivity {
    TextInputEditText iedtEmail, iedtPassword;
    Button btn_signIn;
    TextView tv_signUp, tv_reset;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth= FirebaseAuth.getInstance();

        iedtEmail = findViewById(R.id.input_email);
        iedtPassword = findViewById(R.id.input_passwd);
        btn_signIn = findViewById(R.id.btn_signin);
        tv_signUp = findViewById(R.id.tv_signup);
        tv_reset = findViewById(R.id.tv_forgotpasswd);

        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(log_in.this,registerpage.class);
                startActivity(intent);
                finish();
            }
        });
//        btn_signIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email, password;
//                email = iedtEmail.getText().toString().trim();
//                password = iedtPassword.getText().toString().trim();
//                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(log_in.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(password)) {
//                    Toast.makeText(log_in.this, "Please enter password", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//        });


        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }
    public void home(View view){
        Intent intent = new Intent(log_in.this,MainActivity.class);
        startActivity(intent);
    }
    public void login(){
        String email, password;
        email = iedtEmail.getText().toString().trim();
        password = iedtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(log_in.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
            }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(log_in.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Sign in successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(log_in.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
