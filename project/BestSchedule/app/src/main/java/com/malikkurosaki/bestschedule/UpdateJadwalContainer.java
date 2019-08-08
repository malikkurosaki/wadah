package com.malikkurosaki.bestschedule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class UpdateJadwalContainer extends Fragment {
    private CalendarView calendar;
    private Spinner pilihTutor,pilihJam;
    private EditText namaSiswa,idSiswa,keterangan;
    private Button simpan;

    private FrameLayout adminContainer;
    // firebase
    private DatabaseReference db;

    private List<String> jams;
    private List<String> tutors;
    private List<String> listTutorId;

    private String tahun,bulan,tanggal,tutor,jam,siswaNama,siswaId,catatan,idTutor;
    private String kunciNya;

    private CardView pengaman1;
    private Button pengamanTidak;
    private Button pengamanIya;

    private String kunciTimapa;
    private Map<String,Object> terima1;

    private List<Map<String,Object>> listSaring;
    private ProgressBar loading;

    private String TAG ="-->";
    private Map<String,Object> kirimData;

    private View view;
    private Context context;
    private Activity activity;

    public static UpdateJadwalContainer newInstance(){
        return new UpdateJadwalContainer();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_update_jadwal_container,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.context = view.getContext();
        this.activity = (Activity)context;

        calendar = view.findViewById(R.id.calendar);
        pilihTutor = view.findViewById(R.id.pilihTutor);
        pilihJam = view.findViewById(R.id.pilihJam);
        namaSiswa = view.findViewById(R.id.namasiswa);
        idSiswa = view.findViewById(R.id.idSiswa);
        keterangan = view.findViewById(R.id.keterangan);
        simpan = view.findViewById(R.id.simpan);
        pengaman1 = view.findViewById(R.id.pengaman1);
        pengamanTidak = view.findViewById(R.id.pengamanTidak);
        pengamanIya = view.findViewById(R.id.pengamanIya);
        loading = view.findViewById(R.id.loading);
        adminContainer = view.findViewById(R.id.adminContainer);


        pengaman1.setVisibility(View.GONE);
        db = FirebaseDatabase.getInstance().getReference();

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                tahun = String.valueOf(year);
                bulan = String.valueOf(month+1);
                tanggal = String.valueOf(dayOfMonth);
            }
        });

        jams = new ArrayList<>();
        jams.add("pilih jam");
        for (int jm = 8;jm<20;jm++){
            jams.add(jm +":00");
        }
        ArrayAdapter<String> jamAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, jams);
        jamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pilihJam.setAdapter(jamAdapter);

        db.child("tutor").child("daftartutor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tutors = new ArrayList<>();
                listTutorId = new ArrayList<>();
                tutors.add("pilih tutor");
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String,Object> lihaTutor = (HashMap<String, Object>)ds.getValue();
                    Map<String,Object> wadah = new HashMap<>();
                    for (Map.Entry<String,Object> ent : lihaTutor.entrySet()){
                        wadah.put(ent.getKey(),ent.getValue());
                    }
                    tutors.add(String.valueOf(wadah.get("nama")));
                    listTutorId.add(String.valueOf(wadah.get("id")));
                }

                ArrayAdapter<String> tutorAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, tutors);
                tutorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pilihTutor.setAdapter(tutorAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                String[] sJam = pilihJam.getSelectedItem().toString().split(":");
                jam = sJam[0];
                siswaNama = namaSiswa.getText().toString().trim();
                siswaId = idSiswa.getText().toString().trim();
                tutor = pilihTutor.getSelectedItem().toString();
                catatan = keterangan.getText().toString().trim();

                if (TextUtils.isEmpty(tahun) || TextUtils.isEmpty(siswaNama) || TextUtils.isEmpty(siswaId) || tutor.equals("pilih tutor") || TextUtils.isEmpty(catatan)){
                    Toast.makeText(getContext(),"yang bener ngisinya .. isi semua !!",Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.GONE);
                    return;
                }

                db.child("kegiatan").child(tahun).child(bulan).child(tanggal).child(tutor).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        kirimData = new HashMap<>();
                        kirimData.put("tahun",tahun);
                        kirimData.put("bulan",bulan);
                        kirimData.put("tanggal",tanggal);
                        kirimData.put("tutor",tutor);
                        kirimData.put("jam",jam);
                        kirimData.put("namasiswa",siswaNama);
                        kirimData.put("siswaid",siswaId);
                        kirimData.put("catatan",catatan);

                        if (dataSnapshot.hasChild(jam)){
                            pengaman1.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            pengamanIya.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loading.setVisibility(View.VISIBLE);
                                    mengirimkan(kirimData);
                                }
                            });

                            pengamanTidak.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loading.setVisibility(View.GONE);
                                    activity.finish();
                                    startActivity(activity.getIntent());
                                }
                            });
                        }else {
                            mengirimkan(kirimData);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    void mengirimkan(Map<String,Object> lisNya){
        db.child("kegiatan").child(tahun).child(bulan).child(tanggal).child(tutor).child(jam).setValue(lisNya).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"berhasil",Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.GONE);
                    pengaman1.setVisibility(View.GONE);
                    KirimBerita berita = new KirimBerita("ada update tutor baru","jadwal : "+tahun+"-"+bulan+"-"+tanggal+"\n\n tutor :"+tutor+"\n jam :"+jam);
                    berita.execute();
                    activity.finish();
                    startActivity(activity.getIntent());
                }
            }
        });


    }
}
