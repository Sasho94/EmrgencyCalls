package com.example.user.emergencycall.Services;

import com.example.user.emergencycall.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by User on 12.6.2017 г..
 */

public interface URLPaths {

    @POST("/alarms")
    Call<User> sendUser(@Body User user);


    @Multipart
    @POST("/uploadAttachment")
    Call<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );

}
