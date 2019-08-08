package dev.malikkurosaki.probussystem;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

class BantuanEvent {

    private KetikaMulaiMendengarkan ketikaMulaiMendengarkan;
    private Context context;

    BantuanEvent (Context contextNya){
        this.context = contextNya;
    }

    void setKetikaMulaiMendengarkan(KetikaMulaiMendengarkan ketikaMulaiMendengarkan) {
        this.ketikaMulaiMendengarkan = ketikaMulaiMendengarkan;
    }

    void addOnMendengarkan(){

    }

    public interface KetikaMulaiMendengarkan{
        void maka();
    }
}
