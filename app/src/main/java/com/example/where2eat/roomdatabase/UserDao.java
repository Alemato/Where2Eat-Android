package com.example.where2eat.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.where2eat.domain.modal.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user LIMIT 1")
    User getUser();

    @Query("SELECT token FROM user LIMIT 1")
    String getTocken();

}
