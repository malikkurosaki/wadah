package com.malikkurosaki.demobotassistant;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private ImageView operator1;
    private ImageView operator2;
    private EditText input1;
    private ImageButton search1;
    private ProgressBar progress;

    private TextView nama,alamat,asal,tinggi,umur,terdaftar,infonya;

    private DatabaseReference reference;
    private DroidSpeech droidSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operator1 = findViewById(R.id.operator1);
        operator2 = findViewById(R.id.operator2);
        input1 = findViewById(R.id.input);
        search1 = findViewById(R.id.search);
        progress = findViewById(R.id.progress);
        infonya = findViewById(R.id.operatorInfo);

        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);
        asal = findViewById(R.id.asal);
        tinggi = findViewById(R.id.tinggi);
        umur = findViewById(R.id.umur);
        terdaftar = findViewById(R.id.terdaftar);

        reference = FirebaseDatabase.getInstance().getReference();
        droidSpeech = new DroidSpeech(this, null);

        operator2.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},500);
            return;
        }

        reference.child("pelanggan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> ddt = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    terdaftar.append(snapshot.getKey()+",");

                    /*Map<String,Object> ambilNama = (HashMap<String, Object>)snapshot.getValue();
                    Map<String,Object> akhirnya = new HashMap<>();
                    for (Map.Entry<String,Object> nm : ambilNama.entrySet()){
                        akhirnya.put(nm.getKey(),nm.getValue());
                        ddt.add(String.valueOf(akhirnya.get("nama")));
                    }

                    if (!ddt.isEmpty()){
                        terdaftar.setText(String.valueOf(ddt));

                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       search1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String kunciNama = input1.getText().toString().trim().toLowerCase();
               if (TextUtils.isEmpty(kunciNama)){
                   Toast.makeText(getApplicationContext(),"kata kunci tidak boleh kosong",Toast.LENGTH_LONG).show();
                   return;
               }
               aktifitas(kunciNama);
               InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
               inputMethodManager.hideSoftInputFromWindow(search1.getWindowToken(), 0);
           }
       });

       operator1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               droidSpeech.setShowRecognitionProgressView(true);
               droidSpeech.startDroidSpeechRecognition();
               operator1.setVisibility(View.GONE);
               operator2.setVisibility(View.VISIBLE);
               infonya.setText("silahkan berbicara");
           }
       });

       droidSpeech.setOnDroidSpeechListener(new OnDSListener() {
           @Override
           public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
               droidSpeech.setPreferredLanguage("id-ID");
           }

           @Override
           public void onDroidSpeechRmsChanged(float rmsChangedValue) {

           }

           @Override
           public void onDroidSpeechLiveResult(String liveSpeechResult) {

           }

           @Override
           public void onDroidSpeechFinalResult(String finalSpeechResult) {
               input1.setText(finalSpeechResult);
               aktifitas(finalSpeechResult);
               droidSpeech.closeDroidSpeechOperations();
               infonya.setText("click saya lagi untuk mulai berbicara");
               operator1.setVisibility(View.VISIBLE);
               operator2.setVisibility(View.GONE);

           }

           @Override
           public void onDroidSpeechClosedByUser() {

           }

           @Override
           public void onDroidSpeechError(String errorMsg) {

           }
       });

       operator2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               droidSpeech.closeDroidSpeechOperations();
               operator2.setVisibility(View.GONE);
               operator1.setVisibility(View.VISIBLE);
           }
       });

        //startActivity(new Intent(MainActivity.this,Main2Activity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 500){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"ijin telah diberikan silahkan lanjutkan",Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
            }
        }
    }



    void aktifitas(final String kunci){
        reference.child("pelanggan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(kunci.toLowerCase())){
                    Toast.makeText(getApplicationContext(),"nama tidak terdaftar",Toast.LENGTH_LONG).show();
                    return;
                }

                progress.setVisibility(View.VISIBLE);
                Map<String,Object> data2 = new HashMap<>();
                Map<String,Object> data = (HashMap<String, Object>)dataSnapshot.child(kunci.toLowerCase()).getValue();

                for (Map.Entry<String,Object> en: data.entrySet()){
                    data2.put(en.getKey(),en.getValue());
                }

                if (!data2.isEmpty()){
                    progress.setVisibility(View.GONE);
                    input1.setText("");
                    nama.setText(String.valueOf(data2.get("nama")));
                    alamat.setText(String.valueOf(data2.get("alamat")));
                    asal.setText(String.valueOf(data2.get("asal")));
                    tinggi.setText(String.valueOf(data2.get("tinggi")));
                    umur.setText(String.valueOf(data2.get("umur")));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {

        super.onPause();
    }
}
