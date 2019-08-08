package dev.malikkurosaki.probussystem;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("/api/getCustomer/{id}")
    Call<List<JsonObject>> getCustomer(@Path("id") String nama);



    @GET("/api/notifikasi")
    Call<List<JsonObject>> dapatkanNotifikasi();


    /*
    DASHBOARD NOMER 2
    =================
    - home
    - inhouse
    - reservetion
    - arrival
    - depature
     */

    @GET("/api/home")
    Call<JsonObject> home();

    @GET("/api/inHouse")
    Call<List<JsonObject>> inHouse();

    @GET("/api/reservation")
    Call<List<JsonObject>> reservation();

    @GET("api/arrival")
    Call<List<JsonObject>> arrival();

    @GET("api/departure")
    Call<List<JsonObject>> depature();


}
