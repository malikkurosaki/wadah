package probus.makuro.probuszpizza;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonObject;
import com.ramotion.foldingcell.FoldingCell;

import net.glxn.qrgen.android.QRCode;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.folding_cell)
    FoldingCell fc;
    @BindView(R.id.menu_bottom)
    BottomNavigationView menuBottom;
    @BindView(R.id.behavior1)
    CardView behavior1;
    @BindView(R.id.animator1)
    ViewAnimator animator1;

    @BindView(R.id.gambar1)
    ImageView gambar1;
    BottomSheetBehavior behaviorBawah1;

    @BindView(R.id.kembali1)
    ImageButton kembali1;
    @BindView(R.id.loding)
    ProgressBar loding;

    /*
    HALAMAN 1
    ========
     */
    @BindView(R.id.hal1Swipe)
    SwipeRefreshLayout hal1Swipe;

    /*
    HALAMAN2
    =======
     */
    @BindView(R.id.hal2Poin)
    TextView hal2Poin;

    /*
    FIREBASE
    =======
     */
    private List<AuthUI.IdpConfig> configs;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private final int LOGIN = 500;

    // bawahan
    private final String URLNYA = "https://presto-online.barongpos.com";
    private List<String> kodenya;
    private String TAG = "-->";

    /*
    LAYOUT 2
    ========
     */
    @BindView(R.id.nc_spin)
    NiceSpinner ncSpin;
    private int IJIN_SMS = 555;
    @BindView(R.id.hal2Swipe)
    SwipeRefreshLayout hal2Swipe;

    /*
    HALAMAN 3
    =========
     */
    @BindView(R.id.hal3Refresh)
    SwipeRefreshLayout hal3Refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*
        SQLITE DATABASE
        ===============
         */
        /*SQLiteDatabase db = openOrCreateDatabase("orang",Context.MODE_PRIVATE,null);
        db.execSQL("create table if not exists orang(id integer primary key autoincrement not null,nama text not null);");
        db.execSQL("insert into orang(id,nama) values(null,'halo');");
        Cursor cursor = db.rawQuery("select * from orang",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Log.i(TAG, "onCreate: "+cursor.getString(1));
            cursor.moveToNext();
        }
        db.close();*/




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS},IJIN_SMS);
            return;
        }

        // firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        /*
        LOGIN MENGGUNAKAN NOMOR HP
        =========================
        -  seperti wa
         */
        configs = Collections.singletonList(new AuthUI.IdpConfig.PhoneBuilder().build());
        if (user == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(configs).build(),LOGIN);
            return;
        }

        Toast.makeText(getApplicationContext(),"welcome ",Toast.LENGTH_LONG).show();

        animator1.setDisplayedChild(0);
        behaviorBawah1 = BottomSheetBehavior.from(behavior1);
        behaviorBawah1.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        behaviorBawah1.setPeekHeight(0);

        gambar1.setOnClickListener(v -> {
            fc.toggle(false);
        });
        kembali1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });

        menuBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_bawah1:
                        animator1.setDisplayedChild(0);
                        tutupBawah2();
                        break;
                    case R.id.menu_bawah2:
                        animator1.setDisplayedChild(1);
                        tutupBawah2();
                        break;
                    case R.id.menu_bawah3:
                        animator1.setDisplayedChild(2);
                        tutupBawah2();
                        break;
                    case R.id.menu_bawah4:
                        animator1.setDisplayedChild(3);
                        tutupBawah();
                        break;
                }
                return true;
            }
        });

        /*
        KRTIKA MENU BAWAH DI KLIK 2 X ATAU LEBIH
        =======================================
         */
        menuBottom.setOnNavigationItemReselectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_bawah4){
                tutupBawah();
            }

        });



        kodeCustomer();

        /*
        SWIPE TO REFRESH
        ===============
         */

        hal1Swipe.setOnRefreshListener(() -> {
            ld(true);
            hal1Swipe.setRefreshing(false);
            ld(false);
        });

        hal2Swipe.setOnRefreshListener(() -> {
            ld(true);
            kodeCustomer();
            hal2Swipe.setRefreshing(false);
            ld(false);
        });

        hal3Refresh.setOnRefreshListener(() -> {
            ld(true);
            hal3Refresh.setRefreshing(false);
            ld(false);
        });
    }


    /*
    MENGATASI ERROR KETIKA BEHAVIOR TIDAK TERTUTUP
    =============================================
     */
    void tutupBawah(){
        if (behaviorBawah1.getState() == BottomSheetBehavior.STATE_EXPANDED){
            behaviorBawah1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else {
            behaviorBawah1.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
    void tutupBawah2(){
        if (behaviorBawah1.getState() == BottomSheetBehavior.STATE_EXPANDED){
            behaviorBawah1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    /*
    MENDAPATKAN KODE FOUCHER DARI API
    =================================
     */
    void kodeCustomer(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URLNYA)
                .build();
        PembantuApi pembantuApi = retrofit.create(PembantuApi.class);
        Call<List<JsonObject>> panggilData = pembantuApi.mintaDataCustomer();
        panggilData.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
               kodenya = new ArrayList<>();
                List<JsonObject> terima = response.body();

                for (JsonObject data1 : terima){
                    kodenya.add(data1.get("kd_cus").getAsString());

                }
                ncSpin.attachDataSource(kodenya);

                /*
                MEMUNCULKAN KODE QR CORE
                ========================
                 */

            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });


        /*
        KETIKA SPINNER DI KLIK
        ======================
        - mendapatkan nilai yang sudah diminta
         */
        ncSpin.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            ld(true);
            Retrofit dataRetro = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URLNYA)
                    .build();
            PembantuApi dataPembantu = dataRetro.create(PembantuApi.class);
            Call<List<JsonObject>> dataPanggil = dataPembantu.mendapatkanDetailCustomer(kodenya.get(position));
            dataPanggil.enqueue(new Callback<List<JsonObject>>() {
                @Override
                public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                    List<JsonObject> terima = response.body();
                    hal2Poin.setText(String.valueOf(terima.size()));

                    Bitmap myBitmap = QRCode.from(String.valueOf(terima.size())).bitmap();
                    ImageView gambarQr = findViewById(R.id.gambar_qr);
                    gambarQr.setImageBitmap(myBitmap);
                    ld(false);
                }

                @Override
                public void onFailure(Call<List<JsonObject>> call, Throwable t) {

                }
            });
        });


    }


    /*
    KETIKA LOGIN
    ===========
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN){
            if (resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(),"loged successfuly",Toast.LENGTH_LONG).show();
                recreate();
            }
        }
    }


    /*
    PERMINTAAN PERMISI
    ==================
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IJIN_SMS){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"permisi granted",Toast.LENGTH_LONG).show();
                recreate();
            }
        }
    }

    // loading ....
    void ld(boolean munculkan){
        if (munculkan){
            loding.setVisibility(View.VISIBLE);
        }else {
            loding.setVisibility(View.GONE);
        }
    }
}
