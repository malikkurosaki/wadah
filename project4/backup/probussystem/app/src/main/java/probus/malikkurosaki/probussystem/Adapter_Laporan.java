package probus.malikkurosaki.probussystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class Adapter_Laporan extends RecyclerView.Adapter<Adapter_Laporan.VHolder> {


    private List<Map<String, Object>> mapList;
    private LayoutInflater inflater;
    Adapter_Laporan(Context context, List<Map<String, Object>> list){
        this.inflater = LayoutInflater.from(context);
        this.mapList = list;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHolder(inflater.inflate(R.layout.holder_laporan_helper,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        Map<String, Object> datanya = mapList.get(position);

        try{

            String tanggalDatang = String.valueOf(datanya.get("tgl_datang"));
            String tanggalBerang = String.valueOf(datanya.get("tgl_berang"));

            DecimalFormat formatNya = new DecimalFormat("#,###");

            String[] bulanNy = {"Jan","Feb","Mar","Apr","Mei","Jun","Jul","Ags","Sep","Okt","Nob","Des"};

            String negara = !String.valueOf(datanya.get("nasional")).contains("null")?String.valueOf(datanya.get("nasional")):"undefined";
            String agennya = !String.valueOf(datanya.get("agen")).contains("null")?String.valueOf(datanya.get("agen")):"undefined";

            String[] tanggalTahunDatang = tanggalDatang.split(" ");
            String[] pisahTanggalDatang = tanggalTahunDatang[0].split("-");
            String jadiTanggalDatang = pisahTanggalDatang[2] +" "+bulanNy[Integer.parseInt(pisahTanggalDatang[1])-1];

            String[] tanggalTahunBerang = tanggalBerang.split(" ");
            String[] pisahTanggalBerang = tanggalTahunBerang[0].split("-");
            String jadiTanggalBerang = pisahTanggalBerang[2] +" "+bulanNy[Integer.parseInt(pisahTanggalBerang[1])-1];

            String berapaMalam = datanya.get("lama") +" Night";
            String permalamNya = "Rp"+formatNya.format(Double.parseDouble(String.valueOf(datanya.get("hrg_room")))) + "/ Night";
            String totalNya = "Rp"+ formatNya.format(Double.parseDouble(String.valueOf(datanya.get("total_harga"))));

            holder.laporanTanggalDatang.setText(jadiTanggalDatang);
            holder.laporanTahunDatang.setText(String.valueOf(pisahTanggalDatang[0]));
            holder.laporanTanggalPergi.setText(jadiTanggalBerang);
            holder.laporanTahunPergi.setText(String.valueOf(pisahTanggalBerang[0]));
            holder.laporanJenis.setText(String.valueOf(datanya.get("gsegmen")));
            holder.laporanAgen.setText(agennya);
            holder.laporanNama.setText(String.valueOf(datanya.get("nama_tamu")));
            holder.laporannegara.setText(negara);
            holder.laporanMalam.setText(berapaMalam);
            holder.laporanKamar.setText(String.valueOf(datanya.get("jenis_kamar")));
            holder.laporanPerMalam.setText(permalamNya);
            holder.laporanTotal.setText(totalNya);
            holder.laporanDewasa.setText(String.valueOf(datanya.get("pax")));
            holder.laporanAnak.setText(String.valueOf(datanya.get("pax2")));
            holder.laporanHitung.setText(String.valueOf(position+1));
            holder.laporanGuestSegment.setText(String.valueOf(datanya.get("guestsegmen")).trim());
        }catch (Exception e){
            e.printStackTrace();
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
        TextView laporanDewasa;
        TextView laporanAnak;
        TextView laporanHitung;
        TextView laporanGuestSegment;

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
            laporanDewasa = itemView.findViewById(R.id.laporanDewasa);
            laporanAnak = itemView.findViewById(R.id.laporanAnak);
            laporanHitung = itemView.findViewById(R.id.laporanHitung);
            laporanGuestSegment = itemView.findViewById(R.id.laporanGuesSegment);

        }
    }
}
