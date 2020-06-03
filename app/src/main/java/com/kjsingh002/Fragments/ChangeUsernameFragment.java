package com.kjsingh002.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kjsingh002.Activities.LoginActivity;
import com.kjsingh002.Activities.R;
import com.kjsingh002.Dao.UserDao;
import com.kjsingh002.Database.UserDB;
import com.kjsingh002.Entity.User;
import com.kjsingh002.Sessions.LoginSession;
import com.kjsingh002.Sessions.UserNameSession;

public class ChangeUsernameFragment extends Fragment {
    private TextInputEditText oldUserName, password, newUserName;
    private Button update;
    private UserNameSession userNameSession;
    private UserDao userDao;
    private UserDB userDB;
    private LoginSession loginSession;

    public ChangeUsernameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_username, container, false);
        userDB = Room.databaseBuilder(getContext(),UserDB.class,"UsersData").build();
        userDao = userDB.getUserDao();
        loginSession = new LoginSession(getContext());
        userNameSession = new UserNameSession(getContext());
        oldUserName = view.findViewById(R.id.change_username_old_username);
        oldUserName.setText(userNameSession.getLoginUserName());
        password = view.findViewById(R.id.change_username_password);
        newUserName = view.findViewById(R.id.change_username_new_username);
        update = view.findViewById(R.id.change_username_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(password.getText()) && TextUtils.isEmpty(newUserName.getText())){
                    password.setError("Field Required");
                    newUserName.setError("Field Required");
                }else if (TextUtils.isEmpty(password.getText())){
                    password.setError("Field Required");
                }else if (TextUtils.isEmpty(newUserName.getText())){
                    newUserName.setError("Field Required");
                }else {
                    new CheckNewUsername().execute(newUserName.getText().toString());
                }
            }
        });
        return view;
    }

    class ChangeUsername extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            Log.i("Strings",strings[0] + strings[1] + strings[2]);
            return userDao.updateUsername(strings[0],strings[1],strings[2]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer <= 0){
                Toast.makeText(getContext(), "Operation Failed", Toast.LENGTH_SHORT).show();
            }else {
                loginSession.setLoginState(false);
                userNameSession.setLoginUserName("");
                final Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }

    class CheckNewUsername extends AsyncTask<String,Void,User>{

        @Override
        protected User doInBackground(String... strings) {
            return userDao.checkExistingUser(strings[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null){
                Toast.makeText(getContext(), "Username Exists - Please take another username", Toast.LENGTH_SHORT).show();
            }else {
                new ChangeUsername().execute(oldUserName.getText().toString(), password.getText().toString(), newUserName.getText().toString());
            }
        }
    }
}