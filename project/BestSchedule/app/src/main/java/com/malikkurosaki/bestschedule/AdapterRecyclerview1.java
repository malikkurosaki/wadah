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

public class AdapterRecyclerview1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater inflater;
    private List<Map<String,Objects>> listData;

    private static final int TYPE0 = 0;
    private static final int TYPE2 = 2;

    AdapterRecyclerview1(Context context,List<Map<String,Objects>> listData1){
        this.listData = listData1;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i){
            case TYPE0: return new VHolder0(inflater.inflate(R.layout.layout_vholder0,viewGroup,false));
            case TYPE2: return new VHolder2(inflater.inflate(R.layout.layout_vholder1,viewGroup,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Map<String,Objects> isian = listData.get(i);
        String kunci = String.valueOf(isian.get("type"));
        switch (kunci){
            case "0":
                VHolder0 holder0 = (VHolder0)viewHolder;
                holder0.namaKiri.setText(String.valueOf(isian.get("nama")));
                break;
            case "2":
                VHolder2 holder2 = (VHolder2)viewHolder;
                holder2.namaKanan.setText(String.valueOf(isian.get("nama")));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public int getItemViewType(int position) {
        Map<String,Objects> pos = listData.get(position);
        String kunci = String.valueOf(pos.get("type"));
        switch (kunci){
            case "0":
                return TYPE0;
            case "2":
                return TYPE2;
        }
        return Integer.parseInt(null);
    }

    class VHolder0 extends RecyclerView.ViewHolder{

        TextView namaKiri;
        VHolder0(@NonNull View itemView) {
            super(itemView);
            namaKiri = itemView.findViewById(R.id.namaKiri);
        }
    }

    class VHolder2 extends RecyclerView.ViewHolder{
        TextView namaKanan;
        VHolder2(@NonNull View itemView) {
            super(itemView);
            namaKanan = itemView.findViewById(R.id.namaKanan);
        }
    }
}
