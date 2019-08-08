package com.malikkurosaki.bestschedule;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerContainer;
    private CalendarView calendarContainer;
    private Spinner tutorContainer;
    private RecyclerView jadwalContainer;
    private BottomNavigationView menuBawahContainer;
    private NavigationView menuSampingContainer;
    private Button lihatJadwal;
    private ProgressBar loading1;

    // kegiatan
    private String tahun,bulan,tanggal,tutor;
    private ArrayList<String> namaTutor;
    private List<Map<String,Objects>> listHasilJadwal;

    // firebase
    private DatabaseReference db;

    // dialog
    private EditText user,pass;
    private Button simpan;
    private BottomSheetDialog sheetDialog;
    private View adminView;
    private String masukUser,masukPass;
    private String tujuan;

    //token
    private String TOKEN;
    private String TAG = "-->";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().subscribeToTopic("macan");

        drawerContainer = findViewById(R.id.drawerContainer);
        calendarContainer = findViewById(R.id.calendarContainer);
        tutorContainer = findViewById(R.id.tutorContainer);
        jadwalContainer = findViewById(R.id.jadwalContainer);
        menuBawahContainer = findViewById(R.id.menuBawahContainer);
        menuSampingContainer = findViewById(R.id.menuSampingContainer);
        lihatJadwal = findViewById(R.id.lihatJadwal);
        loading1 = findViewById(R.id.loading1);

        loading1.setVisibility(View.GONE);

        db = FirebaseDatabase.getInstance().getReference();

        menuBawahContainer.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_bawah1:
                        drawerContainer.openDrawer(Gravity.START);
                        break;
                    case R.id.menu_bawah2:
                        Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.menu_bawah3:
                        Toast.makeText(getApplicationContext(),"next update",Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });

       db.child("validasi").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               masukUser = dataSnapshot.child("user").getValue(String.class);
               masukPass = dataSnapshot.child("pass").getValue(String.class);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

       db.child("tutor").child("daftartutor").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               namaTutor = new ArrayList<>();
               namaTutor.add("pilih tutor");
               for (DataSnapshot ds : dataSnapshot.getChildren()){
                   Map<String,Objects> ambil = (HashMap<String,Objects>)ds.getValue();
                   Map<String,Objects> terima = new HashMap<>();
                   for (Map.Entry<String,Objects> ent : ambil.entrySet()){
                       terima.put(ent.getKey(),ent.getValue());
                   }
                   namaTutor.add(String.valueOf(terima.get("nama")));
               }

               ArrayAdapter<String> namaTutorAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,namaTutor);
               namaTutorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
               tutorContainer.setAdapter(namaTutorAdapter);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

       calendarContainer.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
           @Override
           public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
               tahun = String.valueOf(year);
               bulan = String.valueOf(month+1);
               tanggal = String.valueOf(dayOfMonth);
           }
       });

       tutorContainer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               tutor = namaTutor.get(position);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });


       lihatJadwal.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (TextUtils.isEmpty(tahun) || tutor.equals("pilih tutor")){
                   Toast.makeText(getApplicationContext(),"pilih tanggal lalu pilih tutor terlebih dahulu",Toast.LENGTH_LONG).show();
                   return;
               }
                loading1.setVisibility(View.VISIBLE);
               db.child("kegiatan").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if (!dataSnapshot.hasChild(tahun)){
                           loading1.setVisibility(View.GONE);
                           Toast.makeText(getApplicationContext(),"maaf data yang anda minta belum terdaftar",Toast.LENGTH_LONG).show();
                           return;
                       }
                       if (!dataSnapshot.child(tahun).hasChild(bulan)){
                           loading1.setVisibility(View.GONE);
                           Toast.makeText(getApplicationContext(),"maaf data yang anda minta belum terdaftar",Toast.LENGTH_LONG).show();
                           return;
                       }
                       if (!dataSnapshot.child(tahun).child(bulan).child(tanggal).hasChild(tutor)){
                           loading1.setVisibility(View.GONE);
                           Toast.makeText(getApplicationContext(),"maaf data yang anda minta belum terdaftar",Toast.LENGTH_LONG).show();
                           return;
                       }
                       listHasilJadwal = new ArrayList<>();
                       for (DataSnapshot dds : dataSnapshot.child(tahun).child(bulan).child(tanggal).child(tutor).getChildren()){
                           Map<String,Objects> ambilJadwal = (HashMap<String, Objects>)dds.getValue();
                           Map<String,Objects> terimaJadwal = new HashMap<>();
                           for (Map.Entry<String,Objects> hasilJadwal : ambilJadwal.entrySet()){
                               terimaJadwal.put(hasilJadwal.getKey(),hasilJadwal.getValue());
                           }
                           listHasilJadwal.add(terimaJadwal);
                       }

                       loading1.setVisibility(View.GONE);
                       HasilJadwalAdapter adapter = new HasilJadwalAdapter(MainActivity.this,listHasilJadwal);
                       jadwalContainer.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                       jadwalContainer.setAdapter(adapter);

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"database error",Toast.LENGTH_LONG).show();
                        loading1.setVisibility(View.GONE);
                   }
               });
           }
       });

        sheetDialog = new BottomSheetDialog(MainActivity.this);
        adminView = View.inflate(MainActivity.this,R.layout.layout_masuk_admin,null);
        sheetDialog.setContentView(adminView);
        user = adminView.findViewById(R.id.user);
        pass = adminView.findViewById(R.id.pass);
        simpan = adminView.findViewById(R.id.simpan);

        menuSampingContainer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_samping1:
                        drawerContainer.closeDrawers();
                        sheetDialog.show();
                        masuk("updatejadwal");
                        break;
                    case R.id.menu_samping2:
                        drawerContainer.closeDrawers();
                        sheetDialog.show();
                        masuk("updatetutor");
                        break;
                    case R.id.menu_samping3:
                        drawerContainer.closeDrawers();
                        sheetDialog.show();
                        masuk("lihatfull");
                        break;
                    case R.id.menu_samping4:

                        break;
                }
                return false;
            }
        });
    }



    public void masuk(String tujuanNya){
        this.tujuan = tujuanNya;
        //Toast.makeText(getApplicationContext(),tujuan,Toast.LENGTH_LONG).show();
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String terimaUser = user.getText().toString().trim();
                String terimaPass = pass.getText().toString().trim();
                if (TextUtils.isEmpty(terimaPass) || TextUtils.isEmpty(terimaUser)){
                    Toast.makeText(getApplicationContext(),"coba coba tak banned akumnu !!",Toast.LENGTH_LONG).show();
                    return;
                }
                if (terimaUser.equals(masukUser) && terimaPass.equals(masukPass)){
                    sheetDialog.dismiss();
                    //Toast.makeText(getApplicationContext(),"hore",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                    Map<String,Object> paketNya = new HashMap<>();
                    paketNya.put("permintaan",tujuan);
                    intent.putExtra("kiriman", (Serializable) paketNya);
                    startActivity(intent);
                }else {
                    sheetDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"yeee password salah , coba bilang malik nguanteng dong "+masukPass+"-"+masukUser,Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}
