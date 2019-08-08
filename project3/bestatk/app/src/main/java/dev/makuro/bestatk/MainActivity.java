package dev.makuro.bestatk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ViewAnimator;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // main
    private ViewAnimator mainViewAnim;

    //  home
    private RecyclerView homeRec;

    // penetapan nama layout
    private final int HOME = 0;

    private final String URLNYA = "http://bestatk.herokuapp.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*
    PENDEKLARASIAN
    ==============
    - deklarasi view
    - get id
     */

        mainViewAnim = findViewById(R.id.main_viewAnim);
        homeRec = findViewById(R.id.home_rec);


        /*
        PENETAPAN AWAL VIEW
        ==================
         */
        mainViewAnim.setDisplayedChild(HOME);

        /*
        RETROFIT DAPATKAN SEMUA BARANG
        =============================
         */
        Retrofit barang = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URLNYA)
                .build();

    }

}
