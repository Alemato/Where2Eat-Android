package com.example.where2eat.tools;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequests {

    private RequestQueue queue;

    private static volatile VolleyRequests instance = null;

    public static synchronized VolleyRequests getInstance(Context context) {
        if (instance == null) {
            synchronized (VolleyRequests.class) {
                if (instance == null) instance = new VolleyRequests(context);
            }
        }
        return instance;
    }

    private VolleyRequests(Context context) {
        queue = Volley.newRequestQueue(context);
    }

}
