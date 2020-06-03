package com.kjsingh002.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.kjsingh002.Adapters.MyArrayAdapter;
import com.kjsingh002.Dao.UserDao;
import com.kjsingh002.Database.UserDB;
import com.kjsingh002.Entity.User;
import com.kjsingh002.Sessions.UserNameSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmergencyContactsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private UserDB userDB;
    private UserDao userDao;
    private UserNameSession userNameSession;
    private ArrayList<String> nameList,phoneList;
    private MyArrayAdapter myArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        userDB = Room.databaseBuilder(EmergencyContactsActivity.this,UserDB.class,"UsersData").build();
        userDao = userDB.getUserDao();
        userNameSession = new UserNameSession(EmergencyContactsActivity.this);
        listView = findViewById(R.id.list_of_emergency_contacts);
        new GetAllUsers().execute(userNameSession.getLoginUserName());
        toolbar = findViewById(R.id.emergency_contacts_toolbar);
        setSupportActionBar(toolbar);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        PopupMenu popupMenu = new PopupMenu(EmergencyContactsActivity.this,v);
                        popupMenu.inflate(R.menu.contact_popup_menu);
                        popupMenu.show();
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.delete:
                                        Toast.makeText(EmergencyContactsActivity.this, nameList.get(position)+" Deleted", Toast.LENGTH_SHORT).show();
                                        nameList.remove(position);
                                        phoneList.remove(position);
                                        new UpdateContactName().execute();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        return true;
                    }
                });
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.emergency_contacts_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_contact){
            LinearLayout linearLayout = new LinearLayout(EmergencyContactsActivity.this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            final EditText name = new EditText(EmergencyContactsActivity.this);
            name.setHint("Enter name");
            final EditText phone = new EditText(EmergencyContactsActivity.this);
            phone.setHint("Enter phone");
            phone.setInputType(InputType.TYPE_CLASS_NUMBER);
            linearLayout.addView(name);
            linearLayout.addView(phone);
            new AlertDialog.Builder(EmergencyContactsActivity.this)
                    .setView(linearLayout)
                    .setTitle("Add Emergency Contact")
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(phone.getText())) {
                                name.setError("Field Required");
                                phone.setError("Field Required");
                            }else {
                                new AddContactName().execute(userNameSession.getLoginUserName(), name.getText().toString());
                                new AddContactPhone().execute(userNameSession.getLoginUserName(), phone.getText().toString());
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    class AddContactName extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            nameList.add(strings[1]);
            String names = "";
            try {
                JSONObject jsonObjectName = new JSONObject();
                jsonObjectName.put("contactNames",new JSONArray(nameList));
                names = jsonObjectName.toString();
                Log.i("Name",names);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return userDao.insertContactName(strings[0], names);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.i("Integer",integer+"");
            if (integer <= 0){
                Toast.makeText(EmergencyContactsActivity.this, "Operation Failed", Toast.LENGTH_SHORT).show();
            }else {
                new GetAllUsers().execute(userNameSession.getLoginUserName());
            }
        }
    }

    class AddContactPhone extends AsyncTask<String,Void,Integer>{
        String phones = "";
        @Override
        protected Integer doInBackground(String... strings) {
            phoneList.add(strings[1]);
            try {
                JSONObject jsonObjectPhone = new JSONObject();
                jsonObjectPhone.put("contactPhones",new JSONArray(phoneList));
                phones = jsonObjectPhone.toString();
                Log.i("Phone",phones);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return userDao.insertContactPhone(strings[0],phones);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer <= 0){
                Toast.makeText(EmergencyContactsActivity.this, "Operation Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetAllUsers extends AsyncTask<String,Void,User>{

        @Override
        protected User doInBackground(String... strings) {
            nameList = new ArrayList<>();
            phoneList = new ArrayList<>();
            return userDao.checkExistingUser(strings[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null){
                if (user.getContactName() != null && user.getContactPhone() != null){
                    Log.i("test","onPost");
                    try {
                        JSONObject jsonObjectName = new JSONObject(user.getContactName());
                        JSONArray list = jsonObjectName.optJSONArray("contactNames");
                        for (int i=0;i<list.length();i++){
                            nameList.add(list.getString(i));
                        }

                        JSONObject jsonObjectPhone = new JSONObject(user.getContactPhone());
                        JSONArray list2 = jsonObjectPhone.optJSONArray("contactPhones");
                        for (int i=0;i<list2.length();i++){
                            phoneList.add(list2.getString(i));
                            Log.i("Phone",list2.getString(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    myArrayAdapter = new MyArrayAdapter(EmergencyContactsActivity.this,nameList,phoneList);
                    listView.setAdapter(myArrayAdapter);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(EmergencyContactsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    class UpdateContactName extends AsyncTask<Void,Void,Integer>{
        String names;

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("contactNames",new JSONArray(nameList));
                names = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return userDao.updateContactName(userNameSession.getLoginUserName(),names);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer <=0){
                Toast.makeText(EmergencyContactsActivity.this, "Operation Failed", Toast.LENGTH_SHORT).show();
            }else{
                new UpdateContactPhone().execute();
            }
        }
    }

    class UpdateContactPhone extends AsyncTask<Void,Void,Integer> {
        String phones;

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("contactPhones",new JSONArray(phoneList));
                phones = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return userDao.updateContactPhone(userNameSession.getLoginUserName(),phones);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer <= 0){
                Toast.makeText(EmergencyContactsActivity.this, "Operation Failed", Toast.LENGTH_SHORT).show();
            }else {
                new GetAllUsers().execute(userNameSession.getLoginUserName());
            }
        }
    }
}