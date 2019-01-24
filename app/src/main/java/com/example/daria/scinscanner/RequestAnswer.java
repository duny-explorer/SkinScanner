package com.example.daria.scinscanner;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RequestAnswer {
    @Multipart
    @POST("/result557578")
    Call<Object> result(@Part MultipartBody.Part file);
}
