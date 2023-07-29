package com.rehman.clicksonic.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPref {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        this.context = context;
    }

    public void savedDataKey(String key, String dataKey, String value){

       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        preferences = context.getSharedPreferences(key,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(dataKey,value);
        editor.apply();
        editor.commit();
    }

    public void getDataKey(String key){
        preferences = context.getSharedPreferences(key,Context.MODE_PRIVATE);
    }

    public String getData(String dataKey){
        preferences.getString(dataKey,"");
        return dataKey;
    }
}
