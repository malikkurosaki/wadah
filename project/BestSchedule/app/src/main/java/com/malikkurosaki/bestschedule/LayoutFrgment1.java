package com.malikkurosaki.bestschedule;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LayoutFrgment1 extends Fragment {

    private RecyclerView recyclerView;
    private Context context;

    private DatabaseReference db;
    private List<Map<String,Objects>> listData;

    public static LayoutFrgment1 newInstance(){
        return new LayoutFrgment1();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment1,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = view.getContext();
        recyclerView = view.findViewById(R.id.wadah);

        db = FirebaseDatabase.getInstance().getReference();

        db.child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listData = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String, Objects> ambil = (HashMap<String, Objects>)ds.getValue();
                    Map<String,Objects> terima = new HashMap<>();
                    for (Map.Entry<String,Objects> ent : ambil.entrySet()){
                        terima.put(ent.getKey(),ent.getValue());
                    }
                    listData.add(terima);
                }

                AdapterRecyclerview1 recyclerview1 = new AdapterRecyclerview1(context,listData);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(recyclerview1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
