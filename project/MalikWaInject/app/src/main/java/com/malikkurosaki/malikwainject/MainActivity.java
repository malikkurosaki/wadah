package com.malikkurosaki.malikwainject;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText malikBahan;
    private Button malikInject;
    private EditText malikNama;

    private String bahannya;
    private String namanya;
    private String[] bahannya2;

    private int tmpName;
    private int IJIN_CONTAC = 990;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        malikBahan = findViewById(R.id.malikBahan);
        malikInject = findViewById(R.id.malikInject);
        malikNama = findViewById(R.id.malikNama);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS},IJIN_CONTAC);
        }

        malikInject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injectKontak();

            }

        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IJIN_CONTAC){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(),"ijin telah diberikan , silahkan lanjutkan",Toast.LENGTH_LONG).show();
                injectKontak();
            }
        }
    }

    public void injectKontak(){
        tmpName = 0;
        bahannya = malikBahan.getText().toString().trim();
        namanya = malikNama.getText().toString().trim();

        if (TextUtils.isEmpty(namanya)){
            Toast.makeText(getApplicationContext(),"nama gak bole kosong",Toast.LENGTH_LONG).show();
            return;
        }
        if (namanya.contains(" ")){
            Toast.makeText(getApplicationContext(),"nama tanpa spasi",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(bahannya)){
            Toast.makeText(getApplicationContext(),"gak bole kosong coy",Toast.LENGTH_LONG).show();
            return;
        }

        bahannya2 = bahannya.split("\n");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"mulai inject contak , sabar nunggu sampe selese",Toast.LENGTH_SHORT).show();

                List<String> datanya = new ArrayList<>();
                for (String nom : bahannya2){
                    tmpName++;
                    ArrayList<ContentProviderOperation> ops = new ArrayList<>();
                    int rawContactInsertIndex = ops.size();

                    ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
                    ops.add(ContentProviderOperation
                            .newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactInsertIndex)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, namanya+""+tmpName) // Name of the person
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
                    } catch (RemoteException | OperationApplicationException e){
                        Toast.makeText(getApplicationContext(),"inject filed",Toast.LENGTH_LONG).show();

                    }finally {
                        datanya.add("ada");
                        Toast.makeText(getApplicationContext(),"selesai",Toast.LENGTH_LONG).show();
                        malikBahan.setText(Html.fromHtml("<h1>selesai coy</h1>"));
                    }
                }
            }
        },500);

    }
}
