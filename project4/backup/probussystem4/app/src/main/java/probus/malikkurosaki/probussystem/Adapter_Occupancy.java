package probus.malikkurosaki.probussystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class Adapter_Occupancy extends RecyclerView.Adapter<Adapter_Occupancy.VHolder> {

    private List<Map<String,Object>> listOcc;
    private LayoutInflater inflater    ;
    Adapter_Occupancy(Context context, List<Map<String,Object>> list){
        this.inflater = LayoutInflater.from(context);
        this.listOcc = list;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHolder(inflater.inflate(R.layout.holder_occupancy,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        Map<String,Object> datanya = listOcc.get(position);
        holder.occBulan.setText(String.valueOf(datanya.get("month")));
        holder.occValue.setText(String.valueOf(datanya.get("occupancy")));


    }

    @Override
    public int getItemCount() {
        return listOcc.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {

        private TextView occBulan,occValue;

        public VHolder(@NonNull View itemView) {
            super(itemView);

            occBulan = itemView.findViewById(R.id.occBulan);
            occValue = itemView.findViewById(R.id.occValue);
        }
    }
}
