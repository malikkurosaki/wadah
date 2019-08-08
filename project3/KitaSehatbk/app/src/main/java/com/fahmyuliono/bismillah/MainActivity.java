package com.fahmyuliono.bismillah;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private int ht = 0;
    private String TAG = "-->";
    private RecyclerView container1;

    private FirebaseUser user;
    private List<AuthUI.IdpConfig> configs;
    private int posNya;
    private String nomorHp;

    private String URLNYA = "https://kitasehat.herokuapp.com/";
    private BottomSheetBehavior bottomSheetBehavior;
    private NestedScrollView bottomS;
    
    private EditText julid;
    private ImageButton kirimJulid;

    private boolean tutup = false;
    private String posisinya;

    private ViewAnimator con1;
    private RecyclerView lay1;
    private List<Map<String,Object>> listContent;
    private Map<String,Object> dapatkan;

    //bagian kedua
    private TextView judul2,logo2,isi2,komen2;
    private EditText inputKomen;
    private ImageButton sendKomen;
    private ImageButton kembali;
    private BottomSheetBehavior kelakuanBawah;
    
    //masukkan nama
    private NestedScrollView namaContainer;
    private EditText inputNama;
    private Button simpanNamaNya;

    //parent
    private CoordinatorLayout parent;
    private int angka = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Map<String,String> pak = (HashMap<String,String>)intent.getSerializableExtra("paketan");

        final TinyDB simpanan = new TinyDB(MainActivity.this);
        Bundle bundle = getIntent().getExtras();


        
        FirebaseMessaging.getInstance().subscribeToTopic("berita");
        user = FirebaseAuth.getInstance().getCurrentUser();

        con1 = findViewById(R.id.con1);
        lay1 = findViewById(R.id.lay1);

        judul2 = findViewById(R.id.judul2);
        logo2 = findViewById(R.id.logo2);
        isi2 = findViewById(R.id.isi2);
        komen2 = findViewById(R.id.komen2);
        inputKomen = findViewById(R.id.input_komen);
        sendKomen = findViewById(R.id.send_komen);
        kembali = findViewById(R.id.kembali);

        namaContainer = findViewById(R.id.input_nama_container);
        inputNama = findViewById(R.id.input_nama);
        simpanNamaNya = findViewById(R.id.save_namanya);
        
        
        kelakuanBawah = BottomSheetBehavior.from(namaContainer);
        kelakuanBawah.setHideable(true);
        kelakuanBawah.setState(BottomSheetBehavior.STATE_HIDDEN);


        if (bundle != null){

            con1.setDisplayedChild(1);
            judul2.setText(String.valueOf(bundle.get("title")));
            isi2.setText(String.valueOf(bundle.get("body")));

            kembali.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    con1.setDisplayedChild(0);
                    angka = 0;
                }
            });
        }

        if (simpanan.getString("namauser").equals("")){
           kelakuanBawah.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

           simpanNamaNya.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String nam = inputNama.getText().toString().trim();
                   if (TextUtils.isEmpty(nam)){
                       Toast.makeText(getApplicationContext(),"jangan suka omong kosong",Toast.LENGTH_LONG).show();
                       return;
                   }

                   simpanan.putString("namauser",nam);
                   kelakuanBawah.setState(BottomSheetBehavior.STATE_HIDDEN);
                   Toast.makeText(getApplicationContext(),"nama disimpan",Toast.LENGTH_LONG).show();
               }
           });
        }



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URLNYA)
                .build();
        PemanggilApi pemanggilApi = retrofit.create(PemanggilApi.class);
        Call<List<JsonObject>> panggil = pemanggilApi.contennya();
        panggil.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> listNya = response.body();
                listContent = new ArrayList<>();
                for (JsonObject periksa : listNya){
                    Map<String,Object> wadah1 = new Gson().fromJson(periksa.toString(),HashMap.class);
                    Map<String,Object> wadah2 = new HashMap<>();
                    for (Map.Entry<String,Object> entry: wadah1.entrySet()){
                        wadah2.put(entry.getKey(),entry.getValue());
                    }
                    listContent.add(wadah2);

                }

                final MyRecyclerAdapter adapter = new MyRecyclerAdapter(MainActivity.this,listContent);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setSmoothScrollbarEnabled(true);
                linearLayoutManager.setStackFromEnd(true);
                lay1.setLayoutManager(linearLayoutManager);
                lay1.setAdapter(adapter);

                //con1.setDisplayedChild(0);

                adapter.setKetikaBeritaDiKlick(new MyRecyclerAdapter.KetikaBeritaDiKlick() {
                    @Override
                    public void maka(View view, final int posisi) {
                        dapatkan = adapter.getId(posisi);
                        con1.setDisplayedChild(1);

                        judul2.setText(String.valueOf(dapatkan.get("judul")));
                        isi2.setText(String.valueOf(dapatkan.get("isi")));

                        char lo = String.valueOf(dapatkan.get("judul")).charAt(0);
                        logo2.setText(String.valueOf(lo));

                        kembali.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                con1.setDisplayedChild(0);
                                komen2.setText("");
                                angka = 0;
                            }
                        });

                        Retrofit retrofit1 = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())
                                .baseUrl(URLNYA)
                                .build();
                        PemanggilApi pemanggilApi1 = retrofit1.create(PemanggilApi.class);
                        Call<List<JsonObject>> pemanggil1 = pemanggilApi1.komenanNya();
                        pemanggil1.enqueue(new Callback<List<JsonObject>>() {
                            @Override
                            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                                List<JsonObject> res = response.body();
                                StringBuilder builder = new StringBuilder();
                                for (JsonObject ter : res){
                                    if (ter.get("posisi").getAsString().equals(String.valueOf(posisi))){
                                        builder.append(ter.get("dari").getAsString()).append(" : ").append(ter.get("komen").getAsString()).append("\n");
                                        //Log.i(ter.get("posisi").getAsString()+" "+TAG, "onResponse: "+posisi);
                                    }
                                    Log.i(TAG, ter.get("posisi").getAsString()+" "+"onResponse: "+posisi);
                                }
                                komen2.setText(builder);
                            }

                            @Override
                            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

                            }
                        });

                        sendKomen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String isikomenannya = inputKomen.getText().toString().trim();
                                if (TextUtils.isEmpty(isikomenannya)){
                                    Toast.makeText(getApplicationContext(),"jangan suka omong kosong",Toast.LENGTH_LONG).show();
                                    return;
                                }

                                String darinya = simpanan.getString("namauser");

                                String idny = "\n"+darinya+" : "+isikomenannya;
                                komen2.append(idny);

                                Map<String,String> paketan = new HashMap<>();
                                paketan.put("posisi",String.valueOf(posisi));
                                paketan.put("dari",darinya);
                                paketan.put("komen",isikomenannya);

                                Retrofit retrofit2 = new Retrofit.Builder()
                                        .baseUrl(URLNYA)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
                                PemanggilApi pemanggilApi2 = retrofit2.create(PemanggilApi.class);
                                Call<List<JsonObject>> panggil = pemanggilApi2.koromKomenanNya(paketan);
                                panggil.enqueue(new Callback<List<JsonObject>>() {
                                    @Override
                                    public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                                        List<JsonObject> wad = response.body();
                                        for (JsonObject yu : wad){
                                            Toast.makeText(getApplicationContext(),yu.get("pesan").getAsString(),Toast.LENGTH_LONG).show();
                                            inputKomen.setText("");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<JsonObject>> call, Throwable t) {

                                    }
                                });
                            }
                        });
                    }
                });


            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });

        /*container1 = findViewById(R.id.conteiner1);
        bottomS = findViewById(R.id.bottomS);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomS);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        julid = findViewById(R.id.julid);
        kirimJulid = findViewById(R.id.kirimJulid);

        *//*configs = Collections.singletonList(new AuthUI.IdpConfig.PhoneBuilder().build());
        if (user == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(configs).build(),678);
        }else {
            nomorHp = user.getPhoneNumber();
        }*//*

        final TinyDB simpenan = new TinyDB(MainActivity.this);
        if (simpenan.getString("nama").equals("")){
            AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_masukan_nama,null);
            builder.setView(view);
            final Dialog  dialog = builder.create();

            dialog.show();
            dialog.setCanceledOnTouchOutside(false);


            final EditText nama = view.findViewById(R.id.nama);
            Button simpan = view.findViewById(R.id.simpan_nama);

            simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String namanya = nama.getText().toString().trim();
                    if (TextUtils.isEmpty(namanya)){
                        Toast.makeText(getApplicationContext(),"jangan isi kosong",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    simpenan.putString("nama",namanya);
                    dialog.dismiss();
                    recreate();
                }
            });

        }else {
            nomorHp = simpenan.getString("nama");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URLNYA)
                .build();

        PemanggilApi pemanggilApi = retrofit.create(PemanggilApi.class);
        Call<List<JsonObject>> terima = pemanggilApi.contennya();
        terima.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> wadah = response.body();
                List<Map<String,Object>> terima = new ArrayList<>();
                for (JsonObject pecahan1 : wadah){
                    Map<String,Object> maka = new Gson().fromJson(pecahan1.toString(),HashMap.class);
                    Map<String,Object> tr = new HashMap<>();
                    for (Map.Entry<String,Object> entry : maka.entrySet()){
                        tr.put(entry.getKey(),entry.getValue());
                    }

                    terima.add(tr);
                }

                MyRecyclerAdapter  myRecyclerAdapter = new MyRecyclerAdapter(MainActivity.this,terima);
                container1.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,true));
                container1.setAdapter(myRecyclerAdapter);
                container1.smoothScrollToPosition(terima.size()-1);

                container1.clearOnChildAttachStateChangeListeners();

                container1.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                });

                myRecyclerAdapter.setKetikaBeritaDiKlick(new MyRecyclerAdapter.KetikaBeritaDiKlick() {
                    @Override
                    public void maka(View view, final int posisi) {
                        posNya = posisi;
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                        julid.requestFocus();
                        julid.setFocusable(true);
                        julid.setSelected(true);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                    }
                });

            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });
        
        kirimJulid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isinya = julid.getText().toString();
                if (TextUtils.isEmpty(isinya)){
                    Toast.makeText(getApplicationContext(),"gk bisa kirim omong kosong",Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String,String> paketan = new HashMap<>();
                paketan.put("dari",nomorHp);
                paketan.put("komen",isinya);
                paketan.put("posisi",String.valueOf(posNya));

                Retrofit retrofit1Kirim = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(URLNYA)
                        .build();
                PemanggilApi apiKirim = retrofit1Kirim.create(PemanggilApi.class);
                Call<List<JsonObject>> pangil = apiKirim.koromKomenanNya(paketan);
                pangil.enqueue(new Callback<List<JsonObject>>() {
                    @Override
                    public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                        List<JsonObject> terima = response.body();
                        for (JsonObject jb : terima){
                            Toast.makeText(getApplicationContext(),jb.get("pesan").getAsString(),Toast.LENGTH_SHORT).show();
                            recreate();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<JsonObject>> call, Throwable t) {

                    }
                });
            }
        });
    }


    public void berkomentar(int pos){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.layout_berkomen,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        *//*String isinya = inputKom.getText().toString().trim();
        if (TextUtils.isEmpty(isinya)){
            Toast.makeText(getApplicationContext(),"jangan kebiasaan ngasi harapan kosong",Toast.LENGTH_SHORT).show();
            return;
        }
        String pesan = user.getPhoneNumber()+"  :  "+isinya+"\n";
        kom.append(pesan);

        Retrofit mobilRetro = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URLNYA)
                .build();
        PemanggilApi apiGede = mobilRetro.create(PemanggilApi.class);

        Map<String,String> paketan = new HashMap<>();
        paketan.put("komen",isinya);
        paketan.put("dari",nomorHp);
        paketan.put("posisi",String.valueOf(posNya));

        Call<List<JsonObject>> panggil = apiGede.koromKomenanNya(paketan);
        panggil.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> wadah = response.body();
                for (JsonObject apa : wadah){
                    String pesan = apa.get("pesan").getAsString();
                    Toast.makeText(getApplicationContext(),pesan,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });*//*
    }


    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }else {
            super.onBackPressed();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 678){
            if (resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(),"selamat datang"+user.getPhoneNumber(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        }*/

        parent = findViewById(R.id.parent);
    }



    @Override
    public void onBackPressed() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (!imm.hideSoftInputFromWindow(parent.getWindowToken(), 0)){
            Toast.makeText(getApplicationContext(),"tekan sekali lagi untuk keluar",Toast.LENGTH_LONG).show();
            angka++;
            if (angka > 1){
                super.onBackPressed();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        angka = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        angka = 0;
    }
}
