package com.example.where2eat.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.ArrayMap;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.where2eat.domain.modal.Booking;
import com.example.where2eat.roomdatabase.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookingService extends IntentService {
    public static final String KEY_BOOKING_ACTION = "download_booking_action";

    public static final int ACTION_DOWNLOAD_BOOKINGS = 0;
    public static final int ACTION_BOOKING_CREATE = 1;

    public static final String DOWNLOAD_BOOKINGS_SUCCESSFUL = "download_bookings_completed";
    public static final String BOOKING_CREATE_SUCCESSFUL = "create_new_booking_completed";
    public static final String DOWNLOAD_BOOKINGS_ERROR = "download_bookings_error";
    public static final String BOOKING_CREATE_ERROR = "create_new_booking_error";

    public BookingService() {
        super(BookingService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int action = Objects.requireNonNull(intent).getIntExtra(KEY_BOOKING_ACTION, -1);
        if (action == ACTION_DOWNLOAD_BOOKINGS) {
            downloadAllPrenotazioni();
        } // Nothing
    }

    private void downloadAllPrenotazioni() {
        String token = DBHelper.getInstance(getApplicationContext()).getUserDao().getToken();
        if(token == null || token.equals("")) {
            Intent intent = new Intent(DOWNLOAD_BOOKINGS_ERROR);
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .sendBroadcast(intent);
            System.err.println("Token vuoto");
            return;
        }
        Requests request = new Requests(getApplicationContext());
        JsonArrayRequest downloadRequest = new JsonArrayRequest("http://192.168.0.160:8080/api/prenotazioni", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response);
                List<Booking> bookingList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject item = response.optJSONObject(i);
                    if (item == null) continue;
                    System.out.println("item");
                    System.out.println(item);
                    bookingList.add(Booking.parseJson(item));
                }
                System.out.println(bookingList);
                new Thread(()->{
                    DBHelper.getInstance(getApplicationContext()).getBookingDao().deleteAll();
                    DBHelper.getInstance(getApplicationContext()).getBookingDao().save(bookingList);

                    Intent intent = new Intent();
                    intent.setAction(DOWNLOAD_BOOKINGS_SUCCESSFUL);
                    LocalBroadcastManager.getInstance(getApplicationContext())
                            .sendBroadcast(intent);
                }).start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(DOWNLOAD_BOOKINGS_ERROR);
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(intent);

                System.err.println(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> mHeaders = new ArrayMap<String, String>();
                mHeaders.put("Authorization", token);
                return mHeaders;
            }
        };
        request.getQueue().add(downloadRequest);
    }
}