package com.malikkurosaki.malikcarinomerwa;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Scanner;

public class Fragment1 extends Fragment {

    OnbukaFragment1 onbukaFragment1;

    private EditText malikedit;
    private int jalan = 0;
    private String data;
    private String[] data2;
    private Spannable spanTex;

    public void setOnbukaFragment1(Activity activity){
        onbukaFragment1 = (OnbukaFragment1) activity;
    }

    public interface OnbukaFragment1{
        public void onbuka(String tx);
    }

    private Context context;
    private Activity activity;
    public static Fragment1 newInstance(){
        return new Fragment1();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = (Activity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_fragment1,container,false);
            malikedit = view.findViewById(R.id.malikedit);
            data  = malikedit.getText().toString().trim();
            data2  = data.split(" ");


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (jalan < data2.length){
                        if (data2[jalan] != null){
                            data2[jalan] = "<font color='red' width='30px'>"+data2[jalan]+"</font>";
                            StringBuilder builder = new StringBuilder();
                            for (String data3 : data2){
                                builder.append(data3).append(" ");
                            }
                            malikedit.setText(Html.fromHtml(builder.toString()));

                            jalan++;
                            handler.postDelayed(this,1000);
                        }else {
                            handler.removeCallbacks(this);
                            Toast.makeText(getContext(),"hore",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        handler.removeCallbacks(this);
                        Toast.makeText(getContext(),"hore 2",Toast.LENGTH_LONG).show();
                    }


                }
            },1000);


        return view;
    }

    public void setHighLightedText(TextView tv, String textToHighlight) {
        String tvt = tv.getText().toString();
        int ofe = tvt.indexOf(textToHighlight, 0);
        Spannable wordToSpan = new SpannableString(tv.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1)
                break;
            else {
                wordToSpan.setSpan(new BackgroundColorSpan(0xFFFFFF00), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }
}
