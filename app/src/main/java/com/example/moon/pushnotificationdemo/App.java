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
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        FirebaseApp.initializeApp(getApplicationContext());
         if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
             NotificationChannel notificationChannel = new NotificationChannel(CHANEEL_ID, "Channel One", NotificationManager.IMPORTANCE_HIGH);
             notificationManager.createNotificationChannel(notificationChannel);
         }
    }
}
