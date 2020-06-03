package com.kjsingh002.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.kjsingh002.Fragments.ChangePasswordFragment;
import com.kjsingh002.Fragments.MainScreenFragment;
import com.kjsingh002.Location.AccessLastLocation;
import com.kjsingh002.Sessions.LoginSession;
import com.kjsingh002.Sessions.UserNameSession;

public class MainActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LoginSession loginSession;
    private UserNameSession userNameSession;
    private AccessLastLocation accessLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accessLastLocation = new AccessLastLocation(MainActivity.this);
        loginSession = new LoginSession(MainActivity.this);
        userNameSession = new UserNameSession(MainActivity.this);
        myToolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(myToolbar);
        drawerLayout = findViewById(R.id.my_drawer);
        toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,myToolbar,R.string.nav_open,R.string.nav_close);
        getSupportFragmentManager().beginTransaction().add(R.id.frames_container,new MainScreenFragment()).commit();
        toggle.syncState();
        navigationView = findViewById(R.id.my_navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        userNameSession.setLoginUserName("");
                        loginSession.setLoginState(false);
                        final Intent intentLogout = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intentLogout);
                        finish();
                        return true;
                    case R.id.emergency_contacts:
                        final Intent intentContacts = new Intent(MainActivity.this,EmergencyContactsActivity.class);
                        startActivity(intentContacts);
                        return true;
                    case R.id.change_password:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frames_container,new ChangePasswordFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    default:
                        return false;
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}