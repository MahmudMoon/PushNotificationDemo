package com.example.moon.pushnotificationdemo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.google.firebase.FirebaseApp;

public class App extends Application {
    public static final String CHANEEL_ID = "Channel_One";

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
        NotificationChannel notificationChannel = new NotificationChannel(CHANEEL_ID,"Channel One", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}
