package com.example.where2eat.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.where2eat.domain.model.User;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users LIMIT 1")
    User getUser();
    @Query("SELECT token FROM users LIMIT 1")
    String getToken();

    @Query("DELETE FROM users")
    void deleteAll();
}
