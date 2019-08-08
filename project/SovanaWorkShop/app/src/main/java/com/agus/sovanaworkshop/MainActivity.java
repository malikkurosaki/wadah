package com.agus.sovanaworkshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private NavigationView sideMenu;
    private ImageView klik;
    private LinearLayout pass;
    private int hitung = 0;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sideMenu = findViewById(R.id.sideMenu);
        pass = findViewById(R.id.pass);
        View view1 = sideMenu.getHeaderView(0);
        klik = view1.findViewById(R.id.klik);
        drawerLayout = findViewById(R.id.drawer);

        pass.setVisibility(View.GONE);

        klik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitung++;
                if (hitung>4){
                   pass.setVisibility(View.VISIBLE);
                   drawerLayout.closeDrawer(Gravity.START);
                   hitung = 0;

                }
            }
        });

        sideMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                Map<String,Object> halaman = new HashMap<>();
                switch (menuItem.getItemId()){
                    case R.id.menu1:
                        halaman.put("hl","menu1");
                        break;
                    case R.id.menu2:
                        halaman.put("hl","menu2");
                        break;
                    case R.id.menu3:
                        halaman.put("hl","menu3");
                        break;
                    case R.id.menu4:
                        halaman.put("hl","menu4");
                        break;
                }

                intent.putExtra("halaman", (Serializable) halaman);
                if (pass.getVisibility() == View.VISIBLE){
                    pass.setVisibility(View.GONE);
                }
                startActivity(intent);

                return false;
            }
        });


    }
}
