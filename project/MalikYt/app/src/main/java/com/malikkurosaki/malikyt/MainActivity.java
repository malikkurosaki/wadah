package com.malikkurosaki.malikyt;

import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "->";

    private LinearLayout logContainer,inContainer1,inContainer2,inContainer3;
    private TextView log1,tx1,tx2,tx3;
    private EditText in1,in2,in3;
    private boolean mulai = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logContainer = findViewById(R.id.logContainer);
        inContainer1 = findViewById(R.id.inContainer1);
        inContainer2 = findViewById(R.id.inContainer2);
        inContainer3 = findViewById(R.id.inContainer3);
        log1 = findViewById(R.id.log1);
        in1 = findViewById(R.id.in1);
        in2 = findViewById(R.id.in2);
        in3 = findViewById(R.id.in3);


        mengetik("halo selamat datang ");
        mengetik("ini baru lagi tadinya ini adalah sesuatu yang bisa untuk berpindah");
    }


    private void mengetik(final String text){
        final TextView textView1 = new TextView(this);
        textView1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        textView1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        logContainer.addView(textView1);
        final String[] pem = text.split("(?!^)");
        final int[] speed = {70};
        final int[] key = {0};
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (key[0] < pem.length){
                    if (pem[key[0]] != null){
                        textView1.append(pem[key[0]]);
                        key[0]++;
                    }
                }

                handler.postDelayed(this, speed[0]);
            }

        }, speed[0]);

    }


}
