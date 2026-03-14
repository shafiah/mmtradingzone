package com.example.mmtradingzone.network;

import com.example.mmtradingzone.models.ResponseModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @Multipart
    @POST("file/img/upload")
    Call<ResponseModel> uploadImage(@Part MultipartBody.Part file);

    @Multipart
    @POST("file/pdf/upload")
    Call<ResponseModel> uploadPdf(@Part MultipartBody.Part file);

    @GET("/file/get/list/{fileType}/{phoneNumber}")
    Call<List<FilesModel>> getVideoList(
            @Path("fileType") String fileType,
            @Path("phoneNumber") String phoneNumber
    );
}