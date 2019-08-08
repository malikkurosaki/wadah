package probus.makuro.probuszpizza;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PembantuApi {

    @GET("/api/getMemberApp")
    Call<List<JsonObject>> mintaDataCustomer();

    @GET("/api/getPointApp/{id}")
    Call<List<JsonObject>> mendapatkanDetailCustomer(@Path("id")String idnya);

}
