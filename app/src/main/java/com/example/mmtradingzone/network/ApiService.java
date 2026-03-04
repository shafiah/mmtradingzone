package com.example.mmtradingzone.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/payment/create-order")
    Call<Map<String, Object>> createOrder();
}