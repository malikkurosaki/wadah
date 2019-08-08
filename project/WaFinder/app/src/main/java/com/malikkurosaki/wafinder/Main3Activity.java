package com.malikkurosaki.wafinder;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.miguelgaeta.media_picker.MediaPicker;
import com.miguelgaeta.media_picker.RequestType;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener,MediaPicker.Provider,MediaPicker.OnError {

    private Button choose,removeInject,filterWa;
    private EditText showNumber;
    private Button inject;
    private ProgressBar loading;
    private int BACA_EXTERNAL = 822;

    private String[] dataNomor;
    private int tmpName;
    private int BACA_CONTACT = 667;
    private boolean berhasil = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        init2();
        choose.setOnClickListener(this);
        inject.setOnClickListener(this);
        removeInject.setOnClickListener(this);
        filterWa.setOnClickListener(this);

        loading.setVisibility(View.GONE);
    }

    public void init2(){
        choose = findViewById(R.id.choose);
        showNumber = findViewById(R.id.showNumber);
        inject = findViewById(R.id.inject);
        loading = findViewById(R.id.loading);
        removeInject = findViewById(R.id.removeInject);
        filterWa = findViewById(R.id.filterWa);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose:
                mediaPicker();
                break;
            case R.id.inject:
                injectNumber();
                break;
            case R.id.removeInject:
                removeContact();
                break;
            case R.id.filterWa:
                startActivity(new Intent(Main3Activity.this,Main4Activity.class));
                break;
        }
    }

    public void mediaPicker(){
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},BACA_EXTERNAL);
            Toast.makeText(getContext(),"request read external permission",Toast.LENGTH_LONG).show();
            return;
        }
        MediaPicker.startForDocuments(this,this);
        loading.setVisibility(View.VISIBLE);
    }

    public void injectNumber(){
        loading.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS},BACA_CONTACT);
            Toast.makeText(getContext(),"request permission",Toast.LENGTH_LONG).show();
            return;
        }
        if (dataNomor == null){
            Toast.makeText(getContext(),"generate number first or input number",Toast.LENGTH_LONG).show();
            return;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (String nom : dataNomor){
                    tmpName++;
                    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                    int rawContactInsertIndex = ops.size();

                    ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
                    ops.add(ContentProviderOperation
                            .newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactInsertIndex)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "bestwahunter"+tmpName) // Name of the person
                            .build());
                    ops.add(ContentProviderOperation
                            .newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(
                                    ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,"+"+nom) // Number of the person
                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); // Type of mobile number

                    try {
                        ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                        Log.i("->", "injectNumber: success inject number");
                        berhasil = true;
                        loading.setVisibility(View.GONE);
                    } catch (RemoteException | OperationApplicationException e){
                        Toast.makeText(getContext(),"inject filed",Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.GONE);
                    }finally {
                        loading.setVisibility(View.GONE);
                    }
                }
            }
        },500);

    }

    public void removeContact(){
        ContentResolver contentResolverHapus = Main3Activity.this.getContentResolver();
        Cursor cursorHapus = contentResolverHapus.query(ContactsContract.Data.CONTENT_URI,null,null,null,null);

        try {
            while (cursorHapus.moveToNext()){
                String namaHapus = cursorHapus.getString(cursorHapus.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (namaHapus!=null){
                    if (namaHapus.contains("bestwahunter")){
                        String key = cursorHapus.getString(cursorHapus.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI,key);
                        int penghapusan = Main3Activity.this.getContentResolver().delete(uri,null,null);
                        if (penghapusan == 0){
                            berhasil = true;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getContext(),"remove filed",Toast.LENGTH_LONG).show();
        }finally {
            if (cursorHapus!=null){
                cursorHapus.close();
                if (berhasil){
                    Toast.makeText(getContext(),"remove success",Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    @Override
    public void onError(IOException e) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public File getImageFile() {
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MediaPicker.handleActivityResult(Main3Activity.this, requestCode, resultCode, data, new MediaPicker.OnResult() {
            @Override
            public void onSuccess(Uri uri, RequestType request) {
                File file = new File(uri.getPath());

                try (Scanner scanner = new Scanner(new File("file.csv"))) {
                    ArrayList<String> pokemon = new ArrayList<>();
                    while (scanner.hasNextLine()) {
                        pokemon.add(scanner.nextLine().split(",")[1]);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }finally {

                }







                /*FileInputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                try {
                   while ((line = bufferedReader.readLine()) != null){
                       stringBuilder.append(line);
                   }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        bufferedReader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }finally {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Gson gson = new Gson();
                dataNomor = gson.fromJson(stringBuilder.toString(),String[].class);
                showNumber.setText(Arrays.toString(dataNomor));
                loading.setVisibility(View.GONE);
*/

            }

            @Override
            public void onCancelled() {
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onError(IOException e) {
                loading.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BACA_EXTERNAL){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getContext(),"permission read external granted",Toast.LENGTH_LONG).show();
            }
        }
    }
}