package dev.malikkurosaki.probussystem;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class RoomProductionAdapter extends RecyclerView.Adapter<RoomProductionAdapter.VHolder> {

    private List<Map<String,Object>> listRoomNya;
    private LayoutInflater inflater;


    public RoomProductionAdapter(Context context1, List<Map<String, Object>> list){
        this.inflater = LayoutInflater.from(context1);
        this.listRoomNya = list;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VHolder(inflater.inflate(R.layout.room_production_holder,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder vHolder, int i) {
       Map<String,Object> terimaRoom = listRoomNya.get(i);
       vHolder.romNama.setText(String.valueOf(terimaRoom.get("nm_agen")));
       vHolder.romAngka.setText(String.valueOf(terimaRoom.get("value")));

    }

    @Override
    public int getItemCount() {
        return listRoomNya.size();
    }

    class VHolder extends RecyclerView.ViewHolder {
        TextView romNama,romAngka;
        VHolder(@NonNull View itemView) {
            super(itemView);
            romNama = itemView.findViewById(R.id.rom_nama);
            romAngka = itemView.findViewById(R.id.rom_angka);
        }
    }
}
