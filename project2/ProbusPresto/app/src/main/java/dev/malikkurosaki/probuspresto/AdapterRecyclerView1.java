package dev.malikkurosaki.probuspresto;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

public class AdapterRecyclerView1 extends RecyclerView.Adapter<AdapterRecyclerView1.Vholder> {

    private LayoutInflater inflater;
    private List<Map<String,Object>> mapList;
    private KetikaAdapter1DiKlick ketikaAdapter1DiKlick;


    AdapterRecyclerView1(Context contextNya,List<Map<String,Object>> mapListNya){
        this.inflater = LayoutInflater.from(contextNya);
        this.mapList = mapListNya;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Vholder(inflater.inflate(R.layout.layout_adapter_recycler1,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder vholder, int i) {

    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    public class Vholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Vholder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

            ketikaAdapter1DiKlick.maka(v,getAdapterPosition());
        }
    }


    String getAdapter1(int position){
        return String.valueOf(mapList.get(position));
    }

    public void setKetikaAdapter1DiKlick(KetikaAdapter1DiKlick ketikaAdapter1DiKlick) {
        this.ketikaAdapter1DiKlick = ketikaAdapter1DiKlick;
    }

    interface KetikaAdapter1DiKlick{
        void maka(View view,int position);
    }
}
