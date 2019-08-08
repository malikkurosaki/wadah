package dev.malikkurosaki.probussystem;

import android.provider.ContactsContract;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class PojoGetCustomer {

    String nm_cus,kd_tamu,tgl_datang,tgl_berang,no_room,reg_stt,stt;

    public String getNm_cus() {
        return nm_cus;
    }

    public String getKd_tamu() {
        return kd_tamu;
    }

    public String getTgl_datang() {
        return tgl_datang;
    }

    public String getTgl_berang() {
        return tgl_berang;
    }

    public String getNo_room() {
        return no_room;
    }

    public String getReg_stt() {
        return reg_stt;
    }

    public String getStt() {
        return stt;
    }
}
