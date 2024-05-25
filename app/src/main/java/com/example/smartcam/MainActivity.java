package com.example.smartcam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    private VideoView videoView;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firestore.collection("your_collection").document("your_document")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String videoPath = document.getString("videoPath");
                                if (videoPath != null) {
                                    fetchVideoFromStorage(videoPath);
                                }
                            }
                        } else {
                            // Handle error
                        }
                    }
                });
    }
    private void fetchVideoFromStorage(String videoPath) {
        StorageReference videoRef = storage.getReference().child(videoPath);
        videoRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri videoUri = task.getResult();
                    if (videoUri != null) {
                        playVideo(videoUri);
                    }
                } else {
                    // Handle error
                }
            }
        });
    }

    private void playVideo(Uri videoUri) {
        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(mp -> videoView.start());
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