package com.example.mmtradingzone.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://18.206.151.182:8085/";
    private static Retrofit retrofit;

    public static Retrofit getClient(Context context) {

        if (retrofit == null) {

                // 🔹 Gson lenient parser (fix JsonReader error)
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}