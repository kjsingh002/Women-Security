package com.kjsingh002.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kjsingh002.Dao.UserDao;
import com.kjsingh002.Database.UserDB;
import com.kjsingh002.Entity.User;
import com.kjsingh002.Sessions.LoginSession;
import com.kjsingh002.Sessions.UserNameSession;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText userName;
    private TextInputEditText password;
    private LoginSession loginSession;
    private UserDB userDB;
    private UserDao userDao;
    private TextView displayUsers;
    private UserNameSession userNameSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeFields();
    }

    private void initializeFields() {
        loginSession = new LoginSession(LoginActivity.this);
        userNameSession = new UserNameSession(LoginActivity.this);
        if (loginSession.getLoginState()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        displayUsers = findViewById(R.id.users);
        userDB = Room.databaseBuilder(LoginActivity.this,UserDB.class,"UsersData").build();
        userDao = userDB.getUserDao();
        userName = findViewById(R.id.login_activity_username);
        password = findViewById(R.id.login_activity_password);
    }

    public void displayUsers(View view){
        displayUsers.setText("");
        new DisplayUsers().execute();
    }

    public void goToSignUpActivity(View view){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void goToMainActivity(View view){
        if (TextUtils.isEmpty(userName.getText()) && TextUtils.isEmpty(password.getText())){
            userName.setError("Field Required");
            password.setError("Field Required");
        }else if (TextUtils.isEmpty(userName.getText())){
            userName.setError("Field Required");
        }else if (TextUtils.isEmpty(password.getText())){
            password.setError("Field Required");
        }else {
            new AuthUser().execute(userName.getText().toString(), password.getText().toString());
        }
    }

    class DisplayUsers extends AsyncTask<Void,Void, List<User>>{

        @Override
        protected List<User> doInBackground(Void... voids) {
            return userDao.getAllUsers();
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            for (int i=0; i<users.size();i++){
                displayUsers.append(users.get(i).getUserName() + " " + users.get(i).getPassword() +" "+ users.get(i).getContactName()+" "+ users.get(i).getContactPhone() + '\n');
            }
        }
    }

    class AuthUser extends AsyncTask<String,Void,User>{

        @Override
        protected User doInBackground(String... strings) {
            return userDao.auth(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null){
                userNameSession.setLoginUserName(user.getUserName());
                loginSession.setLoginState(true);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(LoginActivity.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}