package com.kjsingh002.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;

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
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_layout,parent,false);
        TextView textViewNames = view.findViewById(R.id.contact_names);
        TextView textViewPhones = view.findViewById(R.id.contact_phones);
        textViewNames.setText(names.get(position));
        textViewPhones.setText(phones.get(position));
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(),view);
                popupMenu.inflate(R.menu.contact_popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                Toast.makeText(getContext(), names.get(position)+" Deleted", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
                return true;
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return names.size();
    }
}