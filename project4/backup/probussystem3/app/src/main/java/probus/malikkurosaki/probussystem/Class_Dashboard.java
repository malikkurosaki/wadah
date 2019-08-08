package probus.malikkurosaki.probussystem;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

class Class_Dashboard {

    private Context context;
    Class_Dashboard(Context context1){
        this.context = context1;
    }

    // mendapatkan kalendar
    String kalendar(){
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        return simpleDateFormat.format(calendar.getTime());
    }

    
}
