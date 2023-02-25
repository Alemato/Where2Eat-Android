package com.example.where2eat.roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.where2eat.domain.modal.Restaurant;
import com.example.where2eat.domain.modal.User;

@Database(entities = {Restaurant.class, User.class}, version = 1)
public abstract class DBHelper extends RoomDatabase {

    public abstract RestaurantDao getRestaurantDao();

    public abstract UserDao getUserDao();

    private volatile static DBHelper instance = null;

    public synchronized static DBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) instance = Room.databaseBuilder(
                                context, DBHelper.class, "roomdatabase.db")
                        .build();
            }
        }
        return instance;
    }

    // protected DBHelper() {}
}
