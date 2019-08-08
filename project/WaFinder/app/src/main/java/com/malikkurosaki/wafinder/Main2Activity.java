package com.malikkurosaki.wafinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private CountryCodePicker edtCode;
    private EditText edtArea,edtNumber,edtJumblah,edtInfo2;
    private TextView txvInfo;
    private Button btnMulai,btnInject;
    private ProgressBar pgrsb;

    private String tw1,tw2,tw3,tw4,tl1,infonya2,infonya,namaFileTmp;
    private long ttl1,ttl2;
    private int IJIN_EXTERNAL = 778;

    private List<String> wadah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();

        btnMulai.setOnClickListener(this);
        btnInject.setOnClickListener(this);

        pgrsb.setVisibility(View.GONE);


        edtJumblah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                tw1 = edtCode.getSelectedCountryCode();
                tw2 = edtArea.getText().toString();
                tw3 = edtNumber.getText().toString();
                tw4 = edtJumblah.getText().toString();

                Toast.makeText(getApplicationContext(),tw1,Toast.LENGTH_LONG).show();

                tl1 = tw1+tw2+tw3;
                ttl1 = Long.parseLong(tl1);
                ttl2 = Long.parseLong(tw4);

                if (TextUtils.isEmpty(tl1)){
                   toas("please filld all form perfectly");
                   return;
                }
                infonya = "start from +"+tl1+" to +"+String.valueOf(ttl1+ttl2);
                txvInfo.setText(infonya);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void init(){
        edtCode = findViewById(R.id.edtCode);
        edtArea = findViewById(R.id.edtArea);
        edtNumber = findViewById(R.id.edtNumber);
        edtJumblah = findViewById(R.id.edtJumblah);
        txvInfo = findViewById(R.id.txvInfo);
        edtInfo2 = findViewById(R.id.edtInfo2);
        pgrsb = findViewById(R.id.pgrsb);
        btnMulai = findViewById(R.id.btnMulai);
        btnInject = findViewById(R.id.btnInject);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnMulai:
                vMulai();
                break;
            case R.id.btnInject:
                vInject();
                break;
        }
    }

    public void vMulai(){
        if (TextUtils.isEmpty(tl1)){
            toas("make all form is not empty");
            return;
        }

        wadah = new ArrayList<>();
        for (long n = 0;n<ttl2;n++){
            ttl1++;
            wadah.add(String.valueOf(ttl1));
        }

        infonya2 = Arrays.toString(wadah.toArray(new String[0]));
        edtInfo2.setText(infonya2);

    }

    public void vInject(){
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},IJIN_EXTERNAL);
            toas("request permission");
            return;
        }
        if (TextUtils.isEmpty(infonya2)){
            toas("please generate number first");
            return;
        }

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/wafinder/output/tmp");
        File fileTmp = new File(folder.toString(),infonya+".json");

        if(!folder.exists()){
            if (folder.mkdirs()){
                Toast.makeText(getApplicationContext(),"folder created /wafinder/output/tmp",Toast.LENGTH_LONG).show();
            }else {
                toas("fail create folder > create it manualy > /wafinder/output/tmp");
            }
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String textNya = gson.toJson(wadah);
        try {
            Writer writer = new BufferedWriter(new FileWriter(fileTmp));
            writer.write(textNya);
            writer.close();
            toas("success save to /wafinder/output/tmp");
        } catch (IOException e) {
            e.printStackTrace();
            toas("filed to save : "+e.toString());
        }


    }

    public void toas(String tx){
        Toast.makeText(getApplicationContext(),tx,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IJIN_EXTERNAL){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                toas("permission write externall granted");
            }
        }
    }
};