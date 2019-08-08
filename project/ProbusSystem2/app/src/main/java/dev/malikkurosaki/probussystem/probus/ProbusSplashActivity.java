package dev.malikkurosaki.probussystem.probus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mapzen.speakerbox.Speakerbox;
import com.white.progressview.CircleProgressView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.malikkurosaki.probussystem.ClassDashboard;
import dev.malikkurosaki.probussystem.JsonPlaceHolderApi;
import dev.malikkurosaki.probussystem.Main2Activity;
import dev.malikkurosaki.probussystem.R;
import dev.malikkurosaki.probussystem.RoomProductionAdapter;
import me.itangqi.waveloadingview.WaveLoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProbusSplashActivity extends AppCompatActivity {

    /*
    MAIN
    ====
     */
    @BindView(R.id.splash_loading)
    ProgressBar splashLoading;
    @BindView(R.id.splash_next)
    ImageButton splashNext;

    // speaker box
    private Speakerbox speakerbox;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.probus_splash_activity);
        ButterKnife.bind(this);


        speakerbox = new Speakerbox(getApplication());
        String bahasa = Locale.getDefault().getDisplayLanguage();
        if (!bahasa.equals("English")){
            speakerbox.play("selamat datang di probus sistem");
        }else{
            speakerbox.play("welcome to the probus system");
        }


        splashNext.setVisibility(View.GONE);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                splashLoading.setVisibility(View.GONE);
                splashNext.setVisibility(View.VISIBLE);

            }
        }.start();

        splashNext.setOnClickListener(view -> {
            startActivity(new Intent(ProbusSplashActivity.this,ProbusActivity.class));
        });
    }



}
