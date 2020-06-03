package com.kjsingh002.Fragments;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.kjsingh002.Activities.MapActivity;
import com.kjsingh002.Activities.R;
import com.kjsingh002.Location.AccessLastLocation;

public class MainScreenFragment extends Fragment {
    private ImageView myLocation,nearbyHospitals,nearbyPharmacies;
    private AccessLastLocation accessLastLocation;

    public MainScreenFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        accessLastLocation = new AccessLastLocation(getContext());
        myLocation = view.findViewById(R.id.my_location);
        nearbyHospitals = view.findViewById(R.id.nearby_hospitals);
        nearbyPharmacies = view.findViewById(R.id.nearby_pharmacy);
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
        return view;
    }
}