package com.kjsingh002.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.kjsingh002.Entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insertUser(User user);

    @Query("UPDATE User SET contactName=:name AND contactPhone=:phone WHERE userName=:userName")
    int insertContacts(String userName, String name, String phone);

    @Query("UPDATE User SET contactName=:name WHERE userName=:userName")
    int insertContactName(String userName, String name);

    @Query("UPDATE User SET contactPhone=:phone WHERE userName=:userName")
    int insertContactPhone(String userName, String phone);

    @Query("SELECT * FROM User WHERE userName=:username")
    User checkExistingUser(String username);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE userName LIKE:userName AND password LIKE :password")
    User auth(String userName, String password);

    @Query("UPDATE User SET password=:newPassword WHERE userName=:username AND password=:oldPassword")
    int updatePassword(String username, String oldPassword, String newPassword);
}