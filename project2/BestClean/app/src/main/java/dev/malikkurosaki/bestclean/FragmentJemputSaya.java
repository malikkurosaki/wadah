package dev.malikkurosaki.bestclean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FragmentJemputSaya extends Fragment {

    private Context context;
    private Activity activity;
    private View view;

    private ImageView showGambar;
    private EditText jemputAlamat;
    private TextView jemputTanggal;


    FragmentJemputSaya newInstance(){
        return new FragmentJemputSaya();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_jemput_saya,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.context = view.getContext();
        this.activity = (Activity)context;


        showGambar = view.findViewById(R.id.showGambar);
        jemputAlamat = view.findViewById(R.id.jemputAalamat);
        jemputTanggal = view.findViewById(R.id.jemputTanggal);


        Map<String,Object> terimaData = (HashMap<String,Object>)getArguments().getSerializable("datanya");

        jemputAlamat.setText(String.valueOf(terimaData.get("address")));
       /* Intent intent  = getIntent();


        showGambar.setImageBitmap(bitmap);
        jemputAlamat.setText(String.valueOf(terimaAlamat.get("address")));*/

        byte[] byteArray = (byte[]) terimaData.get("gambar");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);;
        showGambar.setImageBitmap(bitmap);



        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd-MM-yyy");
        String currentDateandTime = sdf.format(new Date());
        jemputTanggal.setText(currentDateandTime);


    }
}
