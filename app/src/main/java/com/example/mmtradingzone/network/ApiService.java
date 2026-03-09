package com.example.mmtradingzone.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/user/payment/create-order")
    Call<Map<String, Object>> createOrder();

    // ✅ Register User API
    @POST("/user/create")
    Call<UserResponse> registerUser(@Body UserRequest userRequest);
    // ✅ Login User API
    @POST("/user/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}