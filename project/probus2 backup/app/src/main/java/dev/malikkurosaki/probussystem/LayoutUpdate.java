package dev.malikkurosaki.probussystem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayoutUpdate extends Fragment {

    public static LayoutUpdate newInstance(){
        return new LayoutUpdate();
    }

    private DatabaseReference db;
    private String TAG = "-->";

    // data perkenalan
    private EditText perkenalanText;
    private Button simpanPerkenalan;
    private String txKenalan;
    private boolean terkirim;
    private int idPerkenalan = 0;


    // slide
    private EditText slideText;
    private Button simpanSlide;
    private String txSlide;

    private View view;
    private int hitungYuk1 = 0;
    private int hitungYuk2 = 0;

    private Context context;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_update,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        this.context = view.getContext();
        this.activity = (Activity)context;

        // deklarasi view
        perkenalanText = view.findViewById(R.id.linkContainer);
        simpanPerkenalan = view.findViewById(R.id.simpanLinkContainer);
        slideText = view.findViewById(R.id.linkSlide);
        simpanSlide = view.findViewById(R.id.simpanSlide);

        // firebase
        db = FirebaseDatabase.getInstance().getReference();

        // dapatkan perkenalan
        db.child("aktifitas").child("perkenalanlock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txKenalan = (String)dataSnapshot.child("data").child("data").getValue();
                perkenalanText.setText(txKenalan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // dapatkan slide show
        db.child("aktifitas").child("slideshowlock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txSlide = (String)dataSnapshot.child("data").getValue();
                slideText.setText(txSlide);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // update kenalan
        simpanPerkenalan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tmpKenalan = perkenalanText.getText().toString().trim();
                db.child("aktifitas").child("perkenalanlock").child("data").child("data").setValue(tmpKenalan).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            final String[] perkenalanPisah = tmpKenalan.split("\n");
                            final Map<String,Object> muatanKenlan = new HashMap<>();
                            db.child("aktifitas").child("perkenalan").removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    for (String barangKenalan : perkenalanPisah){
                                        muatanKenlan.put("url",barangKenalan);
                                        databaseReference.push().setValue(muatanKenlan).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    hitungYuk2++;
                                                    if (hitungYuk2>= perkenalanPisah.length){
                                                        Toast.makeText(getContext(),"berhasil ya",Toast.LENGTH_LONG).show();
                                                        hitungYuk2 = 0;
                                                    }
                                                }
                                            }
                                        });
                                    }

                                }
                            });

                        }
                    }
                });
            }
        });

        // update slide
        simpanSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tmpSlide = slideText.getText().toString().trim();
                db.child("aktifitas").child("slideshowlock").child("data").setValue(tmpSlide).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            final String[] pslidePisah = tmpSlide.split("\n");
                            final Map<String,Object> slideMuatan = new HashMap<>();
                            db.child("aktifitas").child("slideshow").removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                    for (String barangslide : pslidePisah){
                                        slideMuatan.put("url",barangslide);
                                        databaseReference.push().setValue(slideMuatan).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    hitungYuk1++;
                                                    if (hitungYuk1 >= pslidePisah.length){
                                                        Toast.makeText(getContext(),"berhasil ya ",Toast.LENGTH_LONG).show();
                                                        hitungYuk1 = 0;
                                                    }
                                                }

                                            }
                                        });
                                    }

                                }
                            });
                        }
                    }
                });
            }
        });

    }


}
