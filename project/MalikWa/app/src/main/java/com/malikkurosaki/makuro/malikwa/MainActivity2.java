package com.malikkurosaki.makuro.malikwa;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity2 extends AppCompatActivity {

    private TextView log1,text1,text2;
    private LinearLayout inputContainer1,inputContainer2;
    private EditText input1,input2;

    private TextToSpeech m_TTS;

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        log1 = findViewById(R.id.log1);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        inputContainer1 = findViewById(R.id.inputContainer1);
        inputContainer2 = findViewById(R.id.inputContainer2);

        info("sealamat datang , silahkan masukkan perintah");
        info("contoh : malik ganteng");


        input1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    String kunci = input1.getText().toString().trim();
                    switch (kunci){
                        case "":
                            naik();
                            info("lah kok kosongan gan");
                            muncul1();
                            input1.setText("");
                            break;
                        case "malik":
                            speakText("halo apa kabarnya");
                            break;
                        case "mulai":

                            break;
                            default:
                                naik();
                                info("kata "+inpt1()+" belum ada gan");
                                muncul1();
                                input1.setText("");
                    }
                    return true;
                }
                return false;
            }
        });

        m_TTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                m_TTS.setLanguage(Locale.getDefault());
                m_TTS.setPitch(1.3f);
                m_TTS.setSpeechRate(1f);
            }
        });

    }

    public String tx1(){
        return text1.getText().toString().trim();
    }
    public String inpt1(){
        return input1.getText().toString().trim();
    }
    private void naik(){
        String sNaik = tx1()+inpt1();
        info(sNaik);
        hilang1();

    }
    private void hilang1(){
        inputContainer1.setVisibility(View.GONE);
    }
    private void muncul1(){
        inputContainer1.setVisibility(View.VISIBLE);
    }
    private void info(String tx){
        log1.append(tx + "\n");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity2.this,MainActivity.class));
        finish();
    }


    private void speakText(String text) {
        String state = Environment.getExternalStorageState();
        boolean mExternalStorageWriteable = false;
        boolean mExternalStorageAvailable = false;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;

        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/maliksp");
        dir.mkdirs();
        File file = new File(dir, "myData.mp3");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int test = m_TTS.synthesizeToFile((CharSequence) text, null, file, "tts");
            Toast.makeText(getApplicationContext(),""+test,Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(),"lolipop ++ only",Toast.LENGTH_LONG).show();
        }
    }

}

