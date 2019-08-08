package probus.malikkurosaki.probussystem;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ncornette.cache.OkCacheControl;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.white.progressview.CircleProgressView;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import me.itangqi.waveloadingview.WaveLoadingView;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static probus.malikkurosaki.probussystem.Class_Dashboard.tanggal;

public class Activity_Probus extends AppCompatActivity {

    /*
    DATABASE
    ========
     */
    private SQLiteDatabase db;

    @BindView(R.id.dashboard_tanggal)
    TextView dashboardTanggal;
    @BindView(R.id.cir_prog)
    CircleProgressView cirProg;
    @BindView(R.id.circle_progress_normal2)
    CircleProgressView circleProgressNormal2;
    @BindView(R.id.panahKiri)
    ImageView panahKiri;
    @BindView(R.id.panahKanan)
    ImageView panahKanan;
    @BindView(R.id.circle_progress_normal3)
    CircleProgressView circleProgressNormal3;
    @BindView(R.id.arr)
    TextView arr;
    @BindView(R.id.inH)
    TextView inH;
    @BindView(R.id.dep)
    TextView dep;
    @BindView(R.id.bok)
    TextView bok;
    @BindView(R.id.rs_wav)
    WaveLoadingView rsWav;
    @BindView(R.id.rs_per)
    TextView rsPer;
    @BindView(R.id.rs_today)
    TextView rsToday;
    @BindView(R.id.rs_yesterday)
    TextView rsYesterday;
    @BindView(R.id.emj1)
    EmojiconTextView emj1;
    @BindView(R.id.ever_wave)
    WaveLoadingView everWave;
    @BindView(R.id.ever_per)
    TextView everPer;
    @BindView(R.id.ever_tod)
    TextView everTod;
    @BindView(R.id.ever_yes)
    TextView everYes;
    @BindView(R.id.emj2)
    EmojiconTextView emj2;
    @BindView(R.id.rev_wave)
    WaveLoadingView revWave;
    @BindView(R.id.rev_per)
    TextView revPer;
    @BindView(R.id.rev_tod)
    TextView revTod;
    @BindView(R.id.rev_yes)
    TextView revYes;
    @BindView(R.id.emj3)
    EmojiconTextView emj3;
    @BindView(R.id.leng_wave)
    WaveLoadingView lengWave;
    @BindView(R.id.leng_per)
    TextView lengPer;
    @BindView(R.id.leng_tod)
    TextView lengTod;
    @BindView(R.id.leng_yes)
    TextView lengYes;
    @BindView(R.id.emj4)
    EmojiconTextView emj4;
    @BindView(R.id.un_wave)
    WaveLoadingView unWave;
    @BindView(R.id.un_per)
    TextView unPer;
    @BindView(R.id.un_tod)
    TextView unTod;
    @BindView(R.id.un_yes)
    TextView unYes;
    @BindView(R.id.emj5)
    EmojiconTextView emj5;
    @BindView(R.id.ges_wave)
    WaveLoadingView gesWave;
    @BindView(R.id.ges_per)
    TextView gesPer;
    @BindView(R.id.ges_tod)
    TextView gesTod;
    @BindView(R.id.ges_yes)
    TextView gesYes;
    @BindView(R.id.emj6)
    EmojiconTextView emj6;
    @BindView(R.id.booking)
    TextView booking;
    @BindView(R.id.confirmed)
    TextView confirmed;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.noshow)
    TextView noshow;
    @BindView(R.id.house_used)
    TextView houseUsed;
    @BindView(R.id.compliment)
    TextView compliment;
    @BindView(R.id.pay_room)
    TextView payRoom;
    @BindView(R.id.out_of_order)
    TextView outOfOrder;
    @BindView(R.id.room_production)
    RecyclerView roomProduction;
    @BindView(R.id.chartContainer)
    RecyclerView chartContainer;
    @BindView(R.id.dashBSwipe)
    SwipeRefreshLayout dashBSwipe;
    @BindView(R.id.laporanKembali)
    ImageButton laporanKembali;
    @BindView(R.id.judulLihatLaporan)
    TextView judulLihatLaporan;
    @BindView(R.id.laporanTab)
    TabLayout laporanTab;
    @BindView(R.id.laporanRecycler)
    RecyclerView laporanRecycler;
    @BindView(R.id.laporanSwipe)
    SwipeRefreshLayout laporanSwipe;
    @BindView(R.id.probus_main_container)
    ViewAnimator probusMainContainer;
    @BindView(R.id.probusMenuSamping1)
    TextView probusMenuSamping1;
    @BindView(R.id.probusSideMenu)
    NavigationView probusSideMenu;
    @BindView(R.id.probusDrawer)
    DrawerLayout probusDrawer;
    @BindView(R.id.probusBottomNav)
    BottomNavigationView probusBottomNav;
    @BindView(R.id.probusLoading)
    ProgressBar probusLoading;
    @BindView(R.id.dasArrivalAduld)
    TextView dasArrivalAduld;
    @BindView(R.id.dasArrivalChild)
    TextView dasArrivalChild;
    @BindView(R.id.dasInhouseAdult)
    TextView dasInhouseAdult;
    @BindView(R.id.dasInhouseChild)
    TextView dasInhouseChild;
    @BindView(R.id.dasDerpatureAdult)
    TextView dasDerpatureAdult;
    @BindView(R.id.dasDerpatureChild)
    TextView dasDerpatureChild;
    @BindView(R.id.dasBookingAdult)
    TextView dasBookingAdult;
    @BindView(R.id.dasBookingChild)
    TextView dasBookingChild;
    @BindView(R.id.tombolDistribution)
    LinearLayout tombolDistribution;
    @BindView(R.id.dasboardNeste)
    NestedScrollView dasboardNeste;


    // listnya tody
    private List<Map<String, Object>> listArrivalToday;
    private List<Map<String, Object>> listInhouseToday;
    private List<Map<String, Object>> listDepatureToday;
    private List<Map<String, Object>> listBookingToday;

    // list tomorrow
    private List<Map<String, Object>> listArrivalTomorrow;
    private List<Map<String, Object>> listDepartureTomorrow;
    private List<Map<String, Object>> listInhouseTomorrow;

    // tab atas
    private TabLayout.Tab tabToday;
    private TabLayout.Tab tabTomorrow;


    // chart view
    private List<Map<String, Object>> listOcc;

    public static final int HOME = 0;
    public static final int LAYOUT2 = 1;
    public static final int LAYOUT3 = 2;
    public static final int LAYOUT_LIHAT_LAPORAN = 3;
    public static final int DISTRIBUTION = 4;

    private static final String UBUD_MALANG = "https://ubudmalang.probussystem.com:4458";
    private static final String PURIMAS = "https://purimas.probussystem.com:4450";
    private static final String URLNYA_EZEE = "https://live.ipms247.com";
    private static final String URLNYA = UBUD_MALANG;

    private List<Map<String, Object>> roomListNya;
    private int nilai = 0;

    private List<Integer> berapaArrivalAdult;
    private List<Integer> berapaArrivalChild;

    private String TAG = "-->";

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.probus_activity);
        ButterKnife.bind(this);

        context = this;

        db = openOrCreateDatabase("PROBUS",MODE_PRIVATE,null);
        db.execSQL("create table if not exists catatan_log(" +
                "id integer primary key autoincrement not null," +
                "waktu text not null," +
                "keterangan text not null)");

        // munculkan loading
        probusLoading.setVisibility(View.VISIBLE);

        /*
        SWIPE REFRESH
        =============
         */

        dashboardNya();
        dashBSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dashBSwipe.setRefreshing(true);
                setOmbakFull();
                setEmojiPasif();
                setTodNoll();
                dashboardNya();

                new CountDownTimer(3000, 100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        dashBSwipe.setRefreshing(false);
                    }
                }.start();

            }
        });

        probusBottomNav.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.probusMenuBawah1:
                    tutupDrawer();
                    probusMainContainer.setDisplayedChild(0);
                    break;
                case R.id.probusMenuBawah2:
                    tutupDrawer();
                    probusMainContainer.setDisplayedChild(1);
                    break;
                case R.id.probusMenuBawah3:
                    tutupDrawer();
                    probusMainContainer.setDisplayedChild(2);
                    break;
                case R.id.probusMenuBawah4:
                    probusDrawer.openDrawer(Gravity.RIGHT);
                    break;
            }
            return true;
        });


        /*
        PENANGANA PANEL DICLICK
        ======================
         */
        arr.setOnClickListener(v -> {
            probusMainContainer.setDisplayedChild(LAYOUT_LIHAT_LAPORAN);
            judulLihatLaporan.setText("Arrival");
            laporanHelperNya(listArrivalToday);
        });
        inH.setOnClickListener(v -> {
            laporanHelperNya(listInhouseToday);
            probusMainContainer.setDisplayedChild(LAYOUT_LIHAT_LAPORAN);
            judulLihatLaporan.setText("InHouse");
        });
        dep.setOnClickListener(v -> {
            laporanHelperNya(listDepatureToday);
            probusMainContainer.setDisplayedChild(LAYOUT_LIHAT_LAPORAN);
            judulLihatLaporan.setText("Departure");
        });
        bok.setOnClickListener(v -> {
            laporanHelperNya(listBookingToday);
            probusMainContainer.setDisplayedChild(LAYOUT_LIHAT_LAPORAN);
            judulLihatLaporan.setText("Booking");
        });


         /*
         LAPORAN BAWAH
         =====================
          */
        laporanSwipe.setOnRefreshListener(() -> {
            Toast.makeText(getApplicationContext(), "updating ...", Toast.LENGTH_LONG).show();
            laporanSwipe.setRefreshing(false);
        });

        laporanKembali.setOnClickListener(v -> {
            probusMainContainer.setDisplayedChild(HOME);
        });

        tabToday = laporanTab.newTab().setText("Today");
        tabTomorrow = laporanTab.newTab().setText("Tomorrow");
        laporanTab.addTab(tabToday);
        laporanTab.addTab(tabTomorrow);
        laporanTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        switch (judulLihatLaporan.getText().toString().trim()) {
                            case "Arrival":
                                laporanHelperNya(listArrivalToday);
                                YoYo.with(Techniques.FadeInUp).duration(200).playOn(laporanRecycler);
                                break;
                            case "InHouse":
                                laporanHelperNya(listInhouseToday);
                                break;
                            case "Departure":
                                laporanHelperNya(listDepatureToday);
                                YoYo.with(Techniques.FadeInUp).duration(200).playOn(laporanRecycler);
                                break;
                            case "Booking":
                                laporanHelperNya(listBookingToday);
                                break;
                        }
                        break;
                    case 1:
                        switch (judulLihatLaporan.getText().toString().trim()) {
                            case "Arrival":
                                laporanHelperNya(listArrivalTomorrow);
                                YoYo.with(Techniques.FadeInUp).duration(200).playOn(laporanRecycler);
                                break;
                            case "InHouse":
                                /*laporanTab.selectTab(tabToday);
                                Toast.makeText(getApplicationContext(),"Nothing To Show",Toast.LENGTH_LONG).show();*/
                                laporanHelperNya(listInhouseTomorrow);
                                YoYo.with(Techniques.FadeInUp).duration(200).playOn(laporanRecycler);
                                break;
                            case "Departure":
                                laporanHelperNya(listDepartureTomorrow);
                                YoYo.with(Techniques.FadeInUp).duration(200).playOn(laporanRecycler);
                                break;
                            case "Booking":
                                laporanTab.selectTab(tabToday);
                                Toast.makeText(getApplicationContext(), "Nothing To Show", Toast.LENGTH_LONG).show();
                                break;
                        }
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tombolDistribution.setOnClickListener(view -> {
            probusMainContainer.setDisplayedChild(DISTRIBUTION);
            probusDrawer.closeDrawers();
            distributionVoid();

        });



    }


    /*
    PERBATASAN -------
    ==================
     */


    // PENGATURAN RETROFIT MODE OFFLINE
    boolean isConnected = false;
    public static final String BASE_URL = "https://lateralview.co";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";

    private Interceptor provideCacheInterceptor() {
        return chain -> {
            okhttp3.Response response = chain.proceed(chain.request());

            CacheControl cacheControl;

            if (isConnected) {
                cacheControl = new CacheControl.Builder()
                        .maxAge(0, TimeUnit.SECONDS)
                        .build();
            } else {
                cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();
            }

            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();

        };
    }

    private Interceptor provideOfflineCacheInterceptor() {
        return chain -> {
            Request request = chain.request();

            if (!isConnected) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build();
            }

            return chain.proceed(request);
        };
    }


    private Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(context.getCacheDir(), "http-cache"),
                    10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
            Log.e(TAG, "Could not create Cache!");
        }
        return cache;
    }
    
    public static class Catatan{
        public static boolean lognya(SQLiteDatabase database,String ket){
            database.execSQL("insert into catatan_log(id,waktu,keterangan) values(" +
                    "null,"+tanggal()+","+ket+") ");
            return true;
        }
    }


    /*
    DISTRIBUTION VOID
    =================
     */
    public void distributionVoid(){
        Retrofit retroDistribution = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URLNYA)
                .build();
        PemanggilApi pemanggilApiDistribution = retroDistribution.create(PemanggilApi.class);
        Call<JsonObject> pemangilDistribution = pemanggilApiDistribution.getRoomrates();
        pemangilDistribution.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject wadahDistribution = response.body();


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    List<Map<String,Object>> distributionApa(JsonObject object,String nama){
        List<Map<String,Object>> wadahRoomType = new ArrayList<>();
        JsonArray roomArray = new Gson().fromJson(object.get(nama),JsonArray.class);
        for (JsonElement rom : roomArray){
            Map<String,Object> room2 = new Gson().fromJson(rom.toString(),HashMap.class);
            Map<String,Object> room3 = new HashMap<>();
            for (Map.Entry<String,Object> roomEnt : room2.entrySet()){
                room3.put(roomEnt.getKey(),roomEnt.getValue());
            }
            wadahRoomType.add(room3);

        }
        return wadahRoomType;
    }

    public void laporanHelperNya(List<Map<String, Object>> targetNya) {
        Adapter_Laporan laporan_adapter = new Adapter_Laporan(this, targetNya);
        laporanRecycler.setLayoutManager(new LinearLayoutManager(this));
        laporanRecycler.setAdapter(laporan_adapter);
    }

    // folding activity
    public void lipatanView(View targetNya) {

    }

    public void tutupDrawer() {
        if (probusDrawer.isDrawerOpen(Gravity.RIGHT)) {
            probusDrawer.closeDrawers();
        }
    }


    // menuju ruang hiburan
    public void hiburan(){
        startActivity(new Intent(Activity_Probus.this,Activity_Hiburan.class));
        finish();
    }

    public void dashboardNya() {
         /*
        DASHBOARD
        ========
        - pemanggilan class
        - recyclerview
         */
        Class_Dashboard classDashboard = new Class_Dashboard(this);
        dashboardTanggal.setText(classDashboard.kalendar());

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .build();

        Retrofit dasbordRetro = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URLNYA)
                .client(okHttpClient)
                .build();
        PemanggilApi pemanggilApi = dasbordRetro.create(PemanggilApi.class);



        /*
        MENDAPATKAN ARRIVAL TODAY
        =========================
         */
        Call<List<JsonObject>> panggilArrival = pemanggilApi.arrival();
        panggilArrival.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {

                if (response.body() == null){
                    isConnected = true;
                }
                List<JsonObject> terima = response.body();
                arr.setText(String.valueOf(terima.size()));
                int ulang = terima.size() * 100;
                YoYo.with(Techniques.SlideInUp).duration(ulang).playOn(arr);
                listArrivalToday = new ArrayList<>();

                int berapaArrivalAdult = 0;
                int berapaArrivalChild = 0;
                for (JsonElement dp : terima) {
                    Map<String, Object> iniAr = new Gson().fromJson(dp.toString(), HashMap.class);
                    Map<String, Object> wadah = new HashMap<>();
                    for (Map.Entry<String, Object> ent : iniAr.entrySet()) {
                        wadah.put(ent.getKey(), ent.getValue());
                    }
                    listArrivalToday.add(wadah);
                    berapaArrivalAdult += Integer.parseInt(String.valueOf(iniAr.get("pax")));
                    berapaArrivalChild += Integer.parseInt(String.valueOf(iniAr.get("pax2")));
                }

                dasArrivalAduld.setText(String.valueOf(berapaArrivalAdult));
                dasArrivalChild.setText(String.valueOf(berapaArrivalChild));
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Log.i(TAG, "onFailure: time out jhon");
                probusLoading.setVisibility(View.GONE);
                isConnected = false;
            }
        });

        /*
        MENDAPATKAN INHOUSE TODAY
        =========================
         */
        Call<List<JsonObject>> panggilInhouse = pemanggilApi.inHouse();
        panggilInhouse.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terima = response.body();
                inH.setText(String.valueOf(terima.size()));
                int ulang = terima.size() * 100;
                YoYo.with(Techniques.SlideInUp).duration(ulang).playOn(inH);
                listInhouseToday = new ArrayList<>();

                int berapaInhouseAduld = 0;
                int berapaInhouseChild = 0;

                for (JsonObject inHouseObject : terima) {
                    Map<String, Object> inhs = new Gson().fromJson(inHouseObject.toString(), HashMap.class);
                    Map<String, Object> iinh = new HashMap<>();
                    for (Map.Entry<String, Object> enh : inhs.entrySet()) {
                        iinh.put(enh.getKey(), enh.getValue());
                    }
                    listInhouseToday.add(iinh);

                    berapaInhouseAduld += Integer.parseInt(String.valueOf(inhs.get("pax")));
                    berapaInhouseChild += Integer.parseInt(String.valueOf(inhs.get("pax2")));
                }

                dasInhouseAdult.setText(String.valueOf(berapaInhouseAduld));
                dasInhouseChild.setText(String.valueOf(berapaInhouseChild));
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });


        /*
        MENDAPATKAN INHOUSE TOMORROW
        ============================
         */
        Call<List<JsonObject>> callInHTom = pemanggilApi.inhouseTomorrow();
        callInHTom.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> wadah = response.body();
                listInhouseTomorrow = new ArrayList<>();


                for (JsonObject inHTom : wadah) {
                    Map<String, Object> terima1 = new Gson().fromJson(inHTom.toString(), HashMap.class);
                    Map<String, Object> terima2 = new HashMap<>();
                    for (Map.Entry<String, Object> entry : terima1.entrySet()) {
                        terima2.put(entry.getKey(), entry.getValue());
                    }
                    listInhouseTomorrow.add(terima2);

                }

            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });




        /*
        MENDAPATKAN DEPATURE TODAY
        =========================
         */
        Call<List<JsonObject>> panggilDepature = pemanggilApi.depature();
        panggilDepature.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terima = response.body();
                dep.setText(String.valueOf(terima.size()));
                int ulang = terima.size() * 100;
                YoYo.with(Techniques.SlideInUp).duration(ulang).playOn(dep);
                listDepatureToday = new ArrayList<>();

                int berapaDerpartureAdult = 0;
                int berapaDerpartureChild = 0;
                for (JsonObject depObject : terima) {
                    Map<String, Object> depTerima = new Gson().fromJson(depObject.toString(), HashMap.class);
                    Map<String, Object> depWadah = new HashMap<>();
                    for (Map.Entry<String, Object> enh : depTerima.entrySet()) {
                        depWadah.put(enh.getKey(), enh.getValue());
                    }
                    listDepatureToday.add(depWadah);

                    berapaDerpartureAdult += Integer.parseInt(String.valueOf(depTerima.get("pax")));
                    berapaDerpartureChild += Integer.parseInt(String.valueOf(depTerima.get("pax2")));
                }

                dasDerpatureAdult.setText(String.valueOf(berapaDerpartureAdult));
                dasDerpatureChild.setText(String.valueOf(berapaDerpartureChild));
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });


        /*
        MENDAPATAKAN RESERVATION / BOOKING TODAY
        ==============================
         */
        Call<List<JsonObject>> panggilReservation = pemanggilApi.reservation();
        panggilReservation.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terima = response.body();
                bok.setText(String.valueOf(terima.size()));
                int ulang = terima.size() * 100;
                YoYo.with(Techniques.SlideInUp).duration(ulang).playOn(bok);
                listBookingToday = new ArrayList<>();

                int jumblahBookingAdult = 0;
                int jumblahBookingChild = 0;
                for (JsonObject bokObject : terima) {
                    Map<String, Object> bokTerima = new Gson().fromJson(bokObject.toString(), HashMap.class);
                    Map<String, Object> bokWadah = new HashMap<>();
                    for (Map.Entry<String, Object> enh : bokTerima.entrySet()) {
                        bokWadah.put(enh.getKey(), enh.getValue());

                    }
                    listBookingToday.add(bokWadah);
                    jumblahBookingAdult += Integer.parseInt(String.valueOf(bokTerima.get("pax")));
                    jumblahBookingChild += Integer.parseInt(String.valueOf(bokTerima.get("pax2")));

                }

                dasBookingAdult.setText(String.valueOf(jumblahBookingAdult));
                dasBookingChild.setText(String.valueOf(jumblahBookingChild));

            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });



        /*
        MENDAPATKAN ARRIVAL TOMORROW
        ============================
         */
        Call<List<JsonObject>> callArrivalTomorrow = pemanggilApi.arrivalTomorrow();
        callArrivalTomorrow.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> wadahArrTom = response.body();
                listArrivalTomorrow = new ArrayList<>();
                for (JsonObject arrs : wadahArrTom) {
                    Map<String, Object> wadah1 = new Gson().fromJson(arrs.toString(), HashMap.class);
                    Map<String, Object> wadah2 = new HashMap<>();
                    for (Map.Entry<String, Object> ent : wadah1.entrySet()) {
                        wadah2.put(ent.getKey(), ent.getValue());
                    }
                    listArrivalTomorrow.add(wadah2);
                }
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });

        /*
        MENDAPATKAN DEPATURE TOMORROW
        =============================
         */
        Call<List<JsonObject>> callDepatureTomorrow = pemanggilApi.departureTomorrow();
        callDepatureTomorrow.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> wadahDepTom = response.body();
                listDepartureTomorrow = new ArrayList<>();
                for (JsonObject depTom : wadahDepTom) {
                    Map<String, Object> wadahDep = new Gson().fromJson(depTom.toString(), HashMap.class);
                    Map<String, Object> wadahDep2 = new HashMap<>();
                    for (Map.Entry<String, Object> entry : wadahDep.entrySet()) {
                        wadahDep2.put(entry.getKey(), entry.getValue());
                    }
                    listDepartureTomorrow.add(wadahDep2);
                }
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });



        /*
        MENDAPATKAN HOME
        ================
         */

            Call<JsonObject> panggilHome = pemanggilApi.home();
            panggilHome.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject house = response.body();

                /*
                MENU LINGKARAN ATAS
                ===================
                - yesterday
                - today
                - tomorrow
                 */
                    // occupationandroid:text="increase 14%"

                    try {
                        JsonObject occupancy = house.get("occupancy").getAsJsonObject();
                        cirProg.setProgress(Integer.parseInt(occupancy.get("yesterday").getAsString()));
                        cirProg.setProgressInTime(0, 2000);

                        circleProgressNormal2.setProgress(Integer.parseInt(occupancy.get("today").getAsString()));
                        circleProgressNormal2.setProgressInTime(0, 2000);

                        circleProgressNormal3.setProgress(Integer.parseInt(occupancy.get("tomorrow").getAsString()));
                        circleProgressNormal3.setProgressInTime(0, 2000);


                        int kemarin = Integer.parseInt(occupancy.get("yesterday").getAsString());
                        int hariIni = Integer.parseInt(occupancy.get("today").getAsString());
                        int besok = Integer.parseInt(occupancy.get("tomorrow").getAsString());

                        Drawable gambarNaik = getDrawable(R.drawable.up);
                        Drawable gambarTurun = getDrawable(R.drawable.down);

                        if (kemarin > hariIni) {
                            panahKiri.setImageDrawable(gambarTurun);
                        } else {
                            panahKiri.setImageDrawable(gambarNaik);
                        }

                        if (hariIni > besok) {
                            panahKanan.setImageDrawable(gambarTurun);
                        } else {
                            panahKanan.setImageDrawable(gambarNaik);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"kegagalan mendapatkan occupancy  / "+e,Toast.LENGTH_LONG).show();
                    }

                /*
                GELAS BERAIR
                ============
                - room selles
                - everage
                 */

                    // selles
                    JsonObject romSl = house.get("roomSales").getAsJsonObject();
                    BerapaYa berapaYa = new BerapaYa(romSl);
                    rsYesterday.setText(berapaYa.kemarin());
                    rsToday.setText(berapaYa.sekarang());
                    rsPer.setText(berapaYa.berapaPersen(1));
                    rsWav.setProgressValue(Integer.parseInt(berapaYa.berapaPersen(2)));
                    emj1.setText(berapaYa.txEmoj());

                    // everage
                    JsonObject romRat = house.get("roomRate").getAsJsonObject();
                    BerapaYa berapaYa1 = new BerapaYa(romRat);
                    everYes.setText(berapaYa1.kemarin());
                    everTod.setText(berapaYa1.sekarang());
                    everPer.setText(berapaYa1.berapaPersen(1));
                    everWave.setProgressValue(Integer.parseInt(berapaYa1.berapaPersen(2)));
                    emj2.setText(berapaYa1.txEmoj());

                    // revPar
                    JsonObject rev = house.get("revenue").getAsJsonObject();
                    BerapaYa berapaYa2 = new BerapaYa(rev);
                    revYes.setText(berapaYa2.kemarin());
                    revTod.setText(berapaYa2.sekarang());
                    revPer.setText(berapaYa2.berapaPersen(1));
                    revWave.setProgressValue(Integer.parseInt(berapaYa2.berapaPersen(2)));
                    emj3.setText(berapaYa2.txEmoj());

                    // length of stay
                    JsonObject lengSty = house.get("lenghtOfStay").getAsJsonObject();
                    BerapaYa berapaYa4 = new BerapaYa(lengSty);
                    lengYes.setText(berapaYa4.kemarin() + " Night");
                    lengTod.setText(berapaYa4.sekarang() + " Night");
                    lengPer.setText(berapaYa4.berapaPersen(1));
                    lengWave.setProgressValue(Integer.parseInt(berapaYa4.berapaPersen(2)));
                    emj4.setText(berapaYa4.txEmoj());

                    //unsold
                    JsonObject unsold = house.get("roomUnSold").getAsJsonObject();
                    BerapaYa berapaYa3 = new BerapaYa(unsold);
                    unYes.setText(berapaYa3.kemarin() + " Room");
                    unTod.setText(berapaYa3.sekarang() + " Room");
                    unPer.setText(berapaYa3.berapaPersen(1));
                    unWave.setProgressValue(Integer.parseInt(berapaYa3.berapaPersen(2)));

                    if (berapaYa3.txEmoj().equals("\uD83D\uDE0D")) {
                        emj5.setText("\uD83E\uDD14");
                    } else {
                        emj5.setText("\uD83D\uDE0D");
                    }

                    //guest
                    JsonObject ges = house.get("guest").getAsJsonObject();
                    float yesA = Float.parseFloat(ges.get("yesterdayAdult").getAsString());
                    float yesC = Float.parseFloat(ges.get("yesterdayChild").getAsString());
                    float todA = Float.parseFloat(ges.get("todayAdult").getAsString());
                    float todC = Float.parseFloat(ges.get("todayChild").getAsString());

                    float gA = yesA + yesC;
                    float gB = todA + todC;
                    float gC = (gA / gB) * 100;
                    int gPer = 0;
                    String gesPerNya = "";

                    if (gA < gB) {
                        gPer = (int) (100 - gC);
                        gesPerNya = "Increase " + gPer + " %";
                        emj6.setText("\uD83D\uDE0D");
                    } else if (gA > gB) {
                        gPer = (int) (gC - 100);
                        gesPerNya = "Decrease " + gPer + " %";
                        emj6.setText("\uD83E\uDD14");
                    } else if (gA == gB) {
                        gPer = 0;
                        gesPerNya = "Unchanged 0 %";
                        emj6.setText("\uD83D\uDE11");
                    }

                    int gKemA = (int) yesA;
                    int gKemC = (int) yesC;
                    int gTodA = (int) todA;
                    int gTodC = (int) todC;
                    gesTod.setText(gTodA + " Adult " + gTodC + " Child");
                    gesYes.setText(gKemA + " Adult " + gKemC + " Child");
                    gesPer.setText(gesPerNya);
                    gesWave.setProgressValue(gPer);


                    //reservationSummary
                    JsonObject reservationSummary = house.get("reservationSummary").getAsJsonObject();
                    String txBooking = reservationSummary.get("Booking").getAsString();
                    String txConfirmed = reservationSummary.get("Confirmed").getAsString();
                    String txCanceled = reservationSummary.get("Canceled").getAsString();
                    String txNoShow = reservationSummary.get("No Show").getAsString();

                    booking.setText(txBooking);
                    confirmed.setText(txConfirmed);
                    cancel.setText(txCanceled);
                    noshow.setText(txNoShow);


                 /*
                INHOUSE SUMMARY : inHouseSummary
                ==============
                - houseUsed : House Used
                - compliment : Compliment
                - payRoom : Pay Room
                - outOfOrder : Out Of Order
                 */
                    JsonObject inHouseSummary = house.get("inHouseSummary").getAsJsonObject();
                    houseUsed.setText(inHouseSummary.get("House Used").getAsString());
                    compliment.setText(inHouseSummary.get("Compliment").getAsString());
                    payRoom.setText(inHouseSummary.get("Pay Room").getAsString());
                    outOfOrder.setText(inHouseSummary.get("Out Of Order").getAsString());


                    // roomProduction
                    JsonArray terimaRoom = house.get("roomProduction").getAsJsonArray();
                    //List<Map<String,Object>> hayo = new Gson().fromJson(roomProduction.toString(), HashMap.class);
                    roomListNya = new ArrayList<>();
                    for (JsonElement tr : terimaRoom) {
                        Map<String, Object> apaRoom = new Gson().fromJson(tr.toString(), HashMap.class);
                        Map<String, Object> objectRoom = new HashMap<>();
                        for (Map.Entry<String, Object> ent : apaRoom.entrySet()) {
                            objectRoom.put(ent.getKey(), ent.getValue());
                        }
                        roomListNya.add(objectRoom);
                    }

                    RecyclerView romProduction = findViewById(R.id.room_production);
                    Adapter_Room_Production adapterRoomProduction = new Adapter_Room_Production(Activity_Probus.this, roomListNya);
                    romProduction.setLayoutManager(new LinearLayoutManager(Activity_Probus.this));
                    romProduction.setAdapter(adapterRoomProduction);

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });



        /*
        MENDAPATKAN OCCUPANCY
        ====================
         */

        try {
            Retrofit occRetro = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(URLNYA)
                    .build();
            PemanggilApi apiOccupancy = occRetro.create(PemanggilApi.class);
            Call<List<JsonObject>> panggilOcc = apiOccupancy.forecastOcc();
            panggilOcc.enqueue(new Callback<List<JsonObject>>() {
                @Override
                public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                    List<JsonObject> wadahOcc = response.body();
                    listOcc = new ArrayList<>();

                 /*
                CHART VIEW
                ==========
                 */

                    for (JsonObject opc : wadahOcc) {
                        Map<String, Object> terima1 = new Gson().fromJson(opc, HashMap.class);
                        Map<String, Object> terima2 = new HashMap<>();
                        for (Map.Entry<String, Object> et : terima1.entrySet()) {
                            terima2.put(et.getKey(), et.getValue());
                        }
                        listOcc.add(terima2);
                        // yAxist.add(new PointValue(10,terima2.get("occupancy")))
                    }


                    Adapter_malik_chart malik_chart = new Adapter_malik_chart(Activity_Probus.this, listOcc);
                    RecyclerView chartContainer = findViewById(R.id.chartContainer);
                    chartContainer.setLayoutManager(new LinearLayoutManager(Activity_Probus.this, LinearLayout.HORIZONTAL, false));
                    chartContainer.setAdapter(malik_chart);


                    // berhenti loading
                    probusLoading.setVisibility(View.GONE);


                }

                @Override
                public void onFailure(Call<List<JsonObject>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "there are internet issue or problem with API , occupation : call backend divisi" + e, Toast.LENGTH_LONG).show();
        }

    }

    /*
    MENDAPATKAN BERAPA PERCENNYA
    ===========================
    - dari nilai yesterday
    - dari nilai today
     */

    public class BerapaYa {
        private JsonObject jsonObject;
        private NumberFormat formatter;
        private String kem;
        private String sek;
        private int nil;
        String emojNy = "";

        BerapaYa(JsonObject jsonObject1) {
            this.jsonObject = jsonObject1;
            this.formatter = new DecimalFormat("#,###");
            this.kem = jsonObject.get("yesterday").getAsString();
            this.sek = jsonObject.get("today").getAsString();
        }

        String kemarin() {
            return formatter.format(Double.parseDouble(kem));
        }

        String sekarang() {
            return formatter.format(Double.parseDouble(sek));
        }

        String berapaPersen(int apa) {
            int patokan = 100;
            String hasil = "";

            double a1 = Double.parseDouble(kem);
            float a2 = (float) a1;
            double b1 = Double.parseDouble(sek);
            float b2 = (float) b1;
            float per = (a2 / b2) * 100f;
            int c1 = (int) per;
            int tmp = 0;
            if (apa == 1) {
                if (a2 < b2) {
                    hasil = "Increase " + (patokan - c1) + "%";
                    nil = patokan - c1;
                    emojNy = "\uD83D\uDE0D";
                } else if (a2 > b2) {
                    hasil = "Decrease " + (c1 - patokan) + "%";
                    nil = c1 - patokan;
                    emojNy = "\uD83E\uDD14";
                } else if (a2 == b2) {
                    hasil = "unchanged 0 %";
                    nil = 0;
                    emojNy = "\uD83D\uDE11";
                }
            } else if (apa == 2) {
                if (a2 < b2) {
                    tmp = patokan - c1;
                    if (tmp < 50) {
                        hasil = String.valueOf(tmp + 20);
                    } else if (tmp > 50 && tmp < 90) {
                        hasil = String.valueOf(tmp + 10);
                    } else if (tmp > 95) {
                        hasil = "95";
                    }

                } else if (a2 > b2) {
                    hasil = "10";
                } else if (a2 == b2) {
                    hasil = "10";
                }
            }

            return hasil;
        }

        String txEmoj() {
            double a1 = Double.parseDouble(kem);
            float a2 = (float) a1;
            double b1 = Double.parseDouble(sek);
            float b2 = (float) b1;
            float per = (a2 / b2) * 100f;
            int c1 = (int) per;
            int tmp = 0;
            if (a2 < b2) {
                emojNy = "\uD83D\uDE0D";
            } else if (a2 > b2) {
                emojNy = "\uD83E\uDD14";
            } else if (a2 == b2) {
                emojNy = "\uD83D\uDE11";
            }
            return emojNy;
        }

        int angkanya() {
            return nil;
        }
    }

    int acak() {
        Random r = new Random();
        return r.nextInt(5000 - 1000) + 1000;
    }


    /*
    SET EMOJI PASIF
    ===============
     */
    void setEmojiPasif() {
        emj1.setText("\uD83D\uDE36");
        emj2.setText("\uD83D\uDE36");
        emj3.setText("\uD83D\uDE36");
        emj4.setText("\uD83D\uDE36");
        emj5.setText("\uD83D\uDE36");
        emj6.setText("\uD83D\uDE36");

    }

    /*
    SET OMBAK FULL
    =============
     */
    void setOmbakFull() {
        everWave.setProgressValue(100);
        rsWav.setProgressValue(100);
        gesWave.setProgressValue(100);
        lengWave.setProgressValue(100);
        unWave.setProgressValue(100);
        revWave.setProgressValue(100);
    }

    void setTodNoll() {
        unTod.setText("0000");
        lengTod.setText("0000");
        revTod.setText("0000");
        everTod.setText("0000");
        rsToday.setText("0000");
        gesTod.setText("0000");
    }


    /*
    DISTRIBUTION VOID
    ============
     */
    public void distribution() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLNYA_EZEE)
                .build();
        PemanggilApi pemanggilApi = retrofit.create(PemanggilApi.class);
        String requestBodyText = "<RES_Request>\n" +
                "    <Request_Type>RoomInfo</Request_Type>\n" +
                "    <Authentication>\n" +
                "        <HotelCode>15154</HotelCode>\n" +
                "        <AuthCode>72883744979becd47d-9414-11e9-a</AuthCode>\n" +
                "    </Authentication>\n" +
                "</RES_Request>";
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml"), requestBodyText);
        Call<ResponseBody> panggillXml = pemanggilApi.ezeeResponse(requestBody);
        panggillXml.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    XmlToJson xmlToJson = new XmlToJson.Builder(response.body().string()).build();
                    //Log.i(TAG, "onResponse: "+xmlToJson);
                    //JSONObject object = xmlToJson.toJson().getJSONObject("RES_Response").getJSONObject("RoomInfo").getJSONObject("RatePlans").getJSONObject("RatePlan");


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t);
            }
        });

    }

    /*
    TAB LAYOUT CLICK
    - menagani perubahan tab layout dilaporan
     */

    int mauKeluar = 0;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (probusDrawer.isDrawerOpen(Gravity.RIGHT)) {
            probusDrawer.closeDrawers();
        } else if (probusMainContainer.getDisplayedChild() != HOME) {
            probusMainContainer.setDisplayedChild(HOME);
        } else if (mauKeluar == 0) {
            Toast.makeText(getApplicationContext(), "tekan sekali lagi untuk keluar", Toast.LENGTH_LONG).show();
            mauKeluar += 1;
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    mauKeluar = 0;
                }
            };
        } else {
            mauKeluar = 0;
            super.onBackPressed();

        }

        laporanTab.selectTab(tabToday);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
