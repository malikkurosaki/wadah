package dev.malikkurosaki.probussystem;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class SlideShowHelperAdapter extends RecyclerView.Adapter<SlideShowHelperAdapter.VHolder> {

    private LayoutInflater inflater;
    private List<Map<String,Object>> dataSlide;
    private KetikaSlideDiklik slideDiklik;

    SlideShowHelperAdapter(Context context,List<Map<String,Object>> dataSlide1){
        this.inflater = LayoutInflater.from(context);
        this.dataSlide = dataSlide1;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.layout_slider_helper,viewGroup,false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder vHolder, int i) {
        Map<String,Object> dataGambar = dataSlide.get(i);

        Picasso.get().load(String.valueOf(dataGambar.get("url"))).into(vHolder.gambarSlide);
     }

    @Override
    public int getItemCount() {
        return dataSlide.size();
    }

    class VHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView gambarSlide;
        VHolder(@NonNull View itemView) {
            super(itemView);

            gambarSlide = itemView.findViewById(R.id.gambarslide);

            gambarSlide.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            slideDiklik.maka(v,getAdapterPosition());
        }
    }

    String getId(int id){
        return String.valueOf(dataSlide.get(id));
    }

    void setSlideDiklik(KetikaSlideDiklik slideDiklik) {
        this.slideDiklik = slideDiklik;
    }

    interface KetikaSlideDiklik{
        void maka(View view, int position);
    }
}
