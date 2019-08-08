package dev.malikkurosaki.probussystem.probus;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import dev.malikkurosaki.probussystem.R;

public class Probus_Laporan_Adapter extends RecyclerView.Adapter<Probus_Laporan_Adapter.VHolder> {


    private List<Map<String,Object>> mapList;
    private LayoutInflater inflater;
    Probus_Laporan_Adapter (Context context, List<Map<String,Object>> list){
        this.inflater = LayoutInflater.from(context);
        this.mapList = list;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHolder(inflater.inflate(R.layout.probus_laporan_helper,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        Map<String,Object> datanya = mapList.get(position);

        String tanggalDatang = String.valueOf(datanya.get("tgl_datang"));
        String tanggalBerang = String.valueOf(datanya.get("tgl_berang"));

        String[] bulanNy = {"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Ags","Sep","Okt","Nob","Des"};

        try {
            String[] tanggalTahunDatang = tanggalDatang.split(" ");
            String[] pisahTanggalDatang = tanggalTahunDatang[0].split("-");
            String jadiTanggalDatang = pisahTanggalDatang[2] +" "+bulanNy[Integer.parseInt(pisahTanggalDatang[1])];

            String[] tanggalTahunBerang = tanggalBerang.split(" ");
            String[] pisahTanggalBerang = tanggalTahunBerang[0].split("-");
            String jadiTanggalBerang = pisahTanggalBerang[2] +" "+bulanNy[Integer.parseInt(pisahTanggalBerang[1])];

            holder.laporanTanggalDatang.setText(jadiTanggalDatang);
            holder.laporanTahunDatang.setText(String.valueOf(pisahTanggalDatang[0]));
            holder.laporanTanggalPergi.setText(jadiTanggalBerang);
            holder.laporanTahunPergi.setText(String.valueOf(pisahTanggalBerang[0]));
            holder.laporanJenis.setText(String.valueOf(datanya.get("gsegmen")));
            holder.laporanAgen.setText(String.valueOf(datanya.get("agen")));
            holder.laporanNama.setText(String.valueOf(datanya.get("nama_tamu")));
            holder.laporannegara.setText(String.valueOf(datanya.get("national")));
            holder.laporanMalam.setText(String.valueOf(datanya.get("lama")));
            holder.laporanKamar.setText(String.valueOf(datanya.get("jenis_kamar")));
            holder.laporanPerMalam.setText(String.valueOf(datanya.get("hrg_room")));
            holder.laporanTotal.setText(String.valueOf(datanya.get("total_harga")));
        }catch (Exception e){
            e.printStackTrace();
            Log.i("-->", "onBindViewHolder: "+e);
        }

    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    class VHolder extends RecyclerView.ViewHolder {

        TextView laporanTanggalDatang;
        TextView laporanTahunDatang;
        TextView laporanTanggalPergi;
        TextView laporanTahunPergi;
        TextView laporanJenis;
        TextView laporanAgen;
        TextView laporanNama;
        TextView laporannegara;
        TextView laporanMalam;
        TextView laporanKamar;
        TextView laporanPerMalam;
        TextView laporanTotal;

        VHolder(@NonNull View itemView) {
            super(itemView);

            laporanTanggalDatang = itemView.findViewById(R.id.laporanTanggalDatang);
            laporanTahunDatang = itemView.findViewById(R.id.laporanTahunDatang);
            laporanTanggalPergi = itemView.findViewById(R.id.laporanTanggalPergi);
            laporanTahunPergi = itemView.findViewById(R.id.laporanTahunPergi);
            laporanJenis = itemView.findViewById(R.id.laporanJenis);
            laporanAgen = itemView.findViewById(R.id.laporanAgen);
            laporanNama = itemView.findViewById(R.id.laporanNama);
            laporannegara = itemView.findViewById(R.id.laporannegara);
            laporanMalam = itemView.findViewById(R.id.laporanMalam);
            laporanKamar = itemView.findViewById(R.id.laporanKamar);
            laporanPerMalam = itemView.findViewById(R.id.laporanPerMalam);
            laporanTotal = itemView.findViewById(R.id.laporanTotal);

        }
    }
}
