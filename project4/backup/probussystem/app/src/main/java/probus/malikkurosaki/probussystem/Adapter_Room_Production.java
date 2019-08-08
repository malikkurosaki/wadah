package probus.malikkurosaki.probussystem;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class Adapter_Room_Production extends RecyclerView.Adapter<Adapter_Room_Production.VHolder> {

    private List<Map<String, Object>> listRoomNya;
    private LayoutInflater inflater;


    public Adapter_Room_Production(Context context1, List<Map<String, Object>> list){
        this.inflater = LayoutInflater.from(context1);
        this.listRoomNya = list;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VHolder(inflater.inflate(R.layout.holder_room_production,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder vHolder, int i) {
       Map<String, Object> terimaRoom = listRoomNya.get(i);

        String tx = String.valueOf(terimaRoom.get("nm_agen")).toLowerCase();
        String[] words = tx.split(" ");
        StringBuilder sb = new StringBuilder();
        if (words[0].length() > 0) {
            sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
            for (int x = 1; x < words.length; x++) {
                sb.append(" ");
                sb.append(Character.toUpperCase(words[x].charAt(0)) + words[x].subSequence(1, words[x].length()).toString().toLowerCase());
            }
        }
        String agen = sb.toString();

        DecimalFormat formatNya = new DecimalFormat("#,###");
        String revenuenya = "Rp"+formatNya.format(Double.parseDouble(String.valueOf(terimaRoom.get("revenue"))));

        vHolder.romNama.setText(agen);
        vHolder.romAngka.setText(String.valueOf(terimaRoom.get("value")));
        vHolder.revenue.setText(revenuenya);

    }

    @Override
    public int getItemCount() {
        return listRoomNya.size();
    }

    class VHolder extends RecyclerView.ViewHolder {
        TextView romNama,romAngka,revenue;
        VHolder(@NonNull View itemView) {
            super(itemView);
            romNama = itemView.findViewById(R.id.rom_nama);
            romAngka = itemView.findViewById(R.id.rom_angka);
            revenue = itemView.findViewById(R.id.revenue);
        }
    }
}
