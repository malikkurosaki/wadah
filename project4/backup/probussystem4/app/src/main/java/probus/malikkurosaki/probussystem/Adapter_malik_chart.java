package probus.malikkurosaki.probussystem;

import android.animation.Animator;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.util.ChartUtils;

public class Adapter_malik_chart extends RecyclerView.Adapter<Adapter_malik_chart.VHolder> {
    private List<Map<String,Object>> listNilai;
    private LayoutInflater inflater;
    private Context context1;
    private int hitungan = 0;

    Adapter_malik_chart(Context context,List<Map<String,Object>> list2){
        this.inflater = LayoutInflater.from(context);
        this.listNilai = list2;
        context1 = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHolder(inflater.inflate(R.layout.holder_malik_chart,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        Map<String,Object> dt = listNilai.get(position);

        String bulan[] = {"Jan","Feb","Mar","Apr","Mai","Jun","Jul","Aug","Sep","Okt","Nov","Des"};
        final float scale = context1.getResources().getDisplayMetrics().density;
        int tinggi = Integer.parseInt(String.valueOf(dt.get("occupancy")));
        int tg = (int) (tinggi * scale) + 20 ;
        holder.chart1.getLayoutParams().height = (tg * 3);
        holder.chart1.setBackgroundColor(ChartUtils.pickColor());
        holder.chartBulan.setText(bulan[Integer.parseInt(String.valueOf(dt.get("month")))-1]);
        holder.chartNilai.setText(dt.get("occupancy") + " %");


    }

    @Override
    public int getItemCount() {
        return listNilai.size();
    }

    class VHolder extends RecyclerView.ViewHolder {
        LinearLayout chart1;
        TextView chartNilai,chartBulan;
        CardView bundaranNilai;
        VHolder(@NonNull View itemView) {
            super(itemView);

            chart1 = itemView.findViewById(R.id.chart1);
            chartNilai = itemView.findViewById(R.id.chartNilai);
            chartBulan = itemView.findViewById(R.id.chartBulan);
            bundaranNilai = itemView.findViewById(R.id.bundaranNilai);

            itemView.setOnClickListener(view -> {
                YoYo.with(Techniques.Bounce).duration(1000).onEnd(animator -> YoYo.with(Techniques.SlideOutDown).duration(1000).onEnd(animator1 -> YoYo.with(Techniques.SlideInUp).duration(1000).playOn(bundaranNilai)).playOn(bundaranNilai)).playOn(itemView);

            });


        }
    }


}
