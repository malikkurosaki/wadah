package com.malikkurosaki.malikcarinomerwa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,Fragment1.OnbukaFragment1 {

    private Button malikInject;
    private Button malikSaring;
    private FrameLayout malikContainer;
    private EditText malikInfo;
    private int IJIN_CONTACT = 556;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        malikInject = findViewById(R.id.malikInject);
        malikSaring = findViewById(R.id.malikSaring);
        malikContainer = findViewById(R.id.malikContainer);
        malikInfo = findViewById(R.id.malikInfo);

        malikInject.setOnClickListener(this);
        malikSaring.setOnClickListener(this);

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS},IJIN_CONTACT);
            return;
        }


    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new Fragment1();
        switch (v.getId()){
            case R.id.malikInject:
                fragment = new Fragment1();
                break;
            case R.id.malikSaring:
                fragment = new Fragment2();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.malikContainer,fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IJIN_CONTACT){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
        }
    }

    @Override
    public void onbuka(String tx) {

    }
}
