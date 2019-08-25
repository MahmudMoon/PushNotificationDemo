package com.example.moon.pushnotificationdemo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import android.provider.Settings.Secure;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MyTag";
    public static final String IS_KEY_SAVED = "isKeySaved";
    TextView textView;
    public static String TOKEN;
    MutableLiveData<String> mutableLiveData;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    public static String Device_ID;
    public static String bluetooth_name;
    CheckBox cbWeather,cbMovie,cbGeneral,cbGame,cbAdult;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        cbAdult = findViewById(R.id.cb_adult);
//        cbGame = findViewById(R.id.cb_game);
//        cbGeneral = findViewById(R.id.cb_general);
//        cbMovie = findViewById(R.id.cb_movie);
        cbWeather = findViewById(R.id.cb_weather);


        bluetooth_name = Secure.getString(getContentResolver(), "bluetooth_name");
        Device_ID = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
        textView = (TextView)findViewById(R.id.tv);

        Intent intent = getIntent();
        if(intent!=null){
            String deviceName = intent.getStringExtra("deviceName");
            String deviceId = intent.getStringExtra("deviceId");
           // textView.setText("Name : "+ deviceName + "\n" + "DeviceId : "+ deviceId);
        }



        cbWeather.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   FirebaseMessaging.getInstance().subscribeToTopic("Weather");
                }else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Weather");
                }
            }
        });

//        cbMovie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    FirebaseMessaging.getInstance().subscribeToTopic("Movie");
//                }else{
//                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Movie");
//                }
//            }
//        });
//
//        cbAdult.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    FirebaseMessaging.getInstance().subscribeToTopic("Adult");
//                }else{
//                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Adult");
//                }
//            }
//        });
//
//        cbGame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    FirebaseMessaging.getInstance().subscribeToTopic("Game");
//                }else{
//                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Game");
//                }
//            }
//        });
//
//        cbGeneral.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    FirebaseMessaging.getInstance().subscribeToTopic("General");
//                }else{
//                    FirebaseMessaging.getInstance().unsubscribeFromTopic("General");
//                }
//            }
//        });



//                FirebaseMessaging.getInstance().subscribeToTopic("weather")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getApplicationContext(),"Subscribed",Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(),"Failed to subscribe",Toast.LENGTH_SHORT).show();
//            }
//        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            return;
                        }
                        String token  = task.getResult().getToken();
                        MySharedPref instance = MySharedPref.getInstance(getApplicationContext());
                        String token_saved = instance.getToken();
                        if(!TextUtils.isEmpty(token_saved) &&!token.equals(token_saved)){
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference();
                            DeviceInfo deviceInfo = new DeviceInfo(bluetooth_name,Device_ID,token);
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
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        FirebaseMessaging.getInstance().isAutoInitEnabled();


        sharedPreferences = getSharedPreferences("Token",MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mutableLiveData = new MutableLiveData<>();
        mutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                MySharedPref mySharedPref = MySharedPref.getInstance(getApplicationContext());
                if(mySharedPref!=null){
                 mySharedPref.tokenSaved(s);
                }
               // textView.setText(s);
                databaseReference = firebaseDatabase.getReference();
                DeviceInfo deviceInfo = new DeviceInfo(bluetooth_name,Device_ID,s);
                    databaseReference.child("DeviceTokens").child(Device_ID).setValue(deviceInfo).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(IS_KEY_SAVED,true);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Successfully Stored Data", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed To Store Data", Toast.LENGTH_SHORT).show();

                        }
                    });
            }
        });

        boolean aBoolean = sharedPreferences.getBoolean(IS_KEY_SAVED, false);
        if(!aBoolean) {
            getToken();
        }else{
            Log.i(TAG, "onCreate: "+"Already Called");
        }


       String token =  getCurrentToken();
        if(token!=null){
            Log.i(TAG, "getCurrentToken: "+token);
           // textView.setText(token);
        }

    }

    private String getCurrentToken() {
        MySharedPref mySharedPref = MySharedPref.getInstance(getApplicationContext());
        String token = "";
        if(mySharedPref!=null){
            token = mySharedPref.getToken();
        }
        return token;
    }

    private void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            String token = task.getResult().getToken();
                            mutableLiveData.setValue(token);
                            Log.i(TAG, "onComplete: "+token);
                            TOKEN = token;
                        }else{
                            return;
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
