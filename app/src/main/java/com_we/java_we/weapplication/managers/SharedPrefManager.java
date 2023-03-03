package com_we.java_we.weapplication.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
//
    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "sharedPreferencesUser";
    private static final String KEY_NIGHT = "night_mode_key";
    private static final String KEY_LANGUAGE = "language_key";

    private static SharedPrefManager instance;
    private static Context context;

    public SharedPrefManager(Context context){
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void setLanguage(String language){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LANGUAGE, language);
        editor.apply();
    }

    public String getLanguage(){
        String language = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(KEY_LANGUAGE, "English");
        return language;
    }

    public void setMode(boolean isChecked){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NIGHT, isChecked);
        editor.apply();
    }

    public boolean getMode(){
        Boolean state = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getBoolean(KEY_NIGHT, false);
        return state;
    }

}
