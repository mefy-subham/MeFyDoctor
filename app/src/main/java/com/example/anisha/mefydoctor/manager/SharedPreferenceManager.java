package com.example.anisha.mefydoctor.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.anisha.mefydoctor.constant.APPConstant;


public class SharedPreferenceManager {

    private static SharedPreferences sharedPreferences=null;



    public static void setFcmTokenSharedPreference(Context context,String token)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APPConstant.USER_FCM_TOKEN,token);
        editor.apply();
    }
    public static String getFcmTokenSharedPreference(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(APPConstant.USER_FCM_TOKEN,"");
    }

    }
