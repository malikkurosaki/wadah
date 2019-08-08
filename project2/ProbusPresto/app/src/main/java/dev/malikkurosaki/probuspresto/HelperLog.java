package dev.malikkurosaki.probuspresto;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class HelperLog {

    private DatabaseReference db;
    private TextView log;

    HelperLog(DatabaseReference dbNya, TextView t){
        this.db = dbNya;
        this.log = t;
    }

    HelperLog(DatabaseReference dbNya){
        this.db = dbNya;
    }

    void catat(final String s){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        Map<String,Object> dataNya = new HashMap<>();
        dataNya.put("waktu",currentDateandTime);
        dataNya.put("log",s);
        db.child("mis").child("log").child(currentDateandTime).setValue(s).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("-->", "onSuccess: "+s);
                if (log != null){
                    log.append(s);
                }

            }
        });
    }
}
