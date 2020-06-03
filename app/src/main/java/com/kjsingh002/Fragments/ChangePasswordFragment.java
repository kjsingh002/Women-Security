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
import com.kjsingh002.Sessions.LoginSession;
import com.kjsingh002.Sessions.UserNameSession;

public class ChangePasswordFragment extends Fragment {
    private UserNameSession userNameSession;
    private TextInputEditText userName,oldPassword,newPassword;
    private UserDB userDB;
    private UserDao userDao;
    private Button buttonUpdate;
    private LoginSession loginSession;

    public ChangePasswordFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        userNameSession = new UserNameSession(getContext());
        userName = view.findViewById(R.id.change_password_username);
        oldPassword = view.findViewById(R.id.change_password_old_password);
        newPassword = view.findViewById(R.id.change_password_new_password);
        userName.setText(userNameSession.getLoginUserName());
        userDB = Room.databaseBuilder(getContext(),UserDB.class,"UsersData").build();
        userDao = userDB.getUserDao();
        loginSession = new LoginSession(getContext());
        buttonUpdate = view.findViewById(R.id.change_password_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(oldPassword.getText()) && TextUtils.isEmpty(newPassword.getText())){
                    oldPassword.setError("Field Required");
                    newPassword.setError("Field Required");
                }else if (TextUtils.isEmpty(oldPassword.getText())){
                    oldPassword.setError("Field Required");
                }else if (TextUtils.isEmpty(newPassword.getText())){
                    newPassword.setError("Field Required");
                }else {
                    new ChangePassword().execute(userName.getText().toString(), oldPassword.getText().toString(), newPassword.getText().toString());
                }
            }
        });
        return view;
    }

    class ChangePassword extends AsyncTask<String,Void, Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            return userDao.updatePassword(strings[0], strings[1], strings[2]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer <= 0) {
                Toast.makeText(getContext(), "Operation Failed", Toast.LENGTH_SHORT).show();
            }else {
                userNameSession.setLoginUserName("");
                loginSession.setLoginState(false);
                final Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
    }
}