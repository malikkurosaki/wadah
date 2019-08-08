package probus.malikkurosaki.probussystem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

public class Adapter_Notifikasi extends RecyclerView.Adapter<Adapter_Notifikasi.VHolder> {


    private LayoutInflater inflater;
    private List<Map<String, Object>> list;

    Adapter_Notifikasi(Context context, List<Map<String, Object>> mapList) {
        this.inflater = LayoutInflater.from(context);
        this.list = mapList;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHolder(inflater.inflate(R.layout.holder_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        Map<String, Object> datanya = list.get(position);

        char logonya = String.valueOf(datanya.get("nama_tamu")).charAt(0);

        holder.notifyConLogo.setCardBackgroundColor(ChartUtils.pickColor());
        holder.notifyLogo.setText(String.valueOf(logonya));
        holder.notifyTitle.setText(String.valueOf(datanya.get("nama_tamu")));
        holder.notifyBody.setText(String.valueOf(datanya.get("nasional")));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class VHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.notifyLogo)
        TextView notifyLogo;
        @BindView(R.id.notifyTitle)
        TextView notifyTitle;
        @BindView(R.id.notifyBody)
        TextView notifyBody;
        @BindView(R.id.notifyConLogo)
        CardView notifyConLogo;

        VHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }


    }
}
