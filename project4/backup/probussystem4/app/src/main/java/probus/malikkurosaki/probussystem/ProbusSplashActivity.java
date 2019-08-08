package probus.malikkurosaki.probussystem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mapzen.speakerbox.Speakerbox;
import com.ncorti.slidetoact.SlideToActView;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    SlideToActView splashNext;

    private String TAG = "-->";

    // speaker box
    private Speakerbox speakerbox;
    private int IJIN = 123;

    private String URLNYA_EZEE = "https://live.ipms247.com";

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE},IJIN);

        }

        splashNext.setOnSlideCompleteListener(slideToActView -> {
            startActivity(new Intent(ProbusSplashActivity.this,Activity_User.class));
            finish();
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IJIN){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"permisi has granted click next",Toast.LENGTH_LONG).show();
                recreate();
            }
        }
    }
}
