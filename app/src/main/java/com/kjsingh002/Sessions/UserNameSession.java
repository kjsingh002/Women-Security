package com.kjsingh002.Sessions;

import android.content.Context;
import android.content.SharedPreferences;

public class UserNameSession {
    private SharedPreferences sharedPreferences;
    private final String LOGIN_EMAIL = "loginUserName";

    public UserNameSession(Context context) {
        sharedPreferences = context.getSharedPreferences("LoginUserName",Context.MODE_PRIVATE);
    }

    public void setLoginUserName(String loginUserName){
        sharedPreferences.edit().putString( LOGIN_EMAIL,loginUserName).commit();
    }

    public String getLoginUserName(){
        return sharedPreferences.getString(LOGIN_EMAIL,"");
    }
}