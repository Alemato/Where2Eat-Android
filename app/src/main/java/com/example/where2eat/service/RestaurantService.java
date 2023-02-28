package com.example.where2eat.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.where2eat.domain.modal.Restaurant;
import com.example.where2eat.roomdatabase.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantService extends IntentService {

    public static final String KEY_DOWNLOAD_RESTAURANTS = "download_restaurants_action";

    public static final int DOWNLOAD_RESTAURANTS = 0;

    public static final String DOWNLOAD_RESTAURANTS_COMPLETED = "download_restaurants_completed";
    public static final String DOWNLOAD_RESTAURANTS_ERROR = "download_restaurants_error";

    public RestaurantService() {
        super(RestaurantService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        System.out.println("onHandleIntentPrima");
        int action = Objects.requireNonNull(intent).getIntExtra(KEY_DOWNLOAD_RESTAURANTS, -1);

        if (action == DOWNLOAD_RESTAURANTS) {
            System.out.println("onHandleIntent");
            restaurantsDownloadData();
        }// Nothing
    }

    private void restaurantsDownloadData() {
        Requests request = new Requests(getApplicationContext());
        JsonArrayRequest downloadRequest = new JsonArrayRequest("http://192.168.0.160:8080/api/ristoranti", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("@@@@@@@@@@@@@@@@");
                System.out.println(response);
                List<Restaurant> restaurantList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject item = response.optJSONObject(i);
                    if (item == null) continue;
                    System.out.println("item");
                    System.out.println(item);
                    restaurantList.add(Restaurant.parseJson(item));
                }

                new Thread(() -> {
                    DBHelper.getInstance(getApplicationContext()).getRestaurantDao().deleteAll();
                    DBHelper.getInstance(getApplicationContext())
                            .getRestaurantDao()
                            .save(restaurantList);

                    Intent intent = new Intent();
                    intent.setAction(DOWNLOAD_RESTAURANTS_COMPLETED);
                    LocalBroadcastManager.getInstance(getApplicationContext())
                            .sendBroadcast(intent);
                }).start();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(DOWNLOAD_RESTAURANTS_ERROR);
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(intent);

                System.err.println(error);
            }
        });
        request.getQueue().add(downloadRequest);
    }
}
