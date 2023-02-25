package com.example.where2eat.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DownloadService extends IntentService {

    public static final String KEY_ACTION = "action";

    public static final int ACTION_DOWNLOAD = 0;

    public static final String ACTION_COMPLETED = "completed";
    public static final String ACTION_ERROR = "error";

    public DownloadService() {
        super(DownloadService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        int action = Objects.requireNonNull(intent).getIntExtra(KEY_ACTION, -1);
        if (action == ACTION_DOWNLOAD) {
            downloadData();
        }// Nothing
    }

    private void downloadData() {
        Requests request = new Requests(getApplicationContext());
/*
        JsonArrayRequest downloadRequest = new JsonArrayRequest(
                getString(R.string.server_url),
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    List<Autovelox> autoveloxList = new ArrayList<>();
                    for(int i = 0; i < response.length(); i++) {
                        JSONObject item = response.optJSONObject(i);
                        if(item == null) continue;

                        autoveloxList.add(Autovelox.parseJson(item));
                    }

                    new Thread(() -> {
                        Database.getInstance(getApplicationContext()).getAutoveloxDao().deleteAll();
                        Database.getInstance(getApplicationContext())
                                .getAutoveloxDao()
                                .save(autoveloxList);

                        Intent intent = new Intent();
                        intent.setAction(ACTION_COMPLETED);
                        LocalBroadcastManager.getInstance(getApplicationContext())
                                .sendBroadcast(intent);
                    }).start();
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Intent intent = new Intent(ACTION_ERROR);
                LocalBroadcastManager.getInstance(getApplicationContext())
                        .sendBroadcast(intent);

                System.err.println(error);
            }
        });
        request.getQueue().add(downloadRequest);
        */
    }
}
