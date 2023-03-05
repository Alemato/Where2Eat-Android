package com.example.where2eat.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.where2eat.domain.model.User;
import com.example.where2eat.domain.model.UserNamePassword;
import com.example.where2eat.roomdatabase.DBHelper;
import com.example.where2eat.service.utility.JsonObjectRequestAndHeader;
import com.example.where2eat.service.utility.Requests;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.Objects;


public class AuthService extends IntentService {

    public static final String KEY_ACTION = "action";
    public static final String KEY_USER_PASSWORD = "userPassword";

    public static final int ACTION_LOGIN = 0;

    public static final String LOGIN_SUCCESSFUL = "completed";
    public static final String LOGIN_ERROR = "error";
    public static final String INTERNET_LOGIN_ERROR = "internet_login_error";

    public AuthService() {
        super(AuthService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (isNetworkConnected()) {
            int action = Objects.requireNonNull(intent).getIntExtra(KEY_ACTION, -1);
            if (action == ACTION_LOGIN) {
                UserNamePassword userNamePassword = (UserNamePassword) intent.getSerializableExtra(KEY_USER_PASSWORD);
                if (userNamePassword != null)
                    loginDownloadData(userNamePassword);
            }
        } else {
            Intent intentError = new Intent(INTERNET_LOGIN_ERROR);
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .sendBroadcast(intentError);
        }
        // Nothing
    }

    private void loginDownloadData(UserNamePassword userNamePassword) {
        Requests request = new Requests(getApplicationContext());
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", userNamePassword.getUsername());

            jsonBody.put("password", userNamePassword.getPassword());

            JsonObjectRequestAndHeader authReq = new JsonObjectRequestAndHeader(Request.Method.POST, "http://192.168.0.160:8080/api/login", jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    User user = new User();
                    JSONObject item = response.optJSONObject("data");
                    JSONObject headers = response.optJSONObject("headers");
                    if (item == null || headers == null) {
                        Intent intent = new Intent(LOGIN_ERROR);
                        LocalBroadcastManager.getInstance(getApplicationContext())
                                .sendBroadcast(intent);
                    }
                    if (item != null) user = User.parseJson(item);
                    if (headers != null)
                        user.setToken("Bearer " + headers.optString("access_token"));

                    User finalUser = user;
                    new Thread(() -> {
                        DBHelper.getInstance(getApplicationContext()).getUserDao().deleteAll();
                        DBHelper.getInstance(getApplicationContext()).getRestaurantDao().deleteAll();
                        DBHelper.getInstance(getApplicationContext()).getUserDao().save(finalUser);

                        Intent intent = new Intent();
                        intent.setAction(LOGIN_SUCCESSFUL);
                        LocalBroadcastManager.getInstance(getApplicationContext())
                                .sendBroadcast(intent);
                    }).start();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Intent intent = new Intent(LOGIN_ERROR);
                    LocalBroadcastManager.getInstance(getApplicationContext())
                            .sendBroadcast(intent);
                    System.err.println(error);
                }
            });

            request.getQueue().add(authReq);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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
