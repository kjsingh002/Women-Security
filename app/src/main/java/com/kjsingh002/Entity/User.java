package com.kjsingh002.Entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    String userName;

    @ColumnInfo
    String password;

    @Nullable
    @ColumnInfo
    String contactName;

    @Nullable
    @ColumnInfo
    String contactPhone;

    public User(@NonNull String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setContactName(@Nullable String contactName) {
        this.contactName = contactName;
    }

    public void setContactPhone(@Nullable String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Nullable
    public String getContactName() {
        return contactName;
    }

    @Nullable
    public String getContactPhone() {
        return contactPhone;
    }
}