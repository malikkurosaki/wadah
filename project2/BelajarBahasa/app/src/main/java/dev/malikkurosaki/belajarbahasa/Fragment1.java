package dev.malikkurosaki.belajarbahasa;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blongho.country_data.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment1 extends Fragment {

    private Context context;
    private Activity activity;
    private RecyclerView pilihBahasaContainer;

    private List<Map<String,Object>> listBendera;

    Fragment1 newInstance(){
        return new Fragment1();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment1,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.context = view.getContext();
        this.activity = (Activity)context;


        pilihBahasaContainer = view.findViewById(R.id.pilihBahasaContainer);

        World.init(context);


        String[] nama = {
                "america",
                "korea"
        };

        String[] bendera = {
                String.valueOf(World.getFlagOf("Us")),
                String.valueOf(World.getFlagOf("jp"))
        };
        listBendera = new ArrayList<>();
        Map<String,Object> benderanya = new HashMap<>();

        for (int r = 0;r< nama.length;r++){
            benderanya.put("nama",nama[r]);
            benderanya.put("bendera",bendera[r]);

        }
        listBendera.add(benderanya);

        RecyclerviewPilihBahasaAdapter bahasaAdapter = new RecyclerviewPilihBahasaAdapter(context,listBendera);
        pilihBahasaContainer.setLayoutManager(new LinearLayoutManager(context));
        pilihBahasaContainer.setAdapter(bahasaAdapter);
    }
}
