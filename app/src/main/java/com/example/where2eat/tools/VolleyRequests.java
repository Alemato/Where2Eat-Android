package com.example.where2eat.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyRequests {

    private RequestQueue queue;
    private RequestQueue imageQueue;
    private ImageLoader imageLoader;

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
        imageQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(imageQueue, new ImageLoader.ImageCache() {

            private LruCache<String, Bitmap> cache = new LruCache<>(50);

            @Nullable
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
