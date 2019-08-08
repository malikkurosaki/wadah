package dev.malikkurosaki.probussystem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class RoomProductionAdapter extends RecyclerView.Adapter<RoomProductionAdapter.VHolder> {

    private List<String> namanya;
    private List<Object> nilainya;
    private LayoutInflater inflater;


    RoomProductionAdapter(Context context1,List<String> na,List<Object> ni){
        this.inflater = LayoutInflater.from(context1);
        this.namanya = na;
        this.nilainya = ni;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VHolder(inflater.inflate(R.layout.room_production_holder,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder vHolder, int i) {
        String nam = namanya.get(i);
        int nil = (int) Float.parseFloat(String.valueOf(nilainya.get(i)));

        vHolder.romNama.setText(nam);
        vHolder.romAngka.setText(String.valueOf(nil));

    }

    @Override
    public int getItemCount() {
        return namanya.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView romNama,romAngka;
        public VHolder(@NonNull View itemView) {
            super(itemView);

            romNama = itemView.findViewById(R.id.rom_nama);
            romAngka = itemView.findViewById(R.id.rom_angka);
        }
    }
}
