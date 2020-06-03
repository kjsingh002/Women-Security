package com.kjsingh002.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kjsingh002.Activities.R;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter {
    ArrayList<String> names, phones;


    public MyArrayAdapter(@NonNull Context context, ArrayList<String> names, ArrayList<String> phones) {
        super(context, R.layout.contacts_layout);
        this.names = names;
        this.phones = phones;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_layout,parent,false);
        TextView textViewNames = view.findViewById(R.id.contact_names);
        TextView textViewPhones = view.findViewById(R.id.contact_phones);
        textViewNames.setText(names.get(position));
        textViewPhones.setText(phones.get(position));
        return view;
    }

    @Override
    public int getCount() {
        return names.size();
    }
}