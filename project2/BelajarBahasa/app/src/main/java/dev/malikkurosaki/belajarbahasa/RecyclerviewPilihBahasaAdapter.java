package dev.malikkurosaki.belajarbahasa;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class RecyclerviewPilihBahasaAdapter extends RecyclerView.Adapter<RecyclerviewPilihBahasaAdapter.VHolder> {

    private LayoutInflater inflater;
    private List<Map<String,Object>> list;
    private KetikaBahasaDiKlik ketikaBahasaDiKlik;


    RecyclerviewPilihBahasaAdapter(Context context, List<Map<String,Object>> listBahasa){
        this.inflater = LayoutInflater.from(context);
        this.list = listBahasa;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VHolder(inflater.inflate(R.layout.layout_pilih_bahasa_adapter,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder vHolder, int i) {

        Map<String,Object> bendera = list.get(i);
        vHolder.namaBahasa.setText(String.valueOf(bendera.get("nama")));
        vHolder.bendera.setImageResource(Integer.parseInt(String.valueOf(bendera.get("bendera"))));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView bendera;
        private TextView namaBahasa;
        VHolder(@NonNull View itemView) {
            super(itemView);

            bendera = itemView.findViewById(R.id.bendera);
            namaBahasa = itemView.findViewById(R.id.namaBahasa);
        }

        @Override
        public void onClick(View v) {
            ketikaBahasaDiKlik.maka(v,getAdapterPosition());
        }
    }

    String getBahasa(int position){
        return String.valueOf(list.get(position));
    }

    void setKetikaBahasaDiklik(KetikaBahasaDiKlik bahasaDiklik){
        ketikaBahasaDiKlik = bahasaDiklik;
    }

    interface KetikaBahasaDiKlik{
        void maka(View view,int position);
    }
}
