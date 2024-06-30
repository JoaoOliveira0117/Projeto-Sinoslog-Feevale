package com.example.projetofeevale.services;

import android.content.Context;
import android.content.SharedPreferences;

public class DataStore {
    private static final String PREFERENCE_FILE_KEY = "super_secretFile";
    private SharedPreferences sharedPreferences;

    // Volatile ensures that multiple threads handle the ourInstance variable correctly
    private static volatile DataStore ourInstance;

    private DataStore(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    }

    public static DataStore getInstance(Context context) {
        if (ourInstance == null) {
            synchronized (DataStore.class) {
                if (ourInstance == null) {
                    ourInstance = new DataStore(context.getApplicationContext());
                }
            }
        }
        return ourInstance;
    }

    public void setStringKey(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringKey(String key) {
        return sharedPreferences.getString(key, "");
    }
}
