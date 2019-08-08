package com.fahmyuliono.bismillah;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PemanggilApi {

    @GET("/api/content")
    Call<List<JsonObject>> contennya();

    @GET("/api/user/komen/")
    Call<List<JsonObject>> komenanNya();

    @FormUrlEncoded
    @POST("/api/user/komen")
    Call<List<JsonObject>> koromKomenanNya(@FieldMap Map<String,String> isiNya);
}
