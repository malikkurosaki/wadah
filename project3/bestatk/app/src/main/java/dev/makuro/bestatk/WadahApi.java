package dev.makuro.bestatk;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WadahApi {

    @GET("/api/semua/barang")
    Call<List<JsonObject>> semuaBarang();
}
