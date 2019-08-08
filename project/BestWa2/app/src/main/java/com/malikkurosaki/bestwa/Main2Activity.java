package com.malikkurosaki.bestwa;

import android.Manifest;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        String bestwa = Environment.getExternalStorageDirectory().getAbsolutePath()+"/bestwa/generate";
        File bestwaF = new File(bestwa);
        if(!bestwaF.exists()){
            if (bestwaF.mkdirs()){
                Toast.makeText(getApplicationContext(),"folder telah dibuat > bestwa",Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onClick(View v) {

    }



    public void toas(String tx){
        Toast.makeText(getApplicationContext(),tx,Toast.LENGTH_LONG).show();
    }
}
