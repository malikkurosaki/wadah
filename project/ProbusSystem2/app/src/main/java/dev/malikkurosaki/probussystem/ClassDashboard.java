package dev.malikkurosaki.probussystem;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClassDashboard {


    private Context context;
    public ClassDashboard(Context context1){
        this.context = context1;
    }

    public String kalendar(){
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        return simpleDateFormat.format(calendar.getTime());
    }
}
