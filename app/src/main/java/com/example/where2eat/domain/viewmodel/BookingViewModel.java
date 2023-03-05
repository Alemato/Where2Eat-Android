package com.example.where2eat.domain.viewmodel;

import android.app.Application;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.where2eat.domain.model.Booking;
import com.example.where2eat.roomdatabase.DBHelper;

import java.util.ArrayList;
import java.util.List;


public class BookingViewModel extends AndroidViewModel {
    private MutableLiveData<List<Booking>> bookingList = new MutableLiveData<>(new ArrayList<>());

    {
        new Thread(() -> {
            List<Booking> list = DBHelper.getInstance(getApplication()
                    .getApplicationContext()).getBookingDao().findAll();
            bookingList.postValue(list);
        }).start();
    }

    public BookingViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Booking>> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            this.bookingList.setValue(bookingList);
        } else {
            this.bookingList.postValue(bookingList);
        }
    }

}
