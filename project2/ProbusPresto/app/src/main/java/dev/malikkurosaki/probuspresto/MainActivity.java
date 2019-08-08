package dev.malikkurosaki.probuspresto;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    public DrawerLayout drawerLayout;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"),600);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragCon1,new LayoutFragment1()).commitAllowingStateLoss();

        navigationView = findViewById(R.id.navigasi);
        drawerLayout = findViewById(R.id.drawer);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null){
            ngeFragment("intent","halaman","login");
            finish();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.menu1:
                        if (user== null){
                            ngeFragment("intent","halaman","login");
                        }else {
                            Toast.makeText(getApplicationContext(),"wong uda login kok",Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.menu2:
                        ngeFragment("intent","halaman","profile");
                        break;
                    case R.id.menu3:

                        break;
                    case R.id.menu8:
                        ngeFragment("intent","halaman","admin");
                        break;
                    case R.id.menu9:
                        drawerLayout.closeDrawers();
                        if (user != null){
                            FirebaseAuth.getInstance().signOut();
                            startActivity(getIntent());
                        }else {
                            Toast.makeText(getApplicationContext(),"masuk aja belum",Toast.LENGTH_LONG).show();
                        }

                        break;
                }
                return false;
            }
        });


    }

    private void ngeFragment(String namaIntent,String key,String value){
        drawerLayout.closeDrawers();
        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
        Map<String,Object> item = new HashMap<>();
        item.put(key,value);
        intent.putExtra(namaIntent, (Serializable) item);
        startActivity(intent);

    }


}
