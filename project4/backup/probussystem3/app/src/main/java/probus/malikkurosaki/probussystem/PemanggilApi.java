package probus.malikkurosaki.probussystem;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PemanggilApi {

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


    // tomorrow
    @GET("api/arrivalTomorrow")
    Call<List<JsonObject>> arrivalTomorrow();

    @GET("api/departureTomorrow")
    Call<List<JsonObject>> departureTomorrow();

    @GET("api/inhouseTomorrow")
    Call<List<JsonObject>> inhouseTomorrow();

    // occupacy
    @GET("api/forecastOcc")
    Call<List<JsonObject>> forecastOcc();


    // api EZEE
    @Headers("Content-Type: application/json")
    @POST("/pmsinterface/reservation.php")
    Call<ResponseBody> ezeeResponse(@Body RequestBody body);

    // distribution
    @GET("api/getRoomrates")
    Call<JsonObject> getRoomrates();

}
