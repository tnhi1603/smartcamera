package com.example.smartcam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
       // setContentView(R.layout.activity_main);
        if(currentUser==null){
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.login);
        }else {
            setContentView(R.layout.activity_main);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void setting(View view){
        Intent intent = new Intent(MainActivity.this,setting.class);
        startActivity(intent);
    }
    public void noti(View view){
        Intent intent = new Intent(MainActivity.this,notification.class);
        startActivity(intent);
    }
}