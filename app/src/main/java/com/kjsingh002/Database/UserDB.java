package com.kjsingh002.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.kjsingh002.Dao.UserDao;
import com.kjsingh002.Entity.User;

@Database(entities = {User.class},version = 2)
public abstract class UserDB extends RoomDatabase {

    public abstract UserDao getUserDao();
}
