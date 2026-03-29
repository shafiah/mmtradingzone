package com.example.mmtradingzone.network;

import com.example.mmtradingzone.models.ResponseModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    Call<ResponseModel> uploadVideo(@Part MultipartBody.Part file,
                                    @Part("paid") Boolean paid,
                                    @Part("title") RequestBody title);

    @Multipart
    @POST("file/img/upload")
    Call<ResponseModel> uploadImage(@Part MultipartBody.Part file,
                                    @Part("paid")RequestBody paid,
                                    @Part("title")RequestBody title);

    @Multipart
    @POST("file/pdf/upload")
    Call<ResponseModel> uploadPdf(@Part MultipartBody.Part file,
                                  @Part("paid")RequestBody paid,
                                  @Part("title")RequestBody title);

    @GET("/file/get/list/{fileType}/{phoneNumber}")
    Call<List<FilesModel>> getVideoList(
            @Path("fileType") String fileType,
            @Path("phoneNumber") String phoneNumber
    );

    // 🔥 NEW API (ADD THIS)
    @GET("/file/get/premium/{fileType}")
    Call<List<FilesModel>> getPremiumVideos(
            @Path("fileType") String fileType
    );

    @GET("file/get/list/{fileType}/{phoneNumber}")
    Call<List<FilesModel>> getImageList(
            @Path("fileType") String fileType,
            @Path("phoneNumber") String phoneNumber
    );

    @GET("/file/get/premium/{fileType}")
    Call<List<FilesModel>> getPremiumImages(
            @Path("fileType") String fileType
    );

    // ⭐ NEW
    // ⭐ FINAL PDF API (SAME PATTERN)
    @GET("/file/get/list/{fileType}/{phoneNumber}")
    Call<List<FilesModel>> getPdfList(
            @Path("fileType") String fileType,
            @Path("phoneNumber") String phoneNumber
    );

    @GET("/file/get/premium/{fileType}")
    Call<List<FilesModel>> getPremiumPdf(
            @Path("fileType") String fileType
    );

    @DELETE("file/delete/file/{id}")
    Call<ResponseBody> deleteFile(@Path("id") Long id);
}

