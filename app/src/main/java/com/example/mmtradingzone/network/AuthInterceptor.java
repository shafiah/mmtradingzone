package com.example.mmtradingzone.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.mmtradingzone.LoginActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        SharedPreferences prefs =
                context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);

        String phone = prefs.getString("PHONE", "");
        String deviceId = prefs.getString("DEVICE_ID", "");

        System.out.println("Phone Header: " + phone);
        System.out.println("Device Header: " + deviceId);

        Request original = chain.request();

        Request request = original.newBuilder()
                .header("phoneNumber", phone)
                .header("deviceId", deviceId)
                .build();

        Response response = chain.proceed(request);

        // 🔴 SESSION EXPIRED
        if (response.code() == 401) {

            prefs.edit().clear().apply();

            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

        return response;
    }
}