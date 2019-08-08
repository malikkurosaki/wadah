package com.malikkurosaki.bestschedule;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class KirimBerita  extends AsyncTask<Void,Void,Void> {

    private String judul,isi;
    KirimBerita(String judul1,String isi1) {
        this.judul = judul1;
        this.isi = isi1;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        try {

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization","key=AIzaSyDAnCBDEUpYki0sOaDk7I4kzYM_djNRyfA");
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();

            json.put("to","/topics/macan");


            JSONObject info = new JSONObject();
            info.put("title", judul);   // Notification title
            info.put("body", isi); // Notification body

            json.put("notification", info);

            JSONObject datanya = new JSONObject();
            datanya.put("body",judul);
            datanya.put("title",isi);

            json.put("data",datanya);

            Log.d("JSON_TO_SEND","jsonnya adalah = "+json);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();

        }
        catch (Exception e)
        {
            Log.d("Error",""+e);
        }

        return null;
    }
}

