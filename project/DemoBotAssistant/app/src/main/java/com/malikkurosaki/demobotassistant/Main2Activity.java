package com.malikkurosaki.demobotassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        db = FirebaseDatabase.getInstance().getReference();

        String[] nama = {
          "bagus","bagas","feri","gina","yanto","dilan","sugi","angga","paijo","tukimin",
          "ana","fitri","rangga","jiwo","sumiran","doyok","kadir","botak","fahmi","mahmut",
          "gareng","duwik","rafi","agus"
        };

        for (String nm : nama){
            Map<String,Object> dataKirim = new HashMap<>();
            dataKirim.put("nama",nm);
            dataKirim.put("alamat","denpasar");
            dataKirim.put("asal","indonesia");
            dataKirim.put("tinggi","170");
            dataKirim.put("umur","30");

            db.child("pelanggan").child(nm).setValue(dataKirim);
        }
    }
}
