package dev.malikkurosaki.crispypizza;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //container
    @BindView(R.id.conSignin) LinearLayout conSignin;
    @BindView(R.id.conSignup) LinearLayout conSignup;
    @BindView(R.id.conSigninInfo) LinearLayout conSigninInfo;

    //edittext
    @BindView(R.id.signinEmail) EditText signinEmail;
    @BindView(R.id.signinPass) EditText signinPass;
    @BindView(R.id.signupCode) EditText signupCode;

    //textview
    @BindView(R.id.signinInfo) TextView signinInfo;
    @BindView(R.id.signupTime) TextView signupTime;

    //button
    @BindView(R.id.signupCodeBtn) Button signupCodeBtn;
    @BindView(R.id.signupBtn) Button signupBtn;
    @BindView(R.id.signinBtn) Button signinBtn;

    @BindView(R.id.swicher)
    ViewSwitcher switcher;

    private String TAG = "-->";

    private String namanya,emailnya;
    private boolean peringatan = true;

    private String nem;
    private String em;

    private int hitungan = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(MainActivity.this,Main2Activity.class));

        TinyDB tinyDB = new TinyDB(this);

        tinyDB.putString("nama","malik");


        ButterKnife.bind(this);

        conSignup.setVisibility(View.GONE);
        signinInfo.setVisibility(View.GONE);
        switcher.setDisplayedChild(0);


        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nem = signinEmail.getText().toString().trim();
                em = signinPass.getText().toString().trim();
                peringatan = true;

                if (TextUtils.isEmpty(nem) || TextUtils.isEmpty(em)){
                    Toasty.warning(getApplicationContext(),"email or pass not be empty",Toasty.LENGTH_LONG).show();
                    return;
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.192.192")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
                Call<List<JsonObject>> panggil = retrofitApi.getNama(nem);
                panggil.enqueue(new Callback<List<JsonObject>>() {
                    @Override
                    public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                        for (JsonObject hasil : response.body()){
                            namanya = hasil.get("nm_cus").getAsString().trim();
                            emailnya = hasil.get("email").getAsString().trim();

                            if (nem.equals(namanya.toLowerCase()) && em.equals(emailnya.toLowerCase())){
                                Toasty.info(getApplicationContext(),"benar",Toasty.LENGTH_LONG).show();

                                TinyDB login = new TinyDB(MainActivity.this);
                                //login.putBoolean("login",true);
                            }else {
                                if (peringatan){
                                    Toasty.error(getApplicationContext(),"your username or email not match",Toasty.LENGTH_LONG).show();
                                    peringatan = false;
                                }
                                conSignup.setVisibility(View.VISIBLE);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<List<JsonObject>> call, Throwable t) {

                    }
                });


            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.setDisplayedChild(1);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (hitungan > 0 ){
                            hitungan --;
                            signupTime.setText(String.valueOf("00:"+hitungan));
                            handler.postDelayed(this,1000);
                        }else {
                            handler.removeCallbacks(this);
                            hitungan = 50;
                        }
                    }
                },1000);
            }
        });



    }

    @Override
    public void onBackPressed() {
        switcher.setDisplayedChild(0);
        hitungan = 0;
        super.onBackPressed();
    }
}
