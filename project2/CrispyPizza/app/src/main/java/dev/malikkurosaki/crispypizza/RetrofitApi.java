package dev.malikkurosaki.crispypizza;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitApi {
    @GET("/api/user/{id}")
    Call<List<JsonObject>> getNama(@Path("id") String nama);

    @POST("/api/user/code/{id}")
    Call<List<JsonObject>> postCode(@Path("id") String codenya);
}
