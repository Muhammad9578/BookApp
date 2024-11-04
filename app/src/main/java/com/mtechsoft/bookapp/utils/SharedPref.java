package com.mtechsoft.bookapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mtechsoft.bookapp.models.User;

public class SharedPref {
    private static final String PREF_NAME = "BOOKAPP";

    public static void saveUser(User user, Context context){
        SharedPreferences mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        mPreferences.edit().putString("user", json).apply();
    }

    public static User getUser(Context context){
        SharedPreferences mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPreferences.getString("user", null);
        return gson.fromJson(json, User.class);
    }
}
