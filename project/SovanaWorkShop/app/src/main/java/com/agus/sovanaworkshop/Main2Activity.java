package com.agus.sovanaworkshop;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private FrameLayout container;
    Fragment fr = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        container = findViewById(R.id.container);
        Intent intent = getIntent();
        Map<String,Object> halaman = (HashMap<String, Object>)intent.getSerializableExtra("halaman");


        switch (String.valueOf(halaman.get("hl"))){
            case "menu1":
                fr = new DatabaseClass().newInstance();
                break;
            case "menu2":
                fr = new HargaClass().newInstance();
                break;
            case "menu3":
                fr = new PeralatanClass().newInstance();
                break;
            case "menu4":
                fr = new TimelineClass().newInstance();
                break;
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container,fr);
        transaction.commitAllowingStateLoss();

    }
}
