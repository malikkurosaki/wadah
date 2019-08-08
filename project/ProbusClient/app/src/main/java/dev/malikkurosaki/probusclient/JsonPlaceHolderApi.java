package dev.malikkurosaki.probusclient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("api/getCustomer")
    Call<List<Post>> getPost();

}
