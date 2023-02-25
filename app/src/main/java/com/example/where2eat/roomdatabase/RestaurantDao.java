package com.example.where2eat.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.where2eat.domain.modal.Restaurant;

import java.util.List;

@Dao
public abstract class RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void save(Restaurant restaurant);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void save(List<Restaurant> restaurantList);

    @Delete
    public abstract void delete(Restaurant restaurant);

    @Query("SELECT * FROM restaurants ORDER BY id DESC")
    public abstract List<Restaurant> findAll();

    @Query("SELECT * FROM restaurants WHERE id = :id")
    public abstract Restaurant findById(long id);

/*
    @Query("SELECT id FROM restaurants ORDER BY id DESC LIMIT 1")
    public abstract long findLastId();

    @Transaction
    public void complexSave(Restaurant restaurant) {
        save(restaurant);
        long id = findLastId();
        restaurant.setId(id);
    }
    */
}
