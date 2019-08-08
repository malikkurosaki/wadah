package dev.malikkurosaki.probussystem;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mapzen.speakerbox.Speakerbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.tiagohm.markdownview.Utils;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private boolean pertama = true;
    private RecyclerView perkenalanContainer;
    private String TAG = "-->";
    private DatabaseReference db;
    private CardView lanjutContainer;
    private ImageButton lanjut;
    private LinearLayout loading;
    private int durasi = 300;

    private List<Map<String,Object>> dataList;

    private Speakerbox speakerbox;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        PENGGUNAAN THEME
        ===============
        - karena penggunaan splashscreen maka diwajibkan mencantumkan theme
         */
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("berita");

        /*
        MENDAPATKAN INFO DARI BUNDLE / MASAGING
        =======================================
        - firebase massaging
        - activity

         */


        //startActivity(new Intent(MainActivity.this,Main5Activity.class));
        // firebase
        if (!FirebaseApp.getApps(this).isEmpty()) FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        speakerbox = new Speakerbox(getApplication());

        /*
        PENENTUAN | PENGECEKAN BAHASA
        =============================
        - bahasa yang digunakan harus bahasa inggris
         */
        String bahasa = Locale.getDefault().getDisplayLanguage();
        if (!bahasa.equals("English")){
            speakerbox.play("selamat datang di probus sistem");

        }else{
            speakerbox.play("welcome to the probus system");
        }

        /*
        PENDEKLARASIAN
        ==============
        - deklarasi view
         */
        db = FirebaseDatabase.getInstance().getReference();
        lanjut = findViewById(R.id.lanjut);
        lanjutContainer = findViewById(R.id.lajutContainer);
        loading = findViewById(R.id.loading);


        /*
        PENGECEKAN PERMISI
        ==================
        - audio
        - baca external
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},500);
        }


        /*
        AKTIFITAS PERKENALAN | SLIDE PERTAMA BUKA
        =========================================
        - pertama buka

         */
        db.child("aktifitas").child("perkenalan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                YoYo.with(Techniques.ZoomOutDown)
                        .duration(durasi)
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                loading.setVisibility(View.GONE);
                                //startActivity(new Intent(MainActivity.this,Main2Activity.class));
                            }
                        })
                        .playOn(loading);

                dataList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String,Object> ambil = (HashMap<String,Object>)ds.getValue();
                    Map<String,Object> ambil2 = new HashMap<>();
                    for (Map.Entry<String,Object> en : ambil.entrySet()){
                        ambil2.put(en.getKey(),en.getValue());

                    }
                    dataList.add(ambil2);
                }

                final PerkenalanHelperAdapter helperAdapter = new PerkenalanHelperAdapter(MainActivity.this,dataList);
                perkenalanContainer = findViewById(R.id.perkenalancontainer);
                perkenalanContainer.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
                perkenalanContainer.setAdapter(helperAdapter);
                SnapHelper snapHelper  = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(perkenalanContainer);
                helperAdapter.setDiklik(new PerkenalanHelperAdapter.KetikaPerkenalanDiklik() {
                    @Override
                    public void jadinya(View view, int position) {

                    }
                });

                // munculkan tombol halaman lanjut
                perkenalanContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        LinearLayoutManager manager = ((LinearLayoutManager)perkenalanContainer.getLayoutManager());
                        final int pos = manager.findLastCompletelyVisibleItemPosition();
                        int jumblah = perkenalanContainer.getAdapter().getItemCount();

                        if ((pos+1) == jumblah){
                            lanjutContainer.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.BounceInUp)
                                    .duration(durasi)
                                    .playOn(lanjutContainer);



                        }else {
                            lanjutContainer.setVisibility(View.GONE);
                        }
                        super.onScrolled(recyclerView, dx, dy);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"internetnya coyy",Toast.LENGTH_LONG).show();
            }
        });

        /*
        MEMUNCULKAN PERINGATAN BAHASA
        =============================
        - ganti bahasa ke bahasa inggris
         */
        final BottomSheetDialog peringatan = new BottomSheetDialog(MainActivity.this);
        View peringatanBhs = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_peringatan_bahasa,null,false);
        peringatan.setContentView(peringatanBhs);
        final TextView gantiBahasaYa = peringatanBhs.findViewById(R.id.gantiBahasaYa);

        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bahasa = Locale.getDefault().getDisplayLanguage();
                if (!bahasa.equals("English")){
                    peringatan.show();

                    return;
                }
                speakerbox.stop();
                YoYo.with(Techniques.ZoomOutDown)
                        .duration(durasi)
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                startActivity(new Intent(MainActivity.this,Main2Activity.class));
                            }
                        })
                        .playOn(lanjutContainer);
            }
        });

        gantiBahasaYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"pilih bahasa inggris",Toast.LENGTH_LONG).show();

                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
            }
        });




    }


    /*
    PENANGANAN PERMINTAAN PERMISI
    =============================
    - kode 500
    - permisi audio
    - permisi baca external
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 500){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                String bahasa = Locale.getDefault().getDisplayLanguage();
                if (!bahasa.equals("English")){
                    Toast.makeText(getApplicationContext(),"selamat , aplikasi telah diijinkan , silahka melanjutkan",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(),"congratulations, the application has been permitted, please continue",Toast.LENGTH_LONG).show();
                }

            }
        }

    }



}
