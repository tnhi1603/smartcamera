package com.example.smartcam;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class resetpassword extends AppCompatActivity {

    Button btnReset;
    TextInputEditText email3;
    TextView tvSignin, tvSignup;
    FirebaseAuth mAuth;
    String getEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword);


        btnReset = findViewById(R.id.btn_reset);
        email3 = findViewById(R.id.input_email3);
        tvSignin = findViewById(R.id.tv_signin3);
        tvSignup = findViewById(R.id.tv_signup3);



        mAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmail = email3.getText().toString().trim();
                if(!TextUtils.isEmpty(getEmail)){
                    reset();
                }else{
                    email3.setError("Email field can't be empty");
                }
            }
        });



    }
    public void reset(){
        mAuth.sendPasswordResetEmail(getEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Reset password link has been sent to your register email", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(resetpassword.this, log_in.class);
                startActivity(intent);
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }





}
