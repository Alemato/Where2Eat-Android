package com.example.where2eat.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.where2eat.domain.modal.Booking;

import java.util.List;

@Dao
public interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(Booking booking);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void save(List<Booking> bookingList);

    @Delete
    public void delete(Booking booking);

    @Query("SELECT * FROM bookings ORDER BY id DESC")
    public List<Booking> findAll();

    @Query("SELECT * FROM bookings WHERE id = :id")
    public Booking findById(long id);

    @Query("DELETE FROM bookings")
    public void deleteAll();
}
