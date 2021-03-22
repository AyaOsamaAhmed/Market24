package com.ka8eem.market24.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ka8eem.market24.models.UserModel;

import java.lang.reflect.Type;

public class Constants {

    // vars
    public static final String BASE_URL = "https://markt-24.online/";
    //https://itcoreapps.com/past/nolx/php/
    public static final String SHARED = "Settings";

    // functions
    public static String getLocal(Context context)
    {
        SharedPreferences pref =  context.getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        String lang = pref.getString("MY_LANG", "NOT");
        if (!lang.equals("NOT"))
            return lang;
        return "AR";
    }

    public static UserModel getUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE);
        String json = pref.getString("USER_MODEL", null);
        if (json == null)
            return null;
        Type type = new TypeToken<UserModel>() {
        }.getType();
        Gson gson = new Gson();
        UserModel userModel = gson.fromJson(json, type);
        return userModel;
    }
}
