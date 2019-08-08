package malikkurosakistudio.maliksmsbom;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText malikNumber;
    private EditText malikPesan;
    private EditText malikinfo;
    private Button malikkirim;

    private String[] nomornya;
    private String pesannya;
    private int IJIN_SMS = 445;

    private int kepala = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        malikNumber = findViewById(R.id.maliknumber);
        malikinfo = findViewById(R.id.malikinfo);
        malikPesan = findViewById(R.id.malikpesan);
        malikkirim = findViewById(R.id.malikkirim);

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS,Manifest.permission.BROADCAST_SMS},IJIN_SMS);
            return;
        }

        nginfo("selamat datang");
        nginfo("isikan nomor");
        nginfo("isikan pesan");
        nginfo("tekan kirim");
        malikkirim.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.malikkirim:
                if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS,Manifest.permission.BROADCAST_SMS},IJIN_SMS);
                    return;
                }
                nginfo("persiapan mengirim");
                cobaNgirim();
                break;
        }
    }

    public void cobaNgirim(){
        nomornya = malikNumber.getText().toString().trim().split("\n");
        pesannya = malikPesan.getText().toString().trim();

        if (TextUtils.isEmpty(malikNumber.getText().toString().trim())){
            nginfo("nomor gak bole kosong");
            return;
        }
        if (TextUtils.isEmpty(pesannya)){
            nginfo("pesan gak bole kosong");
            return;
        }

        kepala = 0;
        malikkirim.setVisibility(View.GONE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (kepala < nomornya.length){
                    if (nomornya[kepala] != null){
                        sendSMS("+"+nomornya[kepala],pesannya,"kirim ke : "+nomornya[kepala]);
                        handler.postDelayed(this,3000);
                        kepala++;
                    }else {
                        handler.removeCallbacks(this);
                        nginfo("pesan telah dikirim semua");
                        malikNumber.setText("");
                        malikkirim.setVisibility(View.VISIBLE);
                    }
                }else {
                    handler.removeCallbacks(this);
                    nginfo("pesan uda terkirim bos");
                    malikNumber.setText("");
                    malikkirim.setVisibility(View.VISIBLE);
                }

            }
        },3000);
    }

    public void sendSMS(String phoneNo, String msg,String kiriman) {
        String SMS_DELIVERED = "SMS_DELIVERED";
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String state = null;
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        state = "SMS delivered";
                        break;
                    case Activity.RESULT_CANCELED:
                        state = "SMS not delivered";
                        break;
                }
                nginfo(state);
            }
        }, new IntentFilter(SMS_DELIVERED));

        SmsManager smsManager = SmsManager.getDefault();
        String SMS_SENT = "SMS_SENT";
        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);
        try {
            smsManager.sendTextMessage(phoneNo, null, msg, sentPendingIntent, deliveredPendingIntent);
            nginfo(kiriman);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

    }

    public void nginfo(String tx){
        malikinfo.append("=> "+tx+"\n");
        malikinfo.setSelection(malikinfo.getText().length());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IJIN_SMS){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                nginfo("ijin telah diberikan");
                cobaNgirim();
            }
        }
    }
}
