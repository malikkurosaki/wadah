package com.malikkurosaki.wafinder;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Main4Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView toShow;
    private Button toFilter;
    private Button toSave;
    private boolean apakahAda = false;

    private List<String> hasilFinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        init3();
        toFilter.setOnClickListener(this);

    }

    public void init3(){
        toShow = findViewById(R.id.toShow);
        toFilter = findViewById(R.id.toFilter);
        toSave = findViewById(R.id.toSave);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toFilter:
                memfilter();
                break;
        }
    }


    public void memfilter(){
        hasilFinal = new ArrayList<>();
        Toast.makeText(getApplicationContext(),"filtering",Toast.LENGTH_LONG).show();
        ContentResolver resolver = Main4Activity.this.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI,null,null,null,ContactsContract.CommonDataKinds.Phone.NUMBER);
        try{
            while (cursor.moveToNext()){
                String nama = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (nama!=null){
                    if (nama.contains("bestwahunter")){
                        String mime = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.Data.MIMETYPE));
                        Log.i("->", "memfilter: "+mime);
                        if (mime!=null){
                            if (mime.contains("what")){
                                String nomor = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                String hasilNomor = nomor.replace("@s.whatsapp.net","");
                                hasilFinal.add(hasilNomor);
                                apakahAda = true;

                            }
                        }
                    }
                }
            }

        }catch (Exception ee){
            ee.printStackTrace();
            apakahAda = false;
            Toast.makeText(getApplicationContext(),"no number",Toast.LENGTH_LONG).show();
        }finally {
            if (cursor != null) {
                cursor.close();
            }
            if (apakahAda){
                if (!hasilFinal.isEmpty()){
                    PackageManager packageManager = Main4Activity.this.getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    Toast.makeText(getApplicationContext(),"menulcur",Toast.LENGTH_LONG).show();
                    try {
                        String url = "https://api.whatsapp.com/send?phone=&text=" + URLEncoder.encode("halo", "UTF-8");
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        if (i.resolveActivity(packageManager) != null) {
                            startActivity(i);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }else {
                Toast.makeText(getApplicationContext(),"gk ada",Toast.LENGTH_LONG).show();
            }
        }
    }
}
