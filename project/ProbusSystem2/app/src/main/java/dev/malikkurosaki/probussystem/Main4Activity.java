package dev.malikkurosaki.probussystem;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

public class Main4Activity extends AppCompatActivity {

    private ViewPager pagerAdmin;
    private MenuItem menuItem;
    private BottomNavigationView adminMenuBawah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main4);



        pagerAdmin = findViewById(R.id.pagerAdmin);
        adminMenuBawah = findViewById(R.id.adminMenuBawah);

        MyFragmentViewPager fragmentViewPager =new MyFragmentViewPager(getSupportFragmentManager());
        fragmentViewPager.addFragment(new LayoutUpdate());
        fragmentViewPager.addFragment(new UpdateHalamanMenu());
        fragmentViewPager.addFragment(new UpdateNewsFeed());
        pagerAdmin.setAdapter(fragmentViewPager);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        adminMenuBawah.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.menuAdmin1:
                        pagerAdmin.setCurrentItem(0);
                        break;
                    case R.id.menuAdmin2:
                        pagerAdmin.setCurrentItem(1);
                        break;
                    case R.id.menuAdmin3:
                        pagerAdmin.setCurrentItem(2);
                        break;
                }

                return false;
            }
        });

        pagerAdmin.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(menuItem!=null){
                    menuItem.setChecked(false);
                }else {
                    adminMenuBawah.getMenu().getItem(0).setChecked(false);
                }
                adminMenuBawah.getMenu().getItem(i).setChecked(true);
                menuItem = adminMenuBawah.getMenu().getItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
