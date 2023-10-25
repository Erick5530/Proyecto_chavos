package com.example.life;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private final String CHANNEL_ID = "mi_canal";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Aquí manejas el token nuevo
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

    }
}
