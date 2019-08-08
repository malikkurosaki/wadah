package com.malikkurosaki.bestwahunter;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CountryCodePicker edtCode;
    private EditText number,total,info;
    private Button mulai;
    private ProgressBar loading;
    private TextView kenaikan;
    private Button cleanup;
    private ImageButton tombolInfo;
    private LinearLayout mainParent;

    private String sCode,sNumber,sTotal;
    private int CONTACT = 778;
    private int tmpName;
    private boolean berhasil = false;
    private boolean apakahAda = false;
    private String[] bahan2;
    private List<String> hasilFinal;
    private StringBuilder lognya;
    private Cursor cursorHapus;
    private  Context context;
    private boolean apakahSudahDihapus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        mulai.setOnClickListener(this);
        cleanup.setOnClickListener(this);
        tombolInfo.setOnClickListener(this);
        edtCode.setCountryForPhoneCode(62);
        loading.setVisibility(View.GONE);

        context = (Context)this;
        lognya = new StringBuilder();
        jangkauan();
        logInfo("for more information just hit info button on left bottom");

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        coba();
    }

    private void init(){
        edtCode = findViewById(R.id.edtCode);
        number = findViewById(R.id.number);
        total = findViewById(R.id.total);
        mulai = findViewById(R.id.mulai);
        loading = findViewById(R.id.loading);
        kenaikan = findViewById(R.id.kenaikan);
        cleanup = findViewById(R.id.cleanup);
        info = findViewById(R.id.info);
        tombolInfo = findViewById(R.id.tombolInfo);
        mainParent = findViewById(R.id.mainParent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mulai:
                coba();
                vMulai();
                break;
            case R.id.cleanup:
                coba();
                removeContact();
                break;
            case R.id.tombolInfo:
                startActivity(new Intent(this,Main2Activity.class));
                break;
            case R.id.mainParent:
                coba();
                break;
        }
    }

    public void logInfo(String texnya){
        String mytime = (DateFormat.format("hh-mm-ss", new java.util.Date()).toString());
        lognya.append(mytime).append(": ").append(texnya).append("\n");
        info.append(lognya.toString());
        lognya.delete(0,lognya.length());
    }
    public void vMulai(){
        loading.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS},CONTACT);
            //logInfo("give all permission to applition or give it manualy on setting");
            logInfo("permission request, give all permission or you can action manualy on phone setting > app > permission");
            loading.setVisibility(View.GONE);
            return;
        }

        sCode =  edtCode.getSelectedCountryCode();
        sNumber = number.getText().toString().trim();
        sTotal = total.getText().toString().trim();

        if (TextUtils.isEmpty(sCode) || TextUtils.isEmpty(sNumber) || TextUtils.isEmpty(sTotal)){
            //logInfo("completely all form perfecly");
            logInfo("fill up all form completely no empty");
            loading.setVisibility(View.GONE);
            return;
        }

        int batasan = Integer.valueOf(sTotal);
        if (batasan > 100){
            loading.setVisibility(View.GONE);
            Toast.makeText(this, "max 100 , upgrade to premium for unlimited value", Toast.LENGTH_SHORT).show();
            logInfo("max 100 , upgrade to premium for unlimited value");
            return;
        }
        logInfo("start > wait until prossess is completly");
        //logInfo("try generate number");
        long bahan = Long.valueOf(sCode+sNumber);
        long totalKenaikan = !sTotal.equals("")?Long.valueOf(sTotal):0;
        List<String> wadah = new ArrayList<>();
        for (int n = 0; n < totalKenaikan;n++){
            bahan++;
            wadah.add(String.valueOf(bahan));
        }
        bahan2 = wadah.toArray(new String[0]);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //logInfo("start > wait 5 second");
                List<String> datanya = new ArrayList<>();
                for (String nom : bahan2){
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
                        Log.i("-->", "run: "+res[0].toString());
                        loading.setVisibility(View.GONE);
                    } catch (RemoteException | OperationApplicationException e){
                        Toast.makeText(getApplicationContext(),"inject filed",Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.GONE);
                    }finally {
                        loading.setVisibility(View.GONE);
                        datanya.add("ada");

                    }
                }
                if (!datanya.isEmpty()){
                    //logInfo("go to nex prossess");
                    memfilter();
                    //logInfo("go to filtering prossess");
                }
            }
        },5000);


    }

    public void removeContact(){
        //logInfo("try clean up object");
        logInfo("try clean object garbage");
        loading.setVisibility(View.VISIBLE);
        apakahSudahDihapus = false;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ContentResolver resolverHapus = context.getContentResolver();
                cursorHapus = resolverHapus.query(ContactsContract.Data.CONTENT_URI,null,null,null,null);
                try {
                    while (cursorHapus.moveToNext()){
                        String namahapus = cursorHapus.getString(cursorHapus.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                        if(namahapus.contains("bestwahunter")){
                            String key = cursorHapus.getString(cursorHapus.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI,key);
                            context.getContentResolver().delete(uri, ContactsContract.RawContacts._ID + " >= ?", new String[]{"0"});
                            apakahSudahDihapus = true;
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                }finally {
                    loading.setVisibility(View.GONE);
                    cursorHapus.close();
                    if (apakahSudahDihapus){
                        logInfo("finish");
                    }else {
                        logInfo("no object to cleanup remove manualy");
                    }
                }

            }
        },1000);

    }

    public void jangkauan(){
        total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sNumber = number.getText().toString();
                sCode = edtCode.getSelectedCountryCode().trim();
                sTotal = total.getText().toString().trim();
                long iGabungan = !sNumber.equals("") ? Long.valueOf(sCode+sNumber) : 0;
                long iKenaikan = !sTotal.equals("")? Long.valueOf(sTotal):0;

                if (iKenaikan > 100){
                    Toast.makeText(getApplicationContext(),"max 100 , upgrade to premium to get unlimited value",Toast.LENGTH_LONG).show();
                    total.setText("");
                    return;
                }
                String jadinya = String.valueOf("start from : +"+iGabungan+" \nuntil : +"+(iGabungan+iKenaikan)+"\n press START button to find active WA user");
                kenaikan.setText(jadinya);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void memfilter(){
        logInfo("wait for 10 second , please anable full internet speed or use wifi");
        loading.setVisibility(View.VISIBLE);
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                hasilFinal = new ArrayList<>();
                //logInfo("giltering");
                ContentResolver resolver = MainActivity.this.getContentResolver();
                Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.NUMBER);
                try  {
                    while (cursor.moveToNext()) {
                        String nama = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                        if (nama != null) {
                            if (nama.contains("bestwahunter")) {
                                String mime = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
                                Log.i("->", "memfilter: " + mime);
                                if (mime != null) {
                                    if (mime.contains("whatsapp")) {
                                        String nomor = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                        String hasilNomor = nomor.replace("@s.whatsapp.net", "");
                                        hasilFinal.add(hasilNomor);
                                        apakahAda = true;
                                    }
                                }
                            }

                        }
                    }

                } catch (Exception ee) {
                    ee.printStackTrace();
                    apakahAda = false;
                    logInfo("no number detect check your country code or number > repeat 1");
                    kosongkan();
                    loading.setVisibility(View.GONE);
                } finally {
                    if (apakahAda) {
                        loading.setVisibility(View.GONE);
                        if (!hasilFinal.isEmpty()) {
                            logInfo("all prossess is completed ,\n CONGRATULATION you have " + hasilFinal.size() + " total friend");
                            logInfo("you will take up to wa automaticly about 3 second");
                            kosongkan();
                            Handler handler3 = new Handler();
                            handler3.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    PackageManager packageManager = MainActivity.this.getPackageManager();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    Toast.makeText(getApplicationContext(), "let see what you get > search by bestwahunter", Toast.LENGTH_LONG).show();
                                    try {
                                        String url = "https://api.whatsapp.com/send?phone=&text=" + URLEncoder.encode("halo", "UTF-8");
                                        i.setPackage("com.whatsapp");
                                        i.setData(Uri.parse(url));
                                        if (i.resolveActivity(packageManager) != null) {
                                            hasilFinal.clear();
                                            startActivity(i);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        hasilFinal.clear();
                                    }
                                }
                            }, 3000);

                        } else {
                            logInfo("no wa number detect > check wa or repeat generate");
                            Handler handler8 = new Handler();
                            handler8.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    PackageManager packageManager = MainActivity.this.getPackageManager();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    //Toast.makeText(getApplicationContext(),"menulcur",Toast.LENGTH_LONG).show();
                                    try {
                                        String url = "https://api.whatsapp.com/send?phone=&text=" + URLEncoder.encode("halo", "UTF-8");
                                        i.setPackage("com.whatsapp");
                                        i.setData(Uri.parse(url));
                                        if (i.resolveActivity(packageManager) != null) {
                                            hasilFinal.clear();
                                            startActivity(i);
                                        }
                                    } catch (Exception e) {
                                        hasilFinal.clear();
                                        e.printStackTrace();
                                    }
                                }
                            }, 3000);
                        }
                    } else {
                        logInfo("no number detect check your country code or your internet connection> repeat 2 try check manualy");
                        loading.setVisibility(View.GONE);
                        hasilFinal.clear();

                    }
                }
            }
        },10000);

    }

    public void kosongkan(){
        number.setText("");
        total.setText("");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACT){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                logInfo("permission has grandted , you can check manualy on phone setting > app > permission");
                vMulai();
            }
        }
    }

    public void coba(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

