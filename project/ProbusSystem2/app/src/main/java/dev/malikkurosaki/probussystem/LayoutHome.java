package dev.malikkurosaki.probussystem;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
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

public class LayoutHome extends Fragment {

    private View view;
    private DatabaseReference db;
    private List<Map<String,Object>> slideList;
    private SlideShowHelperAdapter helperAdapter;
    private RecyclerView slideContainer;
    private int hitungSlide = 0;
    private int kecepantan = 2000;

    private static LayoutChatBot newInstance(){
        return new LayoutChatBot();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.db = FirebaseDatabase.getInstance().getReference();

        slideContainer = view.findViewById(R.id.slideContainer);


        db.child("aktifitas").child("slideshow").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                slideList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String,Object> ambil = (HashMap<String,Object>)ds.getValue();
                    Map<String,Object> wadah = new HashMap<>();
                    for (Map.Entry<String,Object> ent : ambil.entrySet()){
                        wadah.put(ent.getKey(),ent.getValue());

                    }
                    slideList.add(wadah);
                }

                helperAdapter = new SlideShowHelperAdapter(getContext(),slideList);
                slideContainer.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                slideContainer.setAdapter(helperAdapter);

                SnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(slideContainer);

                helperAdapter.setSlideDiklik(new SlideShowHelperAdapter.KetikaSlideDiklik() {
                    @Override
                    public void maka(View view, int position) {

                    }
                });

                hitungSlide = 0;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (hitungSlide >= helperAdapter.getItemCount()){
                            hitungSlide = 0;
                        }
                        slideContainer.smoothScrollToPosition(hitungSlide);
                        hitungSlide++;

                        handler.postDelayed(this,kecepantan);
                    }
                },kecepantan);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        hitungSlide = 0;
        kecepantan = 2000;
    }
}
