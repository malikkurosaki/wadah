package dev.malikkurosaki.probussystem;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;


public class Main3Activity extends AppCompatActivity {

    private Fragment layoutMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main3);

        Intent intent = getIntent();
        Map<String,Object> terima = (HashMap<String,Object>)intent.getSerializableExtra("menus");
        Bundle bundle = new Bundle();

        switch (String.valueOf(terima.get("menus"))){
            case "updatehalamanmenu":
                layoutMenu = new UpdateHalamanMenu();
                break;
            case "admin":
                layoutMenu = new LayoutAdminPanel();
                break;
            case "update":
                layoutMenu = new LayoutUpdate();
                break;
            case "newsfeed":
                layoutMenu = new LayoutNewsFeed();
                break;
                default:
                    layoutMenu = new TanggapanDariMenu();
                    bundle.putString("dari", String.valueOf(terima.get("menus")));
                    layoutMenu.setArguments(bundle);
        }

        if (terima.get("menus")!=null){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.menuLayoutContainer,layoutMenu);
            transaction.commitAllowingStateLoss();
        }else {
            Toast.makeText(getApplicationContext(),"main 3 data anda tidak bisa ditampilkan ",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Main3Activity.this,Main2Activity.class));
        super.onBackPressed();
    }
}
