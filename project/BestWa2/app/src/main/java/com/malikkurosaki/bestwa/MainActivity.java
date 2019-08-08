package com.malikkurosaki.bestwa;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGenerate,btnFilter,btnSend;

    private PermissionListener permissionListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {

            }
        };
        TedPermission.with(this).setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setDeniedCloseButtonText("aplikasi tidak diberikan inin, anda bisa memberinya secara manual di setting > app download > permission")
                .check();

        btnGenerate = findViewById(R.id.btnGenerate);
        btnFilter = findViewById(R.id.btnFilter);
        btnSend = findViewById(R.id.btnSend);

        btnGenerate.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        btnSend.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGenerate:
                startActivity(new Intent(this,Main2Activity.class));
                break;
            case R.id.btnFilter:
                startActivity(new Intent(this,Main3Activity.class));
                break;
            case R.id.btnSend:
                startActivity(new Intent(this,Main4Activity.class));
                break;
        }
    }
}
