package dev.malikkurosaki.probussystem;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RecyclerviewNewsFeedAdapter extends RecyclerView.Adapter<RecyclerviewNewsFeedAdapter.VHolder> {

    private LayoutInflater inflater;
    private List<Map<String, Objects>> listData;
    private List<VHolder> hol = new ArrayList<>();

    private KetikaSukaDiKlick ketikaSukaDiKlick;
    private StorageReference st;

    RecyclerviewNewsFeedAdapter(Context context, List<Map<String,Objects>> dataList, StorageReference std){
        this.listData = dataList;
        this.inflater = LayoutInflater.from(context);
        this.st = std;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VHolder(inflater.inflate(R.layout.layout_news_feed_view_helper,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final VHolder vHolder, int i) {
        Map<String,Objects> datanya = listData.get(i);

        hol.add(vHolder);

        String content = String.valueOf(datanya.get("content"));
        String contentmore = String.valueOf(datanya.get("contentmore"));
        String gamabar = String.valueOf(datanya.get("gambar"));
        String judul = String.valueOf(datanya.get("judul"));
        String key = String.valueOf(datanya.get("key"));
        String pokok = String.valueOf(datanya.get("pokok"));
        String suka = String.valueOf(datanya.get("suka"));

        if (suka.equals("0")){
            vHolder.newsTotalSuaka.setVisibility(View.GONE);
        }else {
            vHolder.newsTotalSuaka.setVisibility(View.VISIBLE);
            vHolder.newsTotalSuaka.setText(suka +" disukai");
            vHolder.newsSuka.setImageResource(R.drawable.icon_suka2);
        }

        if (contentmore.equals("")){
            vHolder.newsMore.setVisibility(View.GONE);
            vHolder.newsContent.setText(content);
            vHolder.tandaMore.setVisibility(View.GONE);
        }else {
            vHolder.newsContent.setVisibility(View.GONE);
            vHolder.newsMore.setVisibility(View.VISIBLE);
            vHolder.newsMore.setText(contentmore);
            vHolder.tandaMore.setVisibility(View.VISIBLE);
        }

        vHolder.newsJudul.setText(judul);
        st.child("newsfeed").child(gamabar).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(vHolder.newsGambar);
                vHolder.loadImg.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class VHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView newsGambar;
        private ImageView newsSuka;
        private TextView newsTotalSuaka;
        private TextView newsJudul,newsContent,newsMore,tandaMore;
        private ProgressBar loadImg;

        VHolder(@NonNull View itemView) {
            super(itemView);

            newsGambar = itemView.findViewById(R.id.newsGambar);
            newsSuka = itemView.findViewById(R.id.newsSuka);
            newsTotalSuaka = itemView.findViewById(R.id.newsTotalSuka);
            newsJudul = itemView.findViewById(R.id.newsJudul);
            newsContent = itemView.findViewById(R.id.newsContent);
            newsMore = itemView.findViewById(R.id.newsMore);
            tandaMore = itemView.findViewById(R.id.tandaMore);
            loadImg = itemView.findViewById(R.id.loadImg);

            newsSuka.setOnClickListener(this);
            tandaMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ketikaSukaDiKlick.maka(v,getAdapterPosition());
        }
    }

    VHolder getView(int i){
        return hol.get(i);
    }

    void setKetikaSukaDiKlick(KetikaSukaDiKlick diKlick){
        ketikaSukaDiKlick = diKlick;
    }

    interface KetikaSukaDiKlick{
        void maka(View view,int position);
    }
}
