package com.example.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "general-channel-id";
    private static final int REQUEST_CODE = 100;

    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(
            v -> {
                boolean result = requestNotificationsPermission();
                if (result) showNotification();
            }
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "General", NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_CODE) return;
        boolean result = true;
        for (int grantResult : grantResults) {
            result = result && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (result) showNotification();
    }

    private boolean requestNotificationsPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true;
        }
        boolean result = ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED;
        if (!result) {
            ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE
            );
        }
        return result;
    }

    private void showNotification() {

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setAutoCancel(true)
            .setContentText("This is a message")
            .setContentTitle("Title")
            .build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(random.nextInt(), notification);
    }
}