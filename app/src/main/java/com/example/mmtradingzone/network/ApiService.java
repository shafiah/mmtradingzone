package com.example.mmtradingzone.network;

import com.example.mmtradingzone.models.ResponseModel;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @POST("/user/payment/create-order")
    Call<Map<String, Object>> createOrder();

    // ✅ Register User API
    @POST("/user/create")
    Call<UserResponse> registerUser(@Body UserRequest userRequest);
    // ✅ Login User API
    @POST("/user/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);
    @Multipart
    @POST("file/vid/upload")
    Call<ResponseModel> uploadVideo(@Part MultipartBody.Part file);
}