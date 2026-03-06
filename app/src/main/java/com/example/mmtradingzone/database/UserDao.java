package com.example.mmtradingzone.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert
    void registerUser(User user);

    @Query("SELECT * FROM users WHERE mobile = :mobile AND password = :password LIMIT 1")
    User loginUser(String mobile, String password);

   // @Query("SELECT * FROM users WHERE mobile = :mobile LIMIT 1")
    //User checkMobileExists(String mobile);

    @Query("SELECT * FROM users WHERE mobile = :mobile LIMIT 1")
    User getUserByMobile(String mobile);

    @Query("UPDATE users SET deviceId = :deviceId WHERE mobile = :mobile")
    void updateDeviceId(String mobile, String deviceId);
}