package com.example.moon.pushnotificationdemo;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;

import static com.example.moon.pushnotificationdemo.MainActivity.Device_ID;
import static com.example.moon.pushnotificationdemo.MainActivity.bluetooth_name;

public class MyFirebaseInstanceIdServices extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        MySharedPref mySharedPref = MySharedPref.getInstance(getApplicationContext());
        if(mySharedPref!=null){
            mySharedPref.tokenSaved(s);
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DeviceInfo deviceInfo = new DeviceInfo(bluetooth_name,Device_ID,s);
        databaseReference.child("DeviceTokens").child(Device_ID).setValue(deviceInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Successfully Stored Data", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed To Store Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
