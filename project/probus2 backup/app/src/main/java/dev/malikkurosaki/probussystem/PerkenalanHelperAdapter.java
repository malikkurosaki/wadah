package dev.malikkurosaki.probussystem;

import android.content.Context;
import android.media.Image;
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

public class PerkenalanHelperAdapter extends RecyclerView.Adapter<PerkenalanHelperAdapter.ViewPerkenalanHolder> {

    private List<Map<String,Object>> listGambar;
    private LayoutInflater inflater;
    private KetikaPerkenalanDiklik diklik;

    PerkenalanHelperAdapter(Context context,List<Map<String,Object>> gambarList){
        this.inflater = LayoutInflater.from(context);
        this.listGambar = gambarList;
    }

    @NonNull
    @Override
    public ViewPerkenalanHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.layout_view_perkenalan_helper,viewGroup,false);
        return new ViewPerkenalanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPerkenalanHolder viewPerkenalanHolder, int i) {
        Map<String,Object> data = listGambar.get(i);

        Picasso.get().load(String.valueOf(data.get("url"))).into(viewPerkenalanHolder.gambarcontainer);
    }

    @Override
    public int getItemCount() {
        return listGambar.size();
    }

    class ViewPerkenalanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView gambarcontainer;
        ViewPerkenalanHolder(@NonNull View itemView) {
            super(itemView);
            gambarcontainer = itemView.findViewById(R.id.gamabarContainer);
        }

        @Override
        public void onClick(View v) {
            diklik.jadinya(v,getAdapterPosition());
        }
    }


    String getId(int id){
        return String.valueOf(listGambar.get(id));
    }

    void setDiklik(KetikaPerkenalanDiklik diklik1){
        this.diklik = diklik1;
    }

    interface KetikaPerkenalanDiklik{
        void jadinya(View view,int position);
    }
}
