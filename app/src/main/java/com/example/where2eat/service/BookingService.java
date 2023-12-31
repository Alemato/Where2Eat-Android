package com.example.where2eat.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.ArrayMap;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.where2eat.domain.model.Booking;
import com.example.where2eat.domain.model.CreateBooking;
import com.example.where2eat.roomdatabase.DBHelper;
import com.example.where2eat.service.utility.EmptyResponseJsonObjectRequest;
import com.example.where2eat.service.utility.Requests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookingService extends IntentService {
    public static final String KEY_BOOKING_ACTION = "download_booking_action";
    public static final String KEY_CREATE_BOOKING_OBJ = "createBookingOBJ";

    public static final int ACTION_DOWNLOAD_BOOKINGS = 0;
    public static final int ACTION_BOOKING_CREATE = 1;

    public static final String DOWNLOAD_BOOKINGS_SUCCESSFUL = "download_bookings_completed";
    public static final String BOOKING_CREATE_SUCCESSFUL = "create_new_booking_completed";
    public static final String DOWNLOAD_BOOKINGS_ERROR = "download_bookings_error";
    public static final String BOOKING_CREATE_ERROR = "create_new_booking_error";
    public static final String INTERNET_BOOKING_ERROR = "internet_booking_error";
    public static final String INTERNET_CREATE_BOOKING_ERROR = "internet_create_booking_error";

    public BookingService() {
        super(BookingService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        int action = Objects.requireNonNull(intent).getIntExtra(KEY_BOOKING_ACTION, -1);
        if (action == ACTION_DOWNLOAD_BOOKINGS) {
            if (isNetworkConnected()) {
                downloadAllPrenotazioni();
            } else {
                Intent intentError = new Intent(INTERNET_BOOKING_ERROR);
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(intentError);
            }
        } else if (action == ACTION_BOOKING_CREATE) {
            if (isNetworkConnected()) {
                CreateBooking createBooking = (CreateBooking) intent.getSerializableExtra(KEY_CREATE_BOOKING_OBJ);
                if (createBooking != null) createBookingServer(createBooking);
            } else {
                Intent intentError = new Intent(INTERNET_CREATE_BOOKING_ERROR);
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(intentError);
            }
        }// Nothing
    }

    private void downloadAllPrenotazioni() {
        String token = DBHelper.getInstance(getApplicationContext()).getUserDao().getToken();
        if (token == null || token.equals("")) {
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
                List<Booking> bookingList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject item = response.optJSONObject(i);
                    if (item == null) continue;
                    bookingList.add(Booking.parseJson(item));
                }
                new Thread(() -> {
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

    private void createBookingServer(CreateBooking createBooking) {
        String token = DBHelper.getInstance(getApplicationContext()).getUserDao().getToken();
        if (token == null || token.equals("")) {
            Intent intent = new Intent(BOOKING_CREATE_ERROR);
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .sendBroadcast(intent);
            System.err.println("Token vuoto");
            return;
        }
        Requests request = new Requests(getApplicationContext());
        JSONObject jsonBody = createBooking.encodeJson();
        EmptyResponseJsonObjectRequest createBookingReq = new EmptyResponseJsonObjectRequest(Request.Method.POST, "http://192.168.0.160:8080/api/prenotazioni", jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent intent = new Intent();
                intent.setAction(BOOKING_CREATE_SUCCESSFUL);
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(BOOKING_CREATE_ERROR);
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

        request.getQueue().add(createBookingReq);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        if (isConnected) {
            try {
                InetAddress ipAddr = InetAddress.getByName("www.google.com");
                return !ipAddr.toString().equals("");
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}