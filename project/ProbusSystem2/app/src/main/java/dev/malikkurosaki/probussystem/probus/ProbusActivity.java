package dev.malikkurosaki.probussystem.probus;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.appcompat.app.AppCompatActivity;
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
import com.rockerhieu.emojicon.EmojiconTextView;
import com.white.progressview.CircleProgressView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.malikkurosaki.probussystem.ClassDashboard;
import dev.malikkurosaki.probussystem.JsonPlaceHolderApi;
import dev.malikkurosaki.probussystem.R;
import dev.malikkurosaki.probussystem.RoomProductionAdapter;
import me.itangqi.waveloadingview.WaveLoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProbusActivity extends AppCompatActivity {

    /*
    MAIN LAYOUT
    ==========
     */
    @BindView(R.id.probusBottomNav)
    BottomNavigationView probusBottomNav;
    @BindView(R.id.probusDrawer)
    DrawerLayout probusDrawer;
    @BindView(R.id.probusSideMenu)
    NavigationView probusSideMenu;

    @BindView(R.id.probus_main_container)
    ViewAnimator probusMainContainer;

    private static final String UBUD_MALANG = "https://ubudmalang.probussystem.com:4458";
    private static final String PURIMAS = "https://purimas.probussystem.com:4450";

    private String TAG = "-->";


    @BindView(R.id.dashBSwipe)
    SwipeRefreshLayout dashBSwipe;

      /*
    DEKLARASI DASHBOARD DAN KELENGKAPANNYA
    ======================================
    - tanggal
    - lainnya
     */

    // EMOJI
    @BindView(R.id.emj1)
    EmojiconTextView emj1;
    @BindView(R.id.emj2)
    EmojiconTextView emj2;
    @BindView(R.id.emj3)
    EmojiconTextView emj3;
    @BindView(R.id.emj4)
    EmojiconTextView emj4;
    @BindView(R.id.emj5)
    EmojiconTextView emj5;
    @BindView(R.id.emj6)
    EmojiconTextView emj6;

    // progress view
    @BindView(R.id.cir_prog)
    CircleProgressView cirProg;
    @BindView(R.id.circle_progress_normal2)
    CircleProgressView cirProg2;
    @BindView(R.id.circle_progress_normal3)
    CircleProgressView cirProg3;

    // circle
    @BindView(R.id.dashboard_tanggal)
    TextView dashboardTanggal;
    @BindView(R.id.arr)
    TextView arr;
    @BindView(R.id.inH)
    TextView inH;
    @BindView(R.id.dep)
    TextView dep;
    @BindView(R.id.bok)
    TextView bok;

    // room selles
    @BindView(R.id.rs_yesterday)
    TextView rsYes;
    @BindView(R.id.rs_today)
    TextView rsTod;
    @BindView(R.id.rs_per)
    TextView rsPer;
    @BindView(R.id.rs_wav)
    WaveLoadingView rsWav;

    // everage
    @BindView(R.id.ever_yes)
    TextView everYes;
    @BindView(R.id.ever_tod)
    TextView everTod;
    @BindView(R.id.ever_per)
    TextView everPer;
    @BindView(R.id.ever_wave)
    WaveLoadingView everWave;

    // rev par
    @BindView(R.id.rev_yes)
    TextView revYes;
    @BindView(R.id.rev_tod)
    TextView revTod;
    @BindView(R.id.rev_per)
    TextView revPer;
    @BindView(R.id.rev_wave)
    WaveLoadingView revWave;

    // length of stay
    @BindView(R.id.leng_yes)
    TextView lengYes;
    @BindView(R.id.leng_tod)
    TextView lengTod;
    @BindView(R.id.leng_per)
    TextView lengPer;
    @BindView(R.id.leng_wave)
    WaveLoadingView lengWave;

    // length of stay
    @BindView(R.id.un_yes)
    TextView unYes;
    @BindView(R.id.un_tod)
    TextView unTod;
    @BindView(R.id.un_per)
    TextView unPer;
    @BindView(R.id.un_wave)
    WaveLoadingView unWave;

    // guest
    @BindView(R.id.ges_yes)
    TextView gesYes;
    @BindView(R.id.ges_tod)
    TextView gesTod;
    @BindView(R.id.ges_per)
    TextView gesPer;
    @BindView(R.id.ges_wave)
    WaveLoadingView gesWave;


    /*
    RESERVATION SUMMARY
    ==================
    - booking
    - confirmed
    - cansel
    - noshow
     */
    @BindView(R.id.booking)
    TextView booking;
    @BindView(R.id.confirmed)
    TextView confirmed;
    @BindView(R.id.cancel)
    TextView cansel;
    @BindView(R.id.noshow)
    TextView noshow;

    /*
    INHOUSE SUMMARY
    ==============
    - houseUsed
    - compliment
    - payRoom
    - outOfOrder
     */
    @BindView(R.id.house_used)
    TextView houseUsed;
    @BindView(R.id.compliment)
    TextView compliment;
    @BindView(R.id.pay_room)
    TextView payRoom;
    @BindView(R.id.out_of_order)
    TextView outOfOrder;

    /*
    roomProduction
    ==============
     */
    @BindView(R.id.roomProduction)
    RecyclerView conRoomProduction;
    private List<Map<String, Object>> roomListNya;
    private int nilai = 0;

    /*
    MENU SAMPING
    ============
     */
    @BindView(R.id.probusMenuSamping1)
    TextView probusMenu1;

    /*
    DIVINISI LAYOUTNYA
    - 0 : layout1 - HOME
    - 1 : lauout2 - LAYOUT2
    - 2 : layout3 - LAYOUT3
    - 3 : layout4 - LAYOUT4
    - 4 : layout lihat laporan - LAYOUT_LIHAT_LAPORAN
     */

    public static final int HOME = 0;
    public static final int LAYOUT2 = 1;
    public static final int LAYOUT3 = 2;
    public static final int LAYOUT_LIHAT_LAPORAN = 3;


    /*
    LAPORAN ATAS
    ===========
     */
    @BindView(R.id.laporanKembali)
    ImageView laporanKembali;
    @BindView(R.id.laporanTab)
    TabLayout laporanTab;
    @BindView(R.id.laporanRecycler)
    RecyclerView laporanRecycler;
    @BindView(R.id.laporanSwipe)
    SwipeRefreshLayout laporanSwipe;
    @BindView(R.id.judulLihatLaporan)
    TextView judulLihatLaporan;

    // listnya
    private List<Map<String,Object>> listArrivalToday;
    private List<Map<String,Object>> listInhouseToday;
    private List<Map<String,Object>> listDepatureToday;
    private List<Map<String,Object>> listBookingToday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.probus_activity);
        ButterKnife.bind(this);

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
                dashBSwipe.setRefreshing(false);
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

        probusMenu1.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "ya", Toast.LENGTH_LONG).show();
        });

        /*
        PENANGANA PANEL DICLICK
        ======================
         */
        arr.setOnClickListener(v -> {
            probusMainContainer.setDisplayedChild(LAYOUT_LIHAT_LAPORAN);
            judulLihatLaporan.setText("Arrival");
            laporanToday(listArrivalToday);
        });
        inH.setOnClickListener(v -> {
            laporanToday(listInhouseToday);
            probusMainContainer.setDisplayedChild(LAYOUT_LIHAT_LAPORAN);
            judulLihatLaporan.setText("InHouse");
        });
        dep.setOnClickListener(v -> {
            laporanToday(listDepatureToday);
            probusMainContainer.setDisplayedChild(LAYOUT_LIHAT_LAPORAN);
            judulLihatLaporan.setText("Depature");
        });
        bok.setOnClickListener(v -> {
            laporanToday(listBookingToday);
            probusMainContainer.setDisplayedChild(LAYOUT_LIHAT_LAPORAN);
            judulLihatLaporan.setText("Booking");
        });


         /*
         LAPORAN BAWAH
         =====================
          */
        laporanSwipe.setOnRefreshListener(() -> {
            Toast.makeText(getApplicationContext(),"updating ...",Toast.LENGTH_LONG).show();
            laporanSwipe.setRefreshing(false);
        });

        laporanKembali.setOnClickListener(v -> {
            probusMainContainer.setDisplayedChild(HOME);
        });
        laporanTab.addTab(laporanTab.newTab().setText("Today"));
        laporanTab.addTab(laporanTab.newTab().setText("Tomorrow"));
        laporanTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        //laporanToday();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "belum dikaitkan :-)", Toast.LENGTH_LONG).show();
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


    }


    /*
    PERBATASAN -------
    ==================
     */

    public void laporanToday(List<Map<String,Object>> targetNya){
        Probus_Laporan_Adapter laporan_adapter = new Probus_Laporan_Adapter(this,targetNya);
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

    public void dashboardNya() {
         /*
        DASHBOARD
        ========
        - pemanggilan class
        - recyclerview
         */
        ClassDashboard classDashboard = new ClassDashboard(this);
        dashboardTanggal.setText(classDashboard.kalendar());

        Retrofit dasbordRetro = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(PURIMAS)
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = dasbordRetro.create(JsonPlaceHolderApi.class);



        /*
        MENDAPATKAN ARRIVAL TODAY
        =========================
         */
        Call<List<JsonObject>> panggilArrival = jsonPlaceHolderApi.arrival();
        panggilArrival.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terima = response.body();
                arr.setText(String.valueOf(terima.size()));
                int ulang = terima.size() * 100;
                YoYo.with(Techniques.SlideInUp).duration(ulang).playOn(arr);
                listArrivalToday = new ArrayList<>();
                for (JsonElement dp : terima){
                    Map<String,Object> iniAr = new Gson().fromJson(dp.toString(),HashMap.class);
                    Map<String,Object> wadah = new HashMap<>();
                    for (Map.Entry<String,Object> ent : iniAr.entrySet()){
                        wadah.put(ent.getKey(),ent.getValue());
                    }
                    listArrivalToday.add(wadah);
                }
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });

        /*
        MENDAPATKAN INHOUSE TODAY
        =========================
         */
        Call<List<JsonObject>> panggilInhouse = jsonPlaceHolderApi.inHouse();
        panggilInhouse.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terima = response.body();
                inH.setText(String.valueOf(terima.size()));
                int ulang = terima.size() * 100;
                YoYo.with(Techniques.SlideInUp).duration(ulang).playOn(inH);
                listInhouseToday =  new ArrayList<>();
                for (JsonObject inHouseObject : terima){
                    Map<String,Object> inhs = new Gson().fromJson(inHouseObject.toString(),HashMap.class);
                    Map<String,Object> iinh = new HashMap<>();
                    for (Map.Entry<String,Object> enh : inhs.entrySet()){
                        iinh.put(enh.getKey(),enh.getValue());
                    }
                    listInhouseToday.add(iinh);
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
        Call<List<JsonObject>> panggilDepature = jsonPlaceHolderApi.depature();
        panggilDepature.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terima = response.body();
                dep.setText(String.valueOf(terima.size()));
                int ulang = terima.size() * 100;
                YoYo.with(Techniques.SlideInUp).duration(ulang).playOn(dep);
                listDepatureToday =  new ArrayList<>();
                for (JsonObject depObject : terima){
                    Map<String,Object> depTerima = new Gson().fromJson(depObject.toString(),HashMap.class);
                    Map<String,Object> depWadah = new HashMap<>();
                    for (Map.Entry<String,Object> enh : depTerima.entrySet()){
                        depWadah.put(enh.getKey(),enh.getValue());
                    }
                    listDepatureToday.add(depWadah);
                }
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });


        /*
        MENDAPATAKAN RESERVATION / BOOKING TODAY
        ==============================
         */
        Call<List<JsonObject>> panggilReservation = jsonPlaceHolderApi.reservation();
        panggilReservation.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terima = response.body();
                bok.setText(String.valueOf(terima.size()));
                int ulang = terima.size() * 100;
                YoYo.with(Techniques.SlideInUp).duration(ulang).playOn(bok);
                listBookingToday =  new ArrayList<>();
                for (JsonObject bokObject : terima){
                    Map<String,Object> bokTerima = new Gson().fromJson(bokObject.toString(),HashMap.class);
                    Map<String,Object> bokWadah = new HashMap<>();
                    for (Map.Entry<String,Object> enh : bokTerima.entrySet()){
                        bokWadah.put(enh.getKey(),enh.getValue());
                    }
                    listBookingToday.add(bokWadah);
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
        Call<JsonObject> panggilHome = jsonPlaceHolderApi.home();
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
                JsonObject occupancy = house.get("occupancy").getAsJsonObject();

                cirProg.setProgress(Integer.parseInt(occupancy.get("yesterday").getAsString()));
                cirProg.setProgressInTime(0, 2000);

                cirProg2.setProgress(Integer.parseInt(occupancy.get("today").getAsString()));
                cirProg2.setProgressInTime(0, 2000);

                cirProg3.setProgress(Integer.parseInt(occupancy.get("tomorrow").getAsString()));
                cirProg3.setProgressInTime(0, 2000);

                /*
                GELAS BERAIR
                ============
                - room selles
                - everage
                 */

                // selles
                JsonObject romSl = house.get("roomSales").getAsJsonObject();
                BerapaYa berapaYa = new BerapaYa(romSl);
                rsYes.setText(berapaYa.kemarin());
                rsTod.setText(berapaYa.sekarang());
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
                emj5.setText(berapaYa3.txEmoj());

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
                    emj6.setText("\uD83D\uDE01");
                } else if (gA > gB) {
                    gPer = (int) (gC - 100);
                    gesPerNya = "Decrease " + gPer + " %";
                    emj6.setText("\uD83D\uDE44");
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
                cansel.setText(txCanceled);
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

                RoomProductionAdapter roomProductionAdapter = new RoomProductionAdapter(ProbusActivity.this, roomListNya);
                conRoomProduction.setLayoutManager(new LinearLayoutManager(ProbusActivity.this));
                conRoomProduction.setAdapter(roomProductionAdapter);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


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
                    emojNy = "\uD83D\uDE01";
                } else if (a2 > b2) {
                    hasil = "Decrease " + (c1 - patokan) + "%";
                    nil = c1 - patokan;
                    emojNy = "\uD83D\uDE44";
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
        rsTod.setText("0000");
        gesTod.setText("0000");
    }

    /*
    TAB LAYOUT CLICK
    - menagani perubahan tab layout dilaporan
     */

    int mauKeluar = 0;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if ( probusDrawer.isDrawerOpen(Gravity.RIGHT)){
            probusDrawer.closeDrawers();
        }else if (probusMainContainer.getDisplayedChild() != HOME) {
            probusMainContainer.setDisplayedChild(HOME);
        }else if (mauKeluar == 0){
            Toast.makeText(getApplicationContext(),"tekan sekali lagi untuk keluar",Toast.LENGTH_LONG).show();
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
        }else {
            mauKeluar = 0;
            moveTaskToBack(true);

        }
    }
}
