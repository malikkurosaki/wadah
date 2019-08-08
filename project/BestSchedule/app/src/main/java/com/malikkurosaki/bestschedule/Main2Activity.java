package com.malikkurosaki.bestschedule;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private FrameLayout adminContainer;
    private Fragment containernya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if (getIntent() == null){
            Toast.makeText(getApplicationContext(),"error mendapatkan aktifitas intent",Toast.LENGTH_LONG).show();
            return;
        }
        adminContainer = findViewById(R.id.adminContainer);


        Intent terimaKiriman = getIntent();
        Map<String,Object> paketanNya = (HashMap<String, Object>)terimaKiriman.getSerializableExtra("kiriman");
        String permintaannya = String.valueOf(paketanNya.get("permintaan"));
        switch (permintaannya){
            case "updatejadwal":
                containernya = new UpdateJadwalContainer();
                break;
            case "updatetutor":
                containernya = new LayoutTambahTutor();
                break;
            case "lihatfull":
                containernya = new LayoutFrgment1();
                break;

        }

        if (containernya != null){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.adminContainer,containernya);
            transaction.commitAllowingStateLoss();
        }

    }

}
