package dev.malikkurosaki.probuspresto;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayoutFragment1 extends Fragment {

    private Context context;
    private Activity activity;
    private View view;

    private Toolbar toolbar;
    private RecyclerView recyclerHome;

    private LinearLayout blurContainer;
    private ImageView gambarNya;

    private DatabaseReference db;


    public LayoutFragment1 newInstance(){
        return new LayoutFragment1();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment1,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.context = view.getContext();
        this.activity = (Activity)context;


        db = FirebaseDatabase.getInstance().getReference();

        toolbar = view.findViewById(R.id.toolbar);
        recyclerHome = view.findViewById(R.id.recyclerHome);
        blurContainer = view.findViewById(R.id.blurContainer);
        gambarNya = view.findViewById(R.id.gambarNya);


        List<Map<String,Object>> list = new ArrayList<>();

        Map<String,Object> map = new HashMap<>();
        map.put("nama","balado");

        list.add(map);

        AdapterRecyclerView1 adapterRecyclerView1 = new AdapterRecyclerView1(context,list);
        recyclerHome.setLayoutManager(new LinearLayoutManager(context));
        recyclerHome.setAdapter(adapterRecyclerView1);

        ((MainActivity)activity).setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_menu));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity)activity).drawerLayout.openDrawer(Gravity.START);
            }
        });
    }


}
