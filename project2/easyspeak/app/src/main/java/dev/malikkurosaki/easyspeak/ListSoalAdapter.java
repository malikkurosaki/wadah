package dev.malikkurosaki.easyspeak;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ListSoalAdapter extends RecyclerView.Adapter<ListSoalAdapter.VHolderSoal> {

    private List<String> stringList;
    private LayoutInflater inflater;
    private KetikaSoalDiklik ketikaSoalDiklik;

    ListSoalAdapter(Context context,List<String> list){
        this.inflater = LayoutInflater.from(context);
        this.stringList = list;
    }

    @NonNull
    @Override
    public VHolderSoal onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VHolderSoal(inflater.inflate(R.layout.layout_list_soal_adapter,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolderSoal vHolderSoal, int i) {
        vHolderSoal.itemSoal.setText(stringList.get(i));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class VHolderSoal extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView itemSoal;
        VHolderSoal(@NonNull View itemView) {
            super(itemView);
            itemSoal = itemView.findViewById(R.id.itemSoal);
            itemSoal.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            ketikaSoalDiklik.maka(v,getAdapterPosition());
        }
    }
    String getItem(int pos){
        return stringList.get(pos);
    }
    void setKetikaSoalDiklik(KetikaSoalDiklik ketikaSoalDiklik1) {
        this.ketikaSoalDiklik = ketikaSoalDiklik1;
    }

    interface KetikaSoalDiklik{
        void maka(View view,int position);
    }
}
