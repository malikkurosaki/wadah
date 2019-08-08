package com.malikkurosaki.bestschedule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class LayoutTambahTutor extends Fragment {

    private View view;
    private EditText editTutor;
    private Button simpanTutor;
    private DatabaseReference db;
    private String[] pecahan;
    private String datanya;
    private ProgressBar loading;

    LayoutTambahTutor newInstance(){
        return new LayoutTambahTutor();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_tambah_tutor,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;

        editTutor = view.findViewById(R.id.editTitor);
        simpanTutor = view.findViewById(R.id.simpanTutor);
        loading = view.findViewById(R.id.loading);



        // firebase
        db = FirebaseDatabase.getInstance().getReference();

        db.child("tutor").child("tutorlock").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Objects> ambil = (HashMap<String,Objects>)dataSnapshot.getValue();
                Map<String,Objects> terima = new HashMap<>();
                for (Map.Entry<String,Objects> hasil : ambil.entrySet()){
                    terima.put(hasil.getKey(),hasil.getValue());
                }
                editTutor.setText(String.valueOf(terima.get("data")));
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        simpanTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                datanya = editTutor.getText().toString().trim();
                pecahan = datanya.split("\n");
                db.child("tutor").child("daftartutor").removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Map<String, String> wadahnya = new HashMap<>();
                        for (final String tutore : pecahan){
                            wadahnya.put("nama",tutore);
                            databaseReference.push().setValue(wadahnya).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        db.child("tutor").child("tutorlock").child("data").setValue(datanya).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                   loading.setVisibility(View.GONE);
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
