package com.example.moon.pushnotificationdemo;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPref {
    private static MySharedPref sharedPref = null;
    private Context mContext;
    public static final String SharedPrefName = "MySharedPreferenceName";
    public static final String SharedPrefKey = "MyKey";

    private MySharedPref(Context mContext) {
        this.mContext = mContext;
    }

    public static synchronized MySharedPref getInstance(Context context){
        if(sharedPref==null){
            sharedPref = new MySharedPref(context);
        }
        return sharedPref;
    }

    public boolean tokenSaved(String token){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SharedPrefName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPrefKey,token).apply();
        return true;
    }

    public String getToken(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SharedPrefName,Context.MODE_PRIVATE);
        String token_ = sharedPreferences.getString(SharedPrefKey, null);
        return token_;
    }

}
