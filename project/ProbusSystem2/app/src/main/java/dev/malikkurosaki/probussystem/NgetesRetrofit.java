package dev.malikkurosaki.probussystem;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NgetesRetrofit {

    private Context context;

    NgetesRetrofit(Context context1){
        this.context = context1;
    }

    public void dapatkanCustomer(String orang,String url){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<JsonObject>> panggil = jsonPlaceHolderApi.getCustomer(orang);
        panggil.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> teriama = response.body();
                for (JsonObject apa : teriama){
                    Log.i("-->", "onResponse: "+apa.get("nama_tamu").getAsString());
                }
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });

    }
    public static String namanya(Context context){
        Paper.init(context);
        return Paper.book().read("kunciNama");
    }

}

