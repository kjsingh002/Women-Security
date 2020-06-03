package com.kjsingh002.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kjsingh002.Dao.UserDao;
import com.kjsingh002.Database.UserDB;
import com.kjsingh002.Entity.User;
import com.kjsingh002.Sessions.LoginSession;
import com.kjsingh002.Sessions.UserNameSession;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText userName;
    private TextInputEditText password;
    private UserDao userDao;
    private UserDB userDB;
    private LoginSession loginSession;
    private UserNameSession userNameSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeFields();
    }

    private void initializeFields() {
        userName = findViewById(R.id.signup_activity_username);
        password = findViewById(R.id.signup_activity_password);
        userDB = Room.databaseBuilder(SignUpActivity.this,UserDB.class,"UsersData").build();
        userDao = userDB.getUserDao();
        loginSession = new LoginSession(SignUpActivity.this);
        userNameSession = new UserNameSession(SignUpActivity.this);
    }

    public void goToMainActivity(View view){
        if (TextUtils.isEmpty(userName.getText())  && TextUtils.isEmpty(password.getText())){
            userName.setError("Field Required");
            password.setError("Field Required");
        }else if (TextUtils.isEmpty(userName.getText())){
            userName.setError("Field Required");
        }else if (TextUtils.isEmpty(password.getText())){
            password.setError("Field Required");
        }else {
            new CheckExistingUser().execute(userName.getText().toString());
        }
    }

    public void goToLoginActivity(View view){
        final Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    class CheckExistingUser extends AsyncTask<String,Void,User>{

        @Override
        protected User doInBackground(String... strings) {
            return userDao.checkExistingUser(strings[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user == null){
                new SignUpUser().execute(userName.getText().toString(),password.getText().toString());
            }else {
                Toast.makeText(SignUpActivity.this, "User Exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class SignUpUser extends AsyncTask<String,Void,Long>{
        private User user;
        @Override
        protected Long doInBackground(String... strings) {
            user = new User(strings[0],strings[1]);
            return userDao.insertUser(user);
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            if (aLong == -1){
                Toast.makeText(SignUpActivity.this, "Operation Failed", Toast.LENGTH_SHORT).show();
            }else {
                userNameSession.setLoginUserName(user.getUserName());
                loginSession.setLoginState(true);
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}