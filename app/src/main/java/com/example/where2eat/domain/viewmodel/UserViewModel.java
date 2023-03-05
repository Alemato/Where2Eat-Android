package com.example.where2eat.domain.viewmodel;

import android.app.Application;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.where2eat.domain.model.User;
import com.example.where2eat.roomdatabase.DBHelper;

import java.util.Objects;

public class UserViewModel extends AndroidViewModel {
    private MutableLiveData<User> user = new MutableLiveData<>(null);

    {
        new Thread(() -> {
            User user1 = DBHelper.getInstance(getApplication().getApplicationContext()).getUserDao().getUser();
            if (user1 != null && !Objects.equals(user1, new User())) {
                user.postValue(user1);
            }
        }).start();
    }

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void setUserMainThread(User user) {
        // MainThread
        this.user.setValue(user);
    }

    public void setUserAnotherThread(User user) {
        // Another Thread
        this.user.postValue(user);
    }

    public void setUser(User user){
        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            // MainThread
            this.user.setValue(user);
        } else {
            // Another Thread
            this.user.postValue(user);
        }
    }
}
