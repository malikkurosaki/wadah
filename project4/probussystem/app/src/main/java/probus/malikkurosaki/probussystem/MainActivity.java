package probus.malikkurosaki.probussystem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.notifContainer)
    RecyclerView notifContainer;

    private static final String UBUD_MALANG = "https://ubudmalang.probussystem.com:4458";
    private static final String PURIMAS = "https://purimas.probussystem.com:4450";
    private static final String URLNYA_EZEE = "https://live.ipms247.com";

    private static String URLNYA;

    private List<Map<String,Object>> listReservasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        URLNYA = UBUD_MALANG;

        Retrofit notifyRetro = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URLNYA)
                .build();

        PemanggilApi notifyPanggilApi = notifyRetro.create(PemanggilApi.class);
        Call<List<JsonObject>> notifyPanggilReservasi = notifyPanggilApi.reservation();
        notifyPanggilReservasi.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terimaReservasi = response.body();
                listReservasi = new ArrayList<>();
                for (JsonObject jadinya : terimaReservasi){
                    Map<String,Object> wadah = new Gson().fromJson(jadinya.toString(), HashMap.class);
                    Map<String,Object> wadah2 = new HashMap<>();
                    for (Map.Entry<String,Object> entry : wadah.entrySet()){
                        wadah2.put(entry.getKey(),entry.getValue());
                    }
                    listReservasi.add(wadah2);
                }

                Adapter_Notifikasi adapter_notifikasi =  new Adapter_Notifikasi(MainActivity.this,listReservasi);
                notifContainer.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                notifContainer.setAdapter(adapter_notifikasi);
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });

    }
}
