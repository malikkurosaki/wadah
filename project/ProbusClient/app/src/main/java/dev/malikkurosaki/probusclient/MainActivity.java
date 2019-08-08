package dev.malikkurosaki.probusclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        result = findViewById(R.id.result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://103.207.96.250:443/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Post>> call = jsonPlaceHolderApi.getPost();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()){
                    result.setText(response.code());
                    return;
                }

                /*List<Post> posts = response.body();
                for (Post post : posts ){
                    String csontent = "";
                    csontent += post.getText() +"\n\n\n";

                    result.append(csontent);
                }*/
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                result.setText(t.getMessage());
            }
        });
    }
}
