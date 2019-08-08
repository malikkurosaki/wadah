package dev.makuro.probussystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.AuthProvider;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // kode login
    private static final int LOGIN_MASUK = 567;

    // membuat posisi layout
    private static final int INTRO = 0;
    private static final int HOME = 1;


    //view animator as container
    private ViewAnimator container;
    private SwipeRefreshLayout swipeRefreshLayout;

    // firebase
    private FirebaseUser user;
    private FirebaseAuth auth;
    private List<AuthUI.IdpConfig> configs;

    // String
    private String nomerHp;

    // database lokal
    private TinyDB db;

    //tambahan
    private String TAG = "-->";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //deklarasi database lokal
        db = new TinyDB(this);

        //firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        configs = Collections.singletonList(new AuthUI.IdpConfig.PhoneBuilder().build());

        container = findViewById(R.id.container);
        swipeRefreshLayout = findViewById(R.id.swipe_resfresh);

        // pembukaan intro
        container.setDisplayedChild(INTRO);

        // check apakan user sudah login , jika belum akan dibawa kehalaman login
        if (user == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(configs).build(),LOGIN_MASUK);
        }else {
            nomerHp = user.getPhoneNumber();
            // tunggu dua detik untuk intro muncul
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    container.setDisplayedChild(HOME);
                }
            },2000);

        }

        //swipe refresh
        swipeRefreshLayout.setNestedScrollingEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                Toast.makeText(getApplicationContext(),"ya",Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        // membuat timer task
        new CountDownTimer(1000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                start();
            }
        }.start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // mencocokan hasil kpode loginnya
        if (requestCode == LOGIN_MASUK){
            if (resultCode == RESULT_OK){
                container.setDisplayedChild(HOME);
                user = auth.getCurrentUser();
                nomerHp = user.getPhoneNumber();
                recreate();
            }
        }


    }
}
