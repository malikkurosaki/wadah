package dev.malikkurosaki.probussystem;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

public class GaleryHelperAdapter extends RecyclerView.Adapter<GaleryHelperAdapter.VHolder> {

    private List<String> listGambar;
    private LayoutInflater inflater;
    private String TAG =" tes";

    private KetikaGambarDiklick ketikaGambarDiklick;

    GaleryHelperAdapter(Context context,List<String> listGambar1){
        this.inflater = LayoutInflater.from(context);
        this.listGambar = listGambar1;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VHolder(inflater.inflate(R.layout.layout_galery_helper_adapter,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder vHolder, int i) {


        File file  = new File(File.pathSeparator + listGambar.get(i));
        //Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(file));
        try{
            vHolder.gambarItem.setImageURI(Uri.fromFile(file));
        }catch (Exception e){
            Log.i(TAG, "onBindViewHolder: "+e);
        }



    }

    @Override
    public int getItemCount() {
        return listGambar.size();
    }

    class VHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView gambarItem;
        VHolder(@NonNull View itemView) {
            super(itemView);
            gambarItem = itemView.findViewById(R.id.gambarItem);
            gambarItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ketikaGambarDiklick.maka(v,getAdapterPosition());
        }
    }

    String getId(int id){
        return String.valueOf(listGambar.get(id));
    }
    void setKetikaGambarDiklick(KetikaGambarDiklick gambarDiklick) {
        this.ketikaGambarDiklick = gambarDiklick;
    }

    interface KetikaGambarDiklick{
        void maka(View view,int position);
    }


}
