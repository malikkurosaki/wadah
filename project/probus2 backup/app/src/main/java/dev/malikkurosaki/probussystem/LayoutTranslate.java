package dev.malikkurosaki.probussystem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;
import com.xd.commander.translatetool.GoogleTrans;
import com.xd.commander.translatetool.OnTransSuccess;

import org.objectweb.asm.Handle;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LayoutTranslate extends Fragment {

    private Context context;
    private Activity activity;
    private View view;
    private TextView hasilContainer;

    private Spinner bahasa1,bahasa2;
    private ImageView mikBicara;
    private DroidSpeech mendengarkan;
    private boolean bolehDengar = false;
    private TextView keluaranHasil;

    private boolean bergetar = false;

    private String bahasaFrom = "";
    private String bahasaTo = "";
    private int hitungan1 = 0;

    LayoutTranslate newInstance(){
        return new LayoutTranslate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_view_translate,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.context  = view.getContext();
        this.activity = (Activity)context;

        bahasa1  = view.findViewById(R.id.bahasa1);
        bahasa2 = view.findViewById(R.id.bahasa2);
        mikBicara = view.findViewById(R.id.mikBicara);
        keluaranHasil = view.findViewById(R.id.keluaranHasil);
        hasilContainer = view.findViewById(R.id.hasilContainer);

        mendengarkan = new DroidSpeech(context,null);
        keluaranHasil.setVisibility(View.GONE);
        hasilContainer.setVisibility(View.GONE);

        final String[] b1 = {
                "indonesia",
                "inggris"
        };
        final String[] b2 = {
                "inggris",
                "indonesia"
        };


        mendengarkan.setPreferredLanguage("id-IN");

        ArrayAdapter<String> bAdapter1 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,b1);
        bAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bahasa1.setAdapter(bAdapter1);

        ArrayAdapter<String> bAdapter2 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,b2);
        bAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bahasa2.setAdapter(bAdapter2);

        bahasa1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (b1[position].equals(b1[0])){
                    bahasaFrom = "id";
                    mendengarkan.setPreferredLanguage("id-IN");
                }else {
                    bahasaFrom = "en";
                    mendengarkan.setPreferredLanguage("en-US");
                }
                bolehDengar = false;
                //Toast.makeText(getContext(),"langeage set to "+bahasaFrom +" - "+bahasaTo,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bahasa2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (b2[position].equals(b2[0])){
                    bahasaTo = "en";
                }else {
                    bahasaTo = "id";
                }

                bolehDengar = false;
                //Toast.makeText(getContext(),"langeage set to "+bahasaFrom +" - "+bahasaTo,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mikBicara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bolehDengar){
                    setBahasaFrom();
                    bergetar = true;
                    mendengarkan.startDroidSpeechRecognition();
                    mikBicara.setBackgroundResource(R.drawable.bundar_biru);
                    bolehDengar = true;
                    keluaranHasil.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.SlideInLeft).duration(200).playOn(keluaranHasil);
                }else {
                    terlarangDengar();

                }
            }
        });


        mendengarkan.setOnDroidSpeechListener(new OnDSListener() {
            @Override
            public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
                setBahasaFrom();
            }

            @Override
            public void onDroidSpeechRmsChanged(float rmsChangedValue) {
                //mendengarkan.setPreferredLanguage("id_IN");
                if (!bolehDengar){
                    terlarangDengar();
                }else {
                    mikBicara.setBackgroundResource(R.drawable.bundar_biru);
                }
            }

            @Override
            public void onDroidSpeechLiveResult(String liveSpeechResult) {

            }

            @Override
            public void onDroidSpeechFinalResult(String finalSpeechResult) {
                keluaranHasil.setText(finalSpeechResult);
                hasilContainer.setVisibility(View.VISIBLE);

                GoogleTrans.build().with(finalSpeechResult).setFrom(bahasaFrom).setTo(bahasaTo).into(new OnTransSuccess() {
                    @Override
                    public void out(String s) {
                        hasilContainer.setText(s);
                    }
                });

                YoYo.with(Techniques.ZoomInUp).duration(500).playOn(hasilContainer);

            }

            @Override
            public void onDroidSpeechClosedByUser() {

            }

            @Override
            public void onDroidSpeechError(String errorMsg) {

            }
        });

        // runtime

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bolehDengar){
                    hitungan1++;
                    //Toast.makeText(getContext(),"ya",Toast.LENGTH_LONG).show();
                    if (hitungan1 % 2 == 0){
                        //Toast.makeText(getContext(),String.valueOf(hitungan1),Toast.LENGTH_LONG).show();
                        YoYo.with(Techniques.Pulse).duration(1000).playOn(mikBicara);
                    }

                }

                handler.postDelayed(this,500);
            }
        },500);

     }

     void terlarangDengar(){
        bergetar = false;
        mendengarkan.closeDroidSpeechOperations();
        mikBicara.setBackgroundResource(R.drawable.bundar_putih);
        bolehDengar = false;
        YoYo.with(Techniques.SlideOutRight).duration(200).withListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                keluaranHasil.setVisibility(View.GONE);
            }
        }).playOn(keluaranHasil);

    }


    void setBahasaFrom(){
        if (bahasaFrom.equals("id")){
            //Toast.makeText(getContext(),bahasaFrom,Toast.LENGTH_LONG).show();
            mendengarkan.setPreferredLanguage("id-ID");
        }else {
            mendengarkan.setPreferredLanguage("en-US");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        terlarangDengar();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        terlarangDengar();
    }
}
