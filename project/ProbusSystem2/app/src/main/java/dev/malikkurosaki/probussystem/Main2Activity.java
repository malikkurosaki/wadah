package dev.malikkurosaki.probussystem;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.white.progressview.CircleProgressView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;
import me.itangqi.waveloadingview.WaveLoadingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity {


    private String TAG = "-->";
    private BottomNavigationView menuBottom;
    private DrawerLayout drawer;
    private NavigationView sideMenu;

    private Map<String,Object> pindah;
    private ImageView touchme;
    private CardView psdContainer;
    private EditText getPsd;

    private Button toAdmin;
    private Fragment isiFragment;
    private ViewPager wadahFragment;
    private MenuItem menuItem;

    private BottomSheetBehavior bottomSheetBehavior;
    private NestedScrollView dashboardBawah;

    // progress view
    private CircleProgressView cirProg,cirProg2,cirProg3;


    /*
    DEKLARASI DASHBOARD DAN KELENGKAPANNYA
    ======================================
    - tanggal
    - lainnya
     */
    private TextView dashboardTanggal;
    private TextView arr,inH,dep,bok;

    // room selles
    private TextView rsYes,rsTod,rsPer;
    private WaveLoadingView rsWav;

    // everage
    private TextView everYes,everTod,everPer;
    private WaveLoadingView everWave;

    // rev par
    private TextView revYes,revTod,revPer;
    private WaveLoadingView revWave;

    // length of stay
    private TextView lengYes,lengTod,lengPer;
    private WaveLoadingView lengWave;

    // length of stay
    private TextView unYes,unTod,unPer;
    private WaveLoadingView unWave;

    // guest
    private TextView gesYes,gesTod,gesPer;
    private WaveLoadingView gesWave;

    /*
    RESERVATION SUMMARY
    ==================
    - booking
    - confirmed
    - cansel
    - noshow
     */
    private TextView booking,confirmed,cansel,noshow;

    /*
    INHOUSE SUMMARY
    ==============
    - houseUsed
    - compliment
    - payRoom
    - outOfOrder
     */
    TextView houseUsed,compliment,payRoom,outOfOrder;

    /*
    roomProduction
    ==============
     */
    private RecyclerView conRoomProduction;

    /*
    BADGES
    ======
    - facebook fa
     */



    private int nilai = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main2);


        menuBottom = findViewById(R.id.menuBottom);
        drawer = findViewById(R.id.drawer);
        sideMenu = findViewById(R.id.sideMenu);
        touchme = findViewById(R.id.touchme);
        psdContainer = findViewById(R.id.psdContainer);
        getPsd = findViewById(R.id.getpsd);
        wadahFragment = findViewById(R.id.wadahFragment);
        dashboardBawah = findViewById(R.id.dashboard_bawah);
        conRoomProduction = findViewById(R.id.roomProduction);


        /*
        FACEBOOK FANCY
        =============
         */


        /*
        PROGRESS VIEW LINGKARANG
        =======================
        - animasi
        - lingkaran
        - dan lainnya

         */
        cirProg = findViewById(R.id.cir_prog);
        cirProg2 = findViewById(R.id.circle_progress_normal2);
        cirProg3 = findViewById(R.id.circle_progress_normal3);

        /*
        BAWAHNYA CIRCLE VIEW
        ====================
        - arr
        - inH
        - dep
        - bok
         */
        arr = findViewById(R.id.arr);
        inH = findViewById(R.id.inH);
        dep = findViewById(R.id.dep);
        bok = findViewById(R.id.bok);

        // selles
        rsYes = findViewById(R.id.rs_yesterday);
        rsTod = findViewById(R.id.rs_today);
        rsPer = findViewById(R.id.rs_per);
        rsWav = findViewById(R.id.rs_wav);

        // everage
        everYes = findViewById(R.id.ever_yes);
        everTod = findViewById(R.id.ever_tod);
        everPer = findViewById(R.id.ever_per);
        everWave = findViewById(R.id.ever_wave);

        // revPar
        revYes = findViewById(R.id.rev_yes);
        revTod = findViewById(R.id.rev_tod);
        revPer = findViewById(R.id.rev_per);
        revWave = findViewById(R.id.rev_wave);

        // length of stay
        lengYes = findViewById(R.id.leng_yes);
        lengTod = findViewById(R.id.leng_tod);
        lengPer = findViewById(R.id.leng_per);
        lengWave = findViewById(R.id.leng_wave);

        // unsold
        unYes = findViewById(R.id.un_yes);
        unTod = findViewById(R.id.un_tod);
        unPer = findViewById(R.id.un_per);
        unWave = findViewById(R.id.un_wave);

        // guest
        gesYes = findViewById(R.id.ges_yes);
        gesTod = findViewById(R.id.ges_tod);
        gesPer = findViewById(R.id.ges_per);
        gesWave = findViewById(R.id.ges_wave);


        // reservation sumary
        booking = findViewById(R.id.booking);
        confirmed = findViewById(R.id.confirmed);
        cansel = findViewById(R.id.cancel);
        noshow = findViewById(R.id.noshow);

         /*
        INHOUSE SUMMARY
        ==============
        - houseUsed
        - compliment
        - payRoom
        - outOfOrder
         */
         houseUsed = findViewById(R.id.house_used);
         compliment = findViewById(R.id.compliment);
         payRoom = findViewById(R.id.pay_room);
         outOfOrder = findViewById(R.id.out_of_order);



        /*
        BOTTOM BEHAVIOR
        ===============
        - menampilkan dasboard

         */
        bottomSheetBehavior = BottomSheetBehavior.from(dashboardBawah);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        dashboardNya();

        // menu on header
        View headerView = sideMenu.getHeaderView(0);
        toAdmin = headerView.findViewById(R.id.toAdmin);

        Paper.init(this);

        // perpindahan
        pindah = new HashMap<>();
        pindah.put("menus","menus");

        toAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pindah.clear();
                pindah.put("menus","admin");
                drawer.closeDrawers();
                Intent intent = new Intent(Main2Activity.this,Main4Activity.class);
                intent.putExtra("menus", (Serializable) pindah);
                startActivity(intent);
            }
        });

        psdContainer.setVisibility(View.GONE);

        MyFragmentViewPager fragmentViewPager = new MyFragmentViewPager(getSupportFragmentManager());
        fragmentViewPager.addFragment(new LayoutHome());
        fragmentViewPager.addFragment(new LayoutNewsFeed());
        fragmentViewPager.addFragment(new LayoutChatBot());
        fragmentViewPager.addFragment(new LayoutTranslate());
        wadahFragment.setAdapter(fragmentViewPager);

        menuBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menub1:
                        drawer.openDrawer(Gravity.LEFT);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                       dashboardNya();
                        break;
                    case R.id.menub2:
                        wadahFragment.setCurrentItem(0);
                        break;
                    case R.id.menub3:
                        wadahFragment.setCurrentItem(1);
                        break;
                    case R.id.menub4:
                        wadahFragment.setCurrentItem(2);
                        break;
                    case R.id.menub5:
                        wadahFragment.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });

        wadahFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (menuItem!=null){
                    menuItem.setChecked(false);
                }else {
                    menuBottom.getMenu().getItem(i+1).setChecked(false);
                }
                menuBottom.getMenu().getItem(i+1).setChecked(true);
                menuItem = menuBottom.getMenu().getItem(i+1);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });



        sideMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menus1:
                        pindah.clear();
                        pindah.put("menus","visi dan misi");
                        break;
                    case R.id.menus2:
                        pindah.clear();
                        pindah.put("menus","product");
                        break;
                    case R.id.menus3:
                        pindah.clear();
                        pindah.put("menus","services");
                        break;
                    case R.id.menus4:
                        pindah.clear();
                        pindah.put("menus","probus team");
                        break;
                    case R.id.menus5:
                        pindah.clear();
                        pindah.put("menus","price list");
                        break;
                    case R.id.menus6:
                        pindah.clear();
                        pindah.put("menus","video tutorial");
                        break;
                    case R.id.menus7:
                        pindah.clear();
                        pindah.put("menus","live support");
                        break;
                    case R.id.menus8:
                        pindah.clear();
                        pindah.put("menus","downloads");
                        break;
                    case R.id.menus9:
                        pindah.clear();
                        pindah.put("menus","galery");
                        break;
                    case R.id.menus10:
                        pindah.clear();
                        pindah.put("menus","our client");
                        break;
                    case R.id.menus11:
                        Toast.makeText(getApplicationContext(),"ya",Toast.LENGTH_LONG).show();
                        break;
                }

                drawer.closeDrawers();
                Intent intent = new Intent(Main2Activity.this,Main3Activity.class);
                intent.putExtra("menus", (Serializable) pindah);
                startActivity(intent);
                return false;

            }
        });

    }

    public void dashboardNya(){
         /*
        DASHBOARD
        ========
        - pemanggilan class
        - recyclerview
         */
        ClassDashboard classDashboard = new ClassDashboard(this);
        dashboardTanggal = findViewById(R.id.dashboard_tanggal);
        dashboardTanggal.setText(classDashboard.kalendar());

        Retrofit dasbordRetro = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://purimas.probussystem.com:4450")
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = dasbordRetro.create(JsonPlaceHolderApi.class);

        //arrival
        Call<List<JsonObject>> panggilArrival = jsonPlaceHolderApi.arrival();
        panggilArrival.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terima = response.body();
                arr.setText(String.valueOf(terima.size()));
                int ulang = terima.size()*100;
                YoYo.with(Techniques.SlideInUp).duration(ulang).playOn(arr);
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });

        // inhouse
        Call<List<JsonObject>> panggilInhouse = jsonPlaceHolderApi.inHouse();
        panggilInhouse.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terima = response.body();
                inH.setText(String.valueOf(terima.size()));
                int ulang = terima.size()*100;
                YoYo.with(Techniques.SlideInUp).duration(ulang).playOn(inH);
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });

        // depature
        Call<List<JsonObject>> panggilDepature = jsonPlaceHolderApi.depature();
        panggilDepature.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terima = response.body();
                dep.setText(String.valueOf(terima.size()));
                int ulang = terima.size()*100;
                YoYo.with(Techniques.SlideInUp).duration(ulang).playOn(dep);
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });


        // reservation
        Call<List<JsonObject>> panggilReservation = jsonPlaceHolderApi.reservation();
        panggilReservation.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> terima = response.body();
                bok.setText(String.valueOf(terima.size()));
                int ulang = terima.size()*100;
                YoYo.with(Techniques.SlideInUp).duration(ulang).playOn(bok);
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });

        //home
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
                cirProg.setProgressInTime(0,2000);

                cirProg2.setProgress(Integer.parseInt(occupancy.get("today").getAsString()));
                cirProg2.setProgressInTime(0,2000);

                cirProg3.setProgress(Integer.parseInt(occupancy.get("tomorrow").getAsString()));
                cirProg3.setProgressInTime(0,2000);

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

                // everage
                JsonObject romRat = house.get("roomRate").getAsJsonObject();
                BerapaYa berapaYa1 = new BerapaYa(romRat);
                everYes.setText(berapaYa1.kemarin());
                everTod.setText(berapaYa1.sekarang());
                everPer.setText(berapaYa1.berapaPersen(1));
                everWave.setProgressValue(Integer.parseInt(berapaYa1.berapaPersen(2)));

                // revPar
                JsonObject rev = house.get("revenue").getAsJsonObject();
                BerapaYa berapaYa2 = new BerapaYa(rev);
                revYes.setText(berapaYa2.kemarin());
                revTod.setText(berapaYa2.sekarang());
                revPer.setText(berapaYa2.berapaPersen(1));
                revWave.setProgressValue(Integer.parseInt(berapaYa2.berapaPersen(2)));

                // length of stay
                JsonObject lengSty = house.get("lenghtOfStay").getAsJsonObject();
                BerapaYa berapaYa4 = new BerapaYa(lengSty);
                lengYes.setText(berapaYa4.kemarin()+ " Night");
                lengTod.setText(berapaYa4.sekarang()+ " Night");
                lengPer.setText(berapaYa4.berapaPersen(1));
                lengWave.setProgressValue(Integer.parseInt(berapaYa4.berapaPersen(2)));


                //unsold
                JsonObject unsold = house.get("roomUnSold").getAsJsonObject();
                BerapaYa berapaYa3 = new BerapaYa(unsold);
                unYes.setText(berapaYa3.kemarin()+ " Room");
                unTod.setText(berapaYa3.sekarang()+ " Room");
                unPer.setText(berapaYa3.berapaPersen(1));
                unWave.setProgressValue(Integer.parseInt(berapaYa3.berapaPersen(2)));

                //guest
                JsonObject ges = house.get("guest").getAsJsonObject();
                float yesA = Float.parseFloat(ges.get("yesterdayAdult").getAsString());
                float yesC = Float.parseFloat(ges.get("yesterdayChild").getAsString());
                float todA = Float.parseFloat(ges.get("todayAdult").getAsString());
                float todC = Float.parseFloat(ges.get("todayChild").getAsString());

                float gA = yesA + yesC;
                float gB = todA + todC;
                float gC = (gA/gB)*100;
                int gPer = 0;
                String gesPerNya = "";

                if (gA < gB){
                    gPer = (int) (100 - gC);
                    gesPerNya = "Increase "+gPer+" %";
                }else if (gA > gB){
                    gPer = (int) (gC - 100);
                    gesPerNya = "Decrease "+gPer+" %";
                }else if (gA == gB){
                    gPer = 0;
                    gesPerNya = "Unchanged 0 %";
                }

                int gKemA = (int) yesA;
                int gKemC = (int) yesC;
                int gTodA = (int) todA;
                int gTodC = (int) todC;
                gesTod.setText(gTodA +" Aduld "+ gTodC +" Child");
                gesYes.setText(gKemA +" Aduld "+ gKemC +" Child");
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
                JsonObject roomProduction = house.get("roomProduction").getAsJsonObject();
                Map<String,Object> hayo = new Gson().fromJson(roomProduction.toString(),HashMap.class);
                    List<String> nama = new ArrayList<>();
                    List<Object> nilai = new ArrayList<>();
                    Map<String,Object> juju = new HashMap<>();
                    for (Map.Entry<String,Object> entry : hayo.entrySet()){
                      nama.add(entry.getKey());
                      nilai.add(entry.getValue());
                    }


                /*RoomProductionAdapter roomProductionAdapter = new RoomProductionAdapter(Main2Activity.this,nama,nilai);
                conRoomProduction.setLayoutManager(new LinearLayoutManager(Main2Activity.this));
                conRoomProduction.setAdapter(roomProductionAdapter);*/

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

    public class BerapaYa{

        private JsonObject jsonObject;
        private NumberFormat formatter;
        private String kem;
        private String sek;

        private int nil;

        BerapaYa(JsonObject jsonObject1){
            this.jsonObject = jsonObject1;
            this.formatter = new DecimalFormat("#,###");
            this.kem = jsonObject.get("yesterday").getAsString();
            this.sek = jsonObject.get("today").getAsString();
        }

        String kemarin(){
            return formatter.format(Double.parseDouble(kem));
        }

        String sekarang(){
            return formatter.format(Double.parseDouble(sek));
        }

        String berapaPersen(int apa){
            int patokan = 100;
            String hasil = "";
            double a1 = Double.parseDouble(kem);
            float a2 = (float) a1;
            double b1 = Double.parseDouble(sek);
            float b2 = (float) b1;
            float per = (a2/b2)*100f;
            int c1 = (int) per;
            int tmp = 0;
            if (apa == 1){
                if (a2 < b2){
                    hasil = "Increase "+ (patokan - c1) + "%";

                    nil = patokan - c1;
                }else if (a2 > b2){
                    hasil = "Decrease "+ (c1 - patokan) + "%";

                    nil = c1 - patokan;
                }else if (a2 == b2){
                    hasil = "unchanged 0 %";

                    nil = 0;
                }
            }else if (apa == 2){
                if (a2 < b2){
                    tmp = patokan - c1;
                    if (tmp < 50 ){
                        hasil = String.valueOf(tmp+20);
                    }else if (tmp > 50 && tmp < 90){
                        hasil = String.valueOf(tmp +10);
                    }else if (tmp > 95){
                        hasil = "95";
                    }

                }else if (a2 > b2){
                    hasil = "10";
                }else if (a2 == b2){
                    hasil = "10";
                }
            }

            return hasil;
        }



        int angkanya(){
            return nil;
        }
    }

    int acak (){
        Random r = new Random();
        return r.nextInt(5000 - 1000) + 1000;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }else {
            moveTaskToBack(true);
        }

    }


}
