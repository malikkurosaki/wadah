package com.malikkurosaki.bestschedule;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HasilJadwalAdapter extends RecyclerView.Adapter<HasilJadwalAdapter.VHolder> {

    private LayoutInflater inflater;
    private List<Map<String,Objects>> listJadwal;

    HasilJadwalAdapter(Context context, List<Map<String, Objects>> listJadwal1){
        this.inflater = LayoutInflater.from(context);
        this.listJadwal = listJadwal1;
    }
    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VHolder(inflater.inflate(R.layout.layout_hasil_jadwal_adapter,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder vHolder, int i) {
        Map<String,Objects> ambil = listJadwal.get(i);
        String jamNya = ambil.get("jam") +":00";
        vHolder.jadwalJam.setText(jamNya);
        vHolder.jadwalNamaPengajar.setText(String.valueOf(ambil.get("tutor")));
        vHolder.jadwalNamaSiswa.setText(String.valueOf(ambil.get("namasiswa")));
        vHolder.jadwalIdsiswa.setText(String.valueOf(ambil.get("siswaid")));
        vHolder.jadwalCatatan.setText(String.valueOf(ambil.get("catatan")));

    }

    @Override
    public int getItemCount() {
        return listJadwal.size();
    }

    class VHolder extends RecyclerView.ViewHolder {

        private TextView jadwalJam,jadwalNamaPengajar,jadwalNamaSiswa,jadwalIdsiswa,jadwalCatatan;
        VHolder(@NonNull View itemView) {
            super(itemView);

            jadwalJam = itemView.findViewById(R.id.jadwalJam);
            jadwalNamaPengajar = itemView.findViewById(R.id.jadwalNamaPengajar);
            jadwalNamaSiswa = itemView.findViewById(R.id.jadwalNamaSiswa);
            jadwalIdsiswa = itemView.findViewById(R.id.jadwalIdSiswa);
            jadwalCatatan = itemView.findViewById(R.id.jadwalCatatan);
        }
    }
}
