package com.example.where2eat.domain.viewmodel;

import android.app.Application;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.where2eat.domain.model.Restaurant;
import com.example.where2eat.roomdatabase.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class RestaurantViewModal extends AndroidViewModel {

    private MutableLiveData<List<Restaurant>> restaurantList = new MutableLiveData<>(new ArrayList<>());

    private MutableLiveData<Restaurant> restaurant = new MutableLiveData<>(null);

    {
        new Thread(() -> {
            List<Restaurant> list = DBHelper.getInstance(getApplication()
                    .getApplicationContext()).getRestaurantDao().findAll();
            restaurantList.postValue(list);
        }).start();
    }

    public RestaurantViewModal(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Restaurant>> getRestaurantList() {
        return restaurantList;
    }

    public LiveData<Restaurant> getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            this.restaurant.setValue(restaurant);
        } else {
            this.restaurant.postValue(restaurant);
        }
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            this.restaurantList.setValue(restaurantList);
        } else {
            this.restaurantList.postValue(restaurantList);
        }
    }

}
