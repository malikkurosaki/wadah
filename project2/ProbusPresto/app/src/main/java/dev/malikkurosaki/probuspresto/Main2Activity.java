package dev.malikkurosaki.probuspresto;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private Toolbar toolbar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar2 = findViewById(R.id.toolbar2);


        setActionBar(toolbar2);
        toolbar2.setNavigationIcon(getResources().getDrawable(R.drawable.icon_kembali));
        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        Map<String,Object> terima = (HashMap<String,Object>)intent.getSerializableExtra("intent");

        String datanaya = String.valueOf(terima.get("halaman"));
        switch (datanaya){
            case "profile":
                ngeFragment(new LayoutProfile());
                break;
            case "login":
                ngeFragment(new LayoutLogin());
                break;
            case "admin":
                ngeFragment(new LayoutInput1());
                break;
        }
    }


    private void ngeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragCon2,fragment).commitAllowingStateLoss();
    }

}
