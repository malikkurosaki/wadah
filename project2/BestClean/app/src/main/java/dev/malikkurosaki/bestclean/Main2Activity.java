package dev.malikkurosaki.bestclean;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.specials.out.TakingOffAnimator;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Intent intent = getIntent();
        Map<String, Object> terimaAlamat = (HashMap<String,Object>)intent.getSerializableExtra("halaman");


        if(terimaAlamat.get("halaman").equals("login")){
            Fragment jemput = new FragmentLogin();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer1,jemput);
            transaction.commitAllowingStateLoss();
        }
        if(terimaAlamat.get("halaman").equals("jemput")){

            Bundle bundle = new Bundle();
            bundle.putSerializable("datanya", (Serializable) terimaAlamat);
            Fragment jemput = new FragmentJemputSaya();
            jemput.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer1,jemput);
            transaction.commitAllowingStateLoss();
        }

        /*byte[] byteArray = (byte[]) terimaAlamat.get("gambar");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);*/



    }
}
