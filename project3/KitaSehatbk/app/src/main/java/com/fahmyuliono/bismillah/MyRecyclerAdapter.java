package com.fahmyuliono.bismillah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.VHolder> {

    private List<Map<String,Object>> mapList;
    private LayoutInflater inflater;
    private KetikaBeritaDiKlick ketikaBeritaDiKlick;
    private String maka;
    private StringBuilder stringBuilder;

    MyRecyclerAdapter(Context context, List<Map<String, Object>> list){

        this.inflater = LayoutInflater.from(context);
        this.mapList = list;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHolder(inflater.inflate(R.layout.layout_holder_activity1,parent,false));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VHolder holder) {
        super.onViewAttachedToWindow(holder);

    }

    @Override
    public void onBindViewHolder(@NonNull final VHolder holder, final int position) {
        Map<String,Object> terima = mapList.get(position);
        holder.judulnya.setText(String.valueOf(terima.get("judul")));
        holder.isinya.setText(String.valueOf(terima.get("isi")));

        char logo = String.valueOf(terima.get("judul")).charAt(0);
        holder.logonya.setText(String.valueOf(logo));

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        maka = "";
        stringBuilder.setLength(0);
    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    class VHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView judulnya;
        private TextView isinya;
        private TextView logonya;

        VHolder(@NonNull View itemView) {
            super(itemView);
            judulnya = itemView.findViewById(R.id.judul);
            isinya = itemView.findViewById(R.id.isi);
            logonya = itemView.findViewById(R.id.logonya);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ketikaBeritaDiKlick.maka(v,getAdapterPosition());
        }


    }

    public Map<String, Object> getId(int pos){
        return mapList.get(pos);
    }

    void setKetikaBeritaDiKlick(KetikaBeritaDiKlick ketikaBeritaDiKlick) {
        this.ketikaBeritaDiKlick = ketikaBeritaDiKlick;
    }

    interface KetikaBeritaDiKlick{
        void maka(View view,int posisi);
    }

}
