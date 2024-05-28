package com.example.smartcam;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.arthenica.mobileffmpeg.FFmpeg;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "smartcam_channel";
    private VideoView videoView;
    private Button playPauseButton, forwardButton, rewindButton;
    private DatabaseReference pirRef;
    private Handler handler;
    private Runnable notificationRunnable;
    private boolean pirStatusOn;
    private String message="Đã phát hiện chuyển động";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firebase
        FirebaseApp.initializeApp(this);

        videoView = findViewById(R.id.videoView);
        playPauseButton = findViewById(R.id.playButton);
        forwardButton = findViewById(R.id.forwardButton);
        rewindButton = findViewById(R.id.rewindButton);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference videoRef = storageRef.child("1405202415:23:41.h264");
        try {
            // Tạo file tạm thời để lưu video tải về
            File localFile = File.createTempFile("video", ".h264");
            File convertedFile = new File(getExternalFilesDir(null), "video1.mp4");

            videoRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Chuyển đổi file h264 thành mp4
                    String[] command = {"-i", localFile.getAbsolutePath(), convertedFile.getAbsolutePath()};
                    int rc = FFmpeg.execute(command);

                    if (rc == 0) {
                        // Chuyển đổi thành công
                        videoView.setVideoURI(Uri.fromFile(convertedFile));
                        videoView.start();
                        playPauseButton.setText("Pause");
                    } else {
                        Log.e(TAG, "FFmpeg process failed with return code: " + rc);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Xử lý lỗi nếu có
                    Log.e(TAG, "Failed to download file: " + exception.getMessage());
                }
            });

        } catch (IOException e) {
            Log.e(TAG, "Error creating temp file: " + e.getMessage());
        }
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    playPauseButton.setText("Play");
                } else {
                    videoView.start();
                    playPauseButton.setText("Pause");
                }
            }
        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = videoView.getCurrentPosition();
                int duration = videoView.getDuration();
                int seekForwardTime = 10000; // 10 seconds

                if (currentPosition + seekForwardTime <= duration) {
                    videoView.seekTo(currentPosition + seekForwardTime);
                } else {
                    videoView.seekTo(duration);
                }
            }
        });

        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = videoView.getCurrentPosition();
                int seekBackwardTime = 10000; // 10 seconds

                if (currentPosition - seekBackwardTime >= 0) {
                    videoView.seekTo(currentPosition - seekBackwardTime);
                } else {
                    videoView.seekTo(0);
                }
            }
        });
        createNotificationChannel();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        pirRef = database.getReference("PIR");

        handler = new Handler();
        pirStatusOn = false;

        // Lắng nghe sự thay đổi của PIR
        pirRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.getValue(String.class);
                if (status != null && status.equals("on") && !pirStatusOn) {
                    sendNotification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to read PIR status", Toast.LENGTH_SHORT).show();
            }
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SmartCam Channel";
            String description = "Channel for SmartCam notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification() {
        Intent intent = new Intent(this, notification.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.smartcam)
                .setContentTitle("SmartCam Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        saveNotification("Đã phát hiện chuyển động");
    }
    DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("noti");
    String notificationId = notificationsRef.push().getKey();
    public void saveNotification(String title) {
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        NotificationData notification = new NotificationData(title, date, time);
        notificationsRef.push().setValue(notification);
    }
}