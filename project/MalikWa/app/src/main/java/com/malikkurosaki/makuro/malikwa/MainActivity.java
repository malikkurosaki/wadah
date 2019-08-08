package com.malikkurosaki.makuro.malikwa;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private String TAG = "-->";
    private int KODE_PERMISI = 556;

    private EditText nomorInput;
    private EditText totalInput;
    private Button tombolMulai;
    private TextView perlihatkanHasil;
    private Button tombolfilter;
    private TextView hasilFilter;
    private TextView totalHasil;
    private EditText log;
    private CheckBox hpParno;
    private Button hapusLog;
    private TextView judul;
    private Button kirim;

    private StringBuilder logBuilder;
    private StringBuilder hasilKelipatan;
    private List<String> wadahNomor;
    private List<String> hasilFinal;
    private boolean kosong = true;
    private int namaBelakang;
    private boolean apakahAda = false;


    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private FileOutputStream outputStream;
    private File folderOutput;
    private File namaOutputFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //deklarasi view class
        nomorInput = findViewById(R.id.nomorInput);
        totalInput = findViewById(R.id.totalInput);
        tombolMulai = findViewById(R.id.tombolMulai);
        perlihatkanHasil = findViewById(R.id.perlihatkanHasil);
        tombolfilter = findViewById(R.id.tombolfilter);
        hasilFilter = findViewById(R.id.hasilFilter);
        totalHasil = findViewById(R.id.totalHasil);
        log = findViewById(R.id.log);
        hpParno = findViewById(R.id.hpParno);
        hapusLog = findViewById(R.id.hapusLog);
        judul = findViewById(R.id.judul);
        kirim = findViewById(R.id.kirim);

        logBuilder = new StringBuilder();
        hasilKelipatan = new StringBuilder();
        wadahNomor = new ArrayList<>();
        hasilFinal = new ArrayList<>();
        namaBelakang = 0;

        int bacaContact = ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS);
        int tulisContact = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS);
        int tulisSdCard = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int bacaSdCard = ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
        int bacaTelpon = ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE);

        if (bacaContact != PackageManager.PERMISSION_GRANTED || tulisContact != PackageManager.PERMISSION_GRANTED || tulisSdCard != PackageManager.PERMISSION_GRANTED || bacaSdCard != PackageManager.PERMISSION_GRANTED || bacaTelpon != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},KODE_PERMISI);
            return;
        }

        log.setMovementMethod(new ScrollingMovementMethod());

        if (adaFolderOutput()){
            logCatatan("cek folder : folder ada");
        }else {
            logCatatan("folder tidak ada terjadi kesalahan");
        }

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivity3.class));
            }
        });

        judul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivity2.class));
            }
        });

        hpParno.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putString("parno","ya");
                    editor.apply();
                    logCatatan("saya mengakui hape saya parno an gk jelas");
                }else {
                    editor.putString("parno","tidak");
                    editor.apply();
                    hapusNomor();
                }
            }
        });

        if (pref.getString("parno",null) != null){
            String dapatkanParno = pref.getString("parno",null);
            if (dapatkanParno.contains("ya")){
                hpParno.setChecked(true);
                logCatatan("menerapkan setting untuk hp parno an");
            }else {
                logCatatan("menerapkan setting hp tidak parno");
                try {
                    hapusNomor();
                }catch (Exception e){
                    e.printStackTrace();
                    logCatatan("masalah penghapusan kontak: "+String.valueOf(e));
                }
                hpParno.setChecked(false);

            }
        }else {
            hpParno.setChecked(false);
            logCatatan("menerapkan setting hp tidak parno");
        }

        hapusLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.setText("");
            }
        });

        logCatatan("selamat datang , berikan ijin aplikasi untuk membaca contact dan external storage, jika tidak aplikasi akan memutup dengan sendirinya");
        tombolMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perlihatkanHasil.setText("Loading ...");
                logCatatan("mulai ");
                logCatatan("Loading ... ");
                if (adaFolderOutput()){
                    logCatatan("diproses");
                }

                Handler handlerMulai = new Handler();
                handlerMulai.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String nomorNya = nomorInput.getText().toString().trim();
                        String totalnya = totalInput.getText().toString().trim();
                        logCatatan("memproduksi nomor");
                        if (TextUtils.isEmpty(nomorNya) || TextUtils.isEmpty(totalnya)){
                            logCatatan("nomor dan jumblah total tidak boleh kosong");
                            perlihatkanHasil.setText("HASIL");
                            return;
                        }
                        long nomor2 = Long.parseLong(nomorNya);
                        int total2 = Integer.parseInt(totalnya);
                        for (int n = 0;n < total2;n++ ){
                            nomor2++;
                            namaBelakang++;
                            wadahNomor.add(String.valueOf(nomor2));
                            kosong = false;
                        }
                        if (!kosong){
                            if (!wadahNomor.isEmpty()){
                                String[] nomor3 = wadahNomor.toArray(new String[0]);
                                logCatatan("nomor kelipatan didapatkan");

                                for (String nomor4 : nomor3){
                                    namaBelakang++;
                                    hasilKelipatan.append(nomor4).append(",");
                                    logCatatan("mencoba inject nomor");
                                    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                                    int rawContactInsertIndex = ops.size();

                                    ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
                                    ops.add(ContentProviderOperation
                                            .newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactInsertIndex)
                                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "malikwa"+namaBelakang) // Name of the person
                                            .build());
                                    ops.add(ContentProviderOperation
                                            .newInsert(ContactsContract.Data.CONTENT_URI)
                                            .withValueBackReference(
                                                    ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
                                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,"+"+nomor4) // Number of the person
                                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); // Type of mobile number

                                    try {
                                        ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                                        logCatatan("proses inject berhasil");
                                    } catch (RemoteException | OperationApplicationException e){
                                        logCatatan("proses inject gagal");
                                    }
                                }

                                perlihatkanHasil.setText(hasilKelipatan.toString());
                                logCatatan("proses inject berhasil tunggu 1 atau 2 menit mesin menghubungi kantor WA terdekat pastikan internet anda super kencang");


                            }
                        }
                    }
                },500);

            }
        });

        tombolfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logCatatan("mencoba memfilter nomer wa");
                hasilFilter.setText("Loading ...");
                logCatatan("mulai Filter");
                logCatatan("Loading ...");
                Handler handlerFilter = new Handler();
                handlerFilter.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ContentResolver resolver = MainActivity.this.getContentResolver();
                        Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI,null,null,null,null);

                        try{
                            while (cursor.moveToNext()){
                                String nama = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                if (nama!=null){
                                    if (nama.contains("malikwa")){
                                        String mime = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
                                        if (mime!=null){
                                            logCatatan("mencoba terhubung keserver WA");
                                            if (mime.contains("what")){
                                                String nomor = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                                String hasilNomor = nomor.replace("@s.whatsapp.net","");
                                                logCatatan("server WA menjawab > "+ hasilNomor);
                                                hasilFinal.add(hasilNomor);
                                                apakahAda = true;

                                            }
                                        }
                                    }
                                }
                            }

                        }catch (Exception ee){
                            ee.printStackTrace();
                            logCatatan("terjadi kesalahan pembacaan contact"+ee);
                            apakahAda = false;
                        }finally {

                            if (cursor!=null){
                                cursor.close();
                                if (apakahAda){
                                    HashSet<String> kembar = new HashSet<>();
                                    kembar.addAll(hasilFinal);
                                    hasilFinal.clear();
                                    hasilFinal.addAll(kembar);
                                    String[] finalnomorwa = hasilFinal.toArray(new String[0]);
                                    StringBuilder builderwa = new StringBuilder();
                                    logCatatan("memberi tip keapada mas penjaga server wa");

                                    for (String finalwa : finalnomorwa){
                                        builderwa.append(finalwa).append(",");

                                    }
                                    hasilFilter.setText(builderwa.toString());
                                    String infoTotal = "total nomor WA yang didapatkan : "+hasilFinal.size();
                                    totalHasil.setText(infoTotal);


                                    String dataTulis = new Gson().toJson(hasilFinal);
                                    try {
                                        outputStream = new FileOutputStream(namaOutputFile.toString());
                                        outputStream.write(dataTulis.getBytes());
                                        outputStream.close();
                                        logCatatan(infoTotal);
                                        logCatatan("pemfilteran selesai,data tersimpan pada folder < malikwa/malikwa.json >");
                                        logCatatan("untuk filter tergantung dari koneksi internet , anda bisa mengulangi memfilter hingga mendapatkan hasil sesuai yang diinginkan");

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        logCatatan("ada masalah saat penyimpanan : "+String.valueOf(e));
                                    }


                                }else {
                                    hasilFilter.setText("ZONK lihat log");
                                    logCatatan("nomor wa tidak terditeksi, coba nomor yang lain , pastikan anda mempunyai internet super kencang");
                                }

                            }
                        }
                    }
                },5000);

            }
        });


    }

    private boolean adaFolderOutput(){
        logCatatan("memcoba membuat folder output");
        folderOutput = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"malikwa/output");
        namaOutputFile = new File(folderOutput.toString(),"/malikwa.json");
        if (!folderOutput.exists()){
            try {
                boolean buatFolder1 = folderOutput.mkdirs();
                if (buatFolder1){
                    logCatatan("folder telah dibuat < malikwa >");
                    return true;
                }else {
                    logCatatan("gagal membuatkamn folder");
                    return false;
                }
            }catch (Exception ee){
                ee.printStackTrace();
                logCatatan("ada kesalahan saat pembuatan folder"+String.valueOf(ee));
                return false;
            }
        }else {
            logCatatan("cek folder : tidak ada");
            return true;
        }
    }

    private void hapusNomor(){
        ContentResolver contentResolverHapus = MainActivity.this.getContentResolver();
        Cursor cursorHapus = contentResolverHapus.query(ContactsContract.Data.CONTENT_URI,null,null,null,null);
        logCatatan("mempersiapkan ruang kerja");
        logCatatan("untuk penggua xiaomi dan sejenisnya , jika ada peringatan muntul tidak dapat menghapus contact, anda bisa menghapusnya secara manual , setting > appsistem > contak > pilih hapus secara bersamaan < kata kunci _malikwa_ ");

        try {
            while (cursorHapus.moveToNext()){
                String namaHapus = cursorHapus.getString(cursorHapus.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (namaHapus!=null){
                    if (namaHapus.contains("malikwa")){
                        String key = cursorHapus.getString(cursorHapus.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI,key);
                        int penghapusan = MainActivity.this.getContentResolver().delete(uri,null,null);
                        Log.i(TAG, "hapusNomor: penghapusan -->" + penghapusan);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logCatatan("terjadi masalah"+e);
        }finally {
            if (cursorHapus!=null){
                cursorHapus.close();
                logCatatan("ruang kerja telah siap");
            }
        }
    }

    private void logCatatan(String catatan){
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        logBuilder.append(mydate).append(" : ").append(catatan).append("...").append("\n");
        log.append(logBuilder);
        logBuilder.setLength(0);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == KODE_PERMISI){
            logCatatan("anda telah mengijinkan aplikasi , semoga harianda menyenangkan");
            finish();
            startActivity(getIntent());
        }
    }
}
