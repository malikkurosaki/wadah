package dev.malikkurosaki.probussystem;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatbotRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Map<String,Objects>> listCostumers;
    private LayoutInflater inflater;
    private View view;

    private static final int TYPE0 = 0;
    private static final int TYPE2 = 2;
    private static final int TYPE4 = 4;

    private KetikaHolder4Diklik ketikaHolder4Diklik;


    ChatbotRecyclerAdapter(Context context, List<Map<String, Objects>> theData){
        this.listCostumers = theData;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i){
            case TYPE0:
                return new VHolder0(inflater.inflate(R.layout.layout_chatbot_vholder0,viewGroup,false));
            case TYPE2:
               return new VHolder2(inflater.inflate(R.layout.layout_chatbot_vholder2,viewGroup,false));
            case TYPE4:
                return new VHolder4(inflater.inflate(R.layout.layout_chatbot_vholder4,viewGroup,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,int i) {
        Map<String,Objects> myData = listCostumers.get(i);
        String kunci = String.valueOf(myData.get("type"));
        switch (kunci){
            case "0":
                VHolder0 vHolder0 = (VHolder0)viewHolder;
                vHolder0.textMe.setText(String.valueOf(myData.get("pesan")));
                break;
            case "2":
                VHolder2 vHolder2 = (VHolder2)viewHolder;
                vHolder2.textBot.setText(String.valueOf(myData.get("pesan")));
                break;
            case "4":
                VHolder4 vHolder4 = (VHolder4)viewHolder;
                vHolder4.cusNama.setText(String.valueOf(myData.get("nama_tamu")));
                vHolder4.cusIn.setText(String.valueOf(myData.get("tanggal_datang")));
                vHolder4.cusOut.setText(String.valueOf(myData.get("tanggal_berang")));
                vHolder4.cusRoom.setText(String.valueOf(myData.get("no_room")));
                vHolder4.blnIn.setText(String.valueOf(myData.get("")));
                vHolder4.blnOut.setText(String.valueOf(myData.get("")));
                vHolder4.tanggalContainer.setVisibility(View.GONE);
                vHolder4.textConfirm.setText(String.valueOf(myData.get("stt")));
                break;
        }
    }


    @Override
    public int getItemCount() {
        return listCostumers.size();
    }

    @Override
    public int getItemViewType(int position) {
        Map<String,Objects> myData = listCostumers.get(position);
        int pos = Integer.parseInt(String.valueOf(myData.get("type")));
        switch (pos){
            case 0:
                return TYPE0;
            case 2:
                return TYPE2;
            case 4:
                return TYPE4;
        }
        return Integer.parseInt(null);
    }


    public class VHolder0 extends  RecyclerView.ViewHolder{
        TextView textMe;
        VHolder0(@NonNull View itemView) {
            super(itemView);
            textMe = itemView.findViewById(R.id.textMe);
        }
    }
    public class VHolder2 extends  RecyclerView.ViewHolder{

        TextView textBot;
        VHolder2(@NonNull View itemView) {
            super(itemView);
            textBot = itemView.findViewById(R.id.textBot);
        }
    }
    public class VHolder4 extends  RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cusNama,cusIn,cusOut,cusRoom,blnIn,blnOut,textConfirm;
        LinearLayout tanggalContainer;
        VHolder4(@NonNull View itemView) {
            super(itemView);
            cusNama = itemView.findViewById(R.id.cusNama);
            cusIn = itemView.findViewById(R.id.cusIn);
            cusOut = itemView.findViewById(R.id.cusOut);
            cusRoom = itemView.findViewById(R.id.cusRoom);
            blnIn = itemView.findViewById(R.id.blnIn);
            blnOut = itemView.findViewById(R.id.blnOut);
            tanggalContainer = itemView.findViewById(R.id.tanggal_container);
            textConfirm = itemView.findViewById(R.id.text_confirm);

            itemView.setOnClickListener(this);
        }

        // ketika hasil customer di klik
        @Override
        public void onClick(View v) {
            ketikaHolder4Diklik.maka(v,getAdapterPosition());
        }
    }

    public Map<String,Objects> getPosisi(int pos){
        return listCostumers.get(pos);
    }

    void setKetikaHolder4Diklik(KetikaHolder4Diklik ketikaHolder4Diklik) {
        this.ketikaHolder4Diklik = ketikaHolder4Diklik;
    }

    public interface KetikaHolder4Diklik{
        void maka(View view, int position);
    }

}
