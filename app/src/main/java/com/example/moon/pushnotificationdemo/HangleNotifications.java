package com.example.moon.pushnotificationdemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static com.example.moon.pushnotificationdemo.App.CHANEEL_ID;

public class HangleNotifications extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        createNotifications(remoteMessage);
        Log.i("MYTAG", "onMessageReceived: "+remoteMessage.getNotification().getBody());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    private void createNotifications(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("deviceName",data.get("deviceName"));
        intent.putExtra("deviceId",data.get("deviceID"));
        Log.i("MYTAG", "createNotifications: "+data.get("deviceName"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        Notification notification;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            notification = new NotificationCompat.Builder(getApplicationContext(),CHANEEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .build();
        }else{
            notification = new NotificationCompat.Builder(getApplicationContext())
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .build();


        }

        notificationManagerCompat.notify(1,notification);
    }


}
