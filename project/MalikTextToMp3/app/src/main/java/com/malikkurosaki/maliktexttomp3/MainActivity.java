package com.malikkurosaki.maliktexttomp3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlay,btnStop,btnSave,btnSet;
    private EditText edtInputText,edtName,edtSpeed,edtRate;
    private ProgressBar loading;
    private ImageButton info;

    private TextToSpeech toSpeech;
    private int EXTERNAL = 778;

    private String datatxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        btnPlay.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnSet.setOnClickListener(this);
        info.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        loading.setVisibility(View.GONE);

    }

    public void init(){
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        btnSave = findViewById(R.id.btnSave);
        edtInputText = findViewById(R.id.edtInputText);
        edtName = findViewById(R.id.edtName);
        loading = findViewById(R.id.loading);
        btnSet = findViewById(R.id.btnSet);
        edtSpeed = findViewById(R.id.edtSpeed);
        edtRate = findViewById(R.id.edtRate);
        info = findViewById(R.id.info);

        toSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    toas("ready to speach");
                    toSpeech.setLanguage(Locale.getDefault());
                    toSpeech.setPitch(1.3f);
                    toSpeech.setSpeechRate(1f);
                }else {
                    toas("your language not support > set manualy on setting phone");
                }

            }
        });
    }

    public void bicara(String dataSpeach){
        toSpeech.speak(dataSpeach,TextToSpeech.QUEUE_FLUSH,Bundle.EMPTY,null);
    }

    public void toas(String tx){
        Toast.makeText(getApplicationContext(),tx,Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.info:
                toInfo();
                break;
            case R.id.btnSet:
                setPeach();
                break;
            case R.id.btnPlay:
                toPlay();
                break;
            case R.id.btnStop:
                toStop();
                break;
            case R.id.btnSave:
                toSave();
                break;
        }
    }

    public void setPeach(){
        String sp = edtSpeed.getText().toString().trim();
        String rt = edtRate.getText().toString().trim();

        if (sp.contains(",") || rt.contains(",")){
            toas("use ( . ) not ( , )");
            return;
        }

        float spf = !sp.equals("")?Float.valueOf(sp+"f"):1.3f;
        float rtf = !rt.equals("")?Float.valueOf(rt+"f"):1f;

        toSpeech.setPitch(rtf);
        toSpeech.setSpeechRate(spf);

        toas("speed set to : "+sp+" rate set to : "+rt);
    }

    public void toPlay(){
        datatxt = edtInputText.getText().toString().trim();
        if (TextUtils.isEmpty(datatxt)){
            toas("text must not empty, deal ??");
            return;
        }
        bicara(datatxt);
    }
    public void toStop(){
        toSpeech.stop();
    }
    public void toInfo(){
        startActivity(new Intent(this,Main2Activity.class));
    }

    public void toSave(){
        int permisi = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permisi != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},EXTERNAL);
            return;
        }

        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/maliktexttomp3/output");
        if (!dir.exists()){
            if (dir.mkdirs()){
                toas("folder created on > maliktexttomp3/output");
            }else {
                toas("filed create folder > create it manualy > maliktexttomp3/output");
            }

            return;
        }

        if (TextUtils.isEmpty(datatxt)){
            toas("insert text first");
            return;
        }

        String namanya = edtName.getText().toString().trim();
        if (TextUtils.isEmpty(namanya)){
            toas("give name first , deal ...?");
            return;
        }
        File file = new File(dir, namanya+".mp3");
        if (file.exists()){
            toas("name has taken > choose another one");
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int test = toSpeech.synthesizeToFile(datatxt, null, file, "tts");
            toas("file save perfectly check to folder maliktexttomp3 > output");
            edtInputText.setText("");
        }else {
            Toast.makeText(getApplicationContext(),"lolipop ++ only",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                toas("permision grandted > check manualy on setting phone > app instaled > permission");
            }
        }
    }
}
