package com.kjsingh002.Fragments;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.kjsingh002.Activities.MapActivity;
import com.kjsingh002.Activities.R;
import com.kjsingh002.Dao.UserDao;
import com.kjsingh002.Database.UserDB;
import com.kjsingh002.Entity.User;
import com.kjsingh002.Location.AccessLastLocation;
import com.kjsingh002.Sessions.UserNameSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainScreenFragment extends Fragment {
    private ImageView myLocation,nearbyHospitals,nearbyPharmacies,myHome,sendLocation;
    private AccessLastLocation accessLastLocation;
    private UserNameSession userNameSession;
    private UserDB userDB;
    private UserDao userDao;
    private ArrayList<String> names,phones;

    public MainScreenFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        accessLastLocation = new AccessLastLocation(getContext());
        userNameSession = new UserNameSession(getContext());
        userDB = Room.databaseBuilder(getContext(),UserDB.class,"UsersData").build();
        userDao = userDB.getUserDao();
        names = new ArrayList<>();
        phones = new ArrayList<>();
        new RetrieveUserEmergencyContacts().execute(userNameSession.getLoginUserName());
        myLocation = view.findViewById(R.id.my_location);
        myHome = view.findViewById(R.id.my_home);
        nearbyHospitals = view.findViewById(R.id.nearby_hospitals);
        nearbyPharmacies = view.findViewById(R.id.nearby_pharmacy);
        sendLocation = view.findViewById(R.id.send_my_location);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        nearbyHospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task<Location> task = accessLastLocation.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Uri uri = Uri.parse("geo:"+location.getLatitude()+","+location.getLongitude()+"?q=Hospitals Nearby");
                        final Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });
            }
        });
        nearbyPharmacies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task<Location> task = accessLastLocation.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Uri uri = Uri.parse("geo:"+location.getLatitude()+","+location.getLongitude()+"?q=Pharmacies Nearby");
                        final Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });
            }
        });
        myHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task<Location> task = accessLastLocation.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Uri uri = Uri.parse("geo:"+location.getLatitude()+","+location.getLongitude()+"?q=My Home");
                        final Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    }
                });
            }
        });
        sendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task<Location> task = accessLastLocation.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        String message = "https://www.google.com/maps?q="+location.getLatitude()+","+location.getLongitude();
                        for (int i=0;i<names.size();i++){
                            Toast.makeText(getContext(), "Message sent to - "+names.get(i), Toast.LENGTH_SHORT).show();
                        }
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("+16505556789",null,message,null,null);
                    }
                });
            }
        });
        return view;
    }

    class RetrieveUserEmergencyContacts extends AsyncTask<String,Void,User>{

        @Override
        protected User doInBackground(String... strings) {
            return userDao.checkExistingUser(strings[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            if (user != null){
                if (user.getContactName() != null && user.getContactPhone() != null){
                    try {
                        JSONObject jsonObject = new JSONObject(user.getContactName());
                        JSONArray name = jsonObject.optJSONArray("contactNames");
                        for (int i=0; i<name.length();i++){
                            names.add(name.getString(i));
                            Log.i("Names",name.getString(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(user.getContactPhone());
                        JSONArray phone = jsonObject.optJSONArray("contactPhones");
                        for (int i=0;i<phone.length();i++){
                            phones.add(phone.getString(i));
                            Log.i("Phones",phone.getString(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}