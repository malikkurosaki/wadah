package dev.malikkurosaki.probussystem;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LayoutNewsFeed extends Fragment {

    private View view;
    private RecyclerView feedContainer;
    private Context context;
    private Activity activity;

    private DatabaseReference db;
    private StorageReference st;
    private List<Map<String, Objects>> listData;
    private boolean sudahSuka = false;

    public static LayoutNewsFeed newInstance(){
        return new LayoutNewsFeed();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_news_feed,container,false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        this.context = view.getContext();
        this.activity = (Activity)context;

        db = FirebaseDatabase.getInstance().getReference();
        st = FirebaseStorage.getInstance().getReference();

        db.child("menubawah").child("newsfeed").child("kontent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listData = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String,Objects> ambil = (HashMap<String,Objects>)snapshot.getValue();
                    Map<String,Objects> terima = new HashMap<>();
                    for (Map.Entry<String,Objects> ent : ambil.entrySet()){
                        terima.put(ent.getKey(),ent.getValue());
                    }
                    listData.add(terima);
                }

                final RecyclerviewNewsFeedAdapter  newsFeedAdapter = new RecyclerviewNewsFeedAdapter(context,listData,st);
                final RecyclerView feedContainer = view.findViewById(R.id.feedContainer);
                LinearLayoutManager manager = new LinearLayoutManager(context);
                feedContainer.setLayoutManager(manager);
                manager.setReverseLayout(true);
                manager.setStackFromEnd(true);
                feedContainer.setAdapter(newsFeedAdapter);


                newsFeedAdapter.setKetikaSukaDiKlick(new RecyclerviewNewsFeedAdapter.KetikaSukaDiKlick() {
                    @Override
                    public void maka(View view, final int position) {
                        Map<String,Objects> ddt = listData.get(position);
                        if (view.getId() == R.id.newsSuka){
                            //view.setBackgroundResource(R.drawable.icon_suka2);
                            int sukanya = Integer.parseInt(String.valueOf(ddt.get("suka")));

                            int finalnya = 0;
                            String keterangannya = "";

                            if (!sudahSuka){
                                sukanya++;
                                keterangannya = "disukai";
                                sudahSuka = true;
                            }else {
                                sukanya--;
                                keterangannya = "";
                                sudahSuka = false;
                            }

                            if (sukanya < 0){
                                sukanya = 0;
                            }
                            final String finalKeterangannya = keterangannya;
                            final String finalKeterangannya1 = keterangannya;
                            db.child("menubawah").child("newsfeed").child("kontent").child(String.valueOf(ddt.get("key"))).child("suka").setValue(sukanya).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (finalKeterangannya1.equals("")){
                                        Toast.makeText(getContext(), finalKeterangannya,Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        }

                        if (view.getId() == R.id.tandaMore){
                            //Toast.makeText(getContext(),"more",Toast.LENGTH_LONG).show();
                            TextView textNya = view.findViewById(R.id.tandaMore);
                            textNya.setText(String.valueOf(ddt.get("content")));

                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
