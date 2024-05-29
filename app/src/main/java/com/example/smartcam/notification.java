package com.example.smartcam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class notification extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<NotificationData> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        recyclerView = findViewById(R.id.recyclerView);
        dataList = new ArrayList<>();
        adapter = new MyAdapter(dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadNotifications();
    }

    private void loadNotifications() {
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("noti");
        notificationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NotificationData notification = snapshot.getValue(NotificationData.class);
                    if (notification != null) {
                        dataList.add(0, notification);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(notification.this, "Failed to load notifications.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void xemlai(View view) {
        Intent intent = new Intent(notification.this, MainActivity.class);
        startActivity(intent);
    }
    public void setting(View view) {
        Intent intent = new Intent(notification.this, setting.class);
        startActivity(intent);
    }

    public void home(View view) {
        Intent intent = new Intent(notification.this, MainActivity.class);
        startActivity(intent);
    }
}
