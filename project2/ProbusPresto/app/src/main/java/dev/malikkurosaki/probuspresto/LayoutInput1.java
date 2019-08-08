package dev.malikkurosaki.probuspresto;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class LayoutInput1 extends Fragment {

    private Context context;
    private Activity activity;
    private View view;

    private ImageView makananGambar;
    private EditText makananNama;
    private EditText makananHarga;
    private EditText makananKeterangan;
    private Button makananSimpan;
    private TextView logNya;

    private HelperLog helperLog;
    private DatabaseReference db;

    LayoutInput1 newInstance(){
        return new LayoutInput1();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_input1,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.context = view.getContext();
        this.activity = (Activity)context;



        makananGambar = view.findViewById(R.id.makananGambar);
        makananNama = view.findViewById(R.id.makananNama);
        makananHarga = view.findViewById(R.id.makananHarga);
        makananKeterangan = view.findViewById(R.id.makananKeterangan);
        makananSimpan = view.findViewById(R.id.makananSimpan);
        logNya = view.findViewById(R.id.logNya);


        //firebase
        db = FirebaseDatabase.getInstance().getReference();
        helperLog = new HelperLog(db,logNya);


        makananGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"),61);
            }
        });

        makananSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam = makananNama.getText().toString().trim();
                String harg = makananHarga.getText().toString().trim();
                String ket = makananKeterangan.getText().toString().trim();

                if (TextUtils.isEmpty(nam) || TextUtils.isEmpty(harg) || TextUtils.isEmpty(ket)){
                    helperLog.catat("lengkapi semuanya");
                    return;
                }


            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
