package com.kjsingh002.Sessions;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginSession {
    private final String LOGIN_STATE = "loginState";
    private SharedPreferences sharedPreferences;

    public LoginSession(Context context) {
        sharedPreferences = context.getSharedPreferences("LoginState",Context.MODE_PRIVATE);
    }

    public void setLoginState(boolean loginState){
        sharedPreferences.edit().putBoolean(LOGIN_STATE,loginState).commit();
    }

    public boolean getLoginState(){
        return sharedPreferences.getBoolean(LOGIN_STATE,false);
    }
}
