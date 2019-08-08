package dev.malikkurosaki.probussystem;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main6Activity extends AppCompatActivity {


    private ImageView notifGambar;
    private TextView notifJudul;
    private TextView notifIsi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        notifGambar = findViewById(R.id.notif_gambar);
        notifJudul = findViewById(R.id.notif_judul);
        notifIsi = findViewById(R.id.notif_isi);

        Retrofit notifRetro = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://probussystem.herokuapp.com")
                .build();
        JsonPlaceHolderApi apinya = notifRetro.create(JsonPlaceHolderApi.class);
        Call<List<JsonObject>> panggil = apinya.dapatkanNotifikasi();
        panggil.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terima = response.body();
                Map<String,String> ambil = new HashMap<>();
                Toast.makeText(getApplicationContext(),"proses",Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });

    }
}
