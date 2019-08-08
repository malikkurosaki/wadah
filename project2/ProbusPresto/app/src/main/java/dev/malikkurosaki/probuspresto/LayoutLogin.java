package dev.malikkurosaki.probuspresto;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class LayoutLogin extends Fragment {

    private Context context;
    private Activity activity;
    private View view;

    private EditText loginNomor;
    private Button loginMasuk;
    private EditText codeSms;
    private Button codeCocok;

    private String ferivicationId;
    private PhoneAuthProvider.ForceResendingToken tokenNya;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private LinearLayout nomerContainer;
    private LinearLayout codeContainer;

    private ProgressBar loginLoading;
    private TextView loginKeterangan;

    private String TAG = "-->";
    private String ket;
    private int hitung = 50;
    private boolean boleHitung = true;

    LayoutLogin newInstance(){
        return new LayoutLogin();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_login,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        this.context = view.getContext();
        this.activity = (Activity)context;

        loginNomor = view.findViewById(R.id.loginNomor);
        loginMasuk = view.findViewById(R.id.loginMasuk);
        codeSms = view.findViewById(R.id.codeSms);
        codeCocok = view.findViewById(R.id.codeCocokan);
        nomerContainer = view.findViewById(R.id.nomerContainer);
        codeContainer = view.findViewById(R.id.codeContainer);
        loginLoading = view.findViewById(R.id.loginLoading);
        loginKeterangan = view.findViewById(R.id.loginKeterangan);

        loginLoading.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        codeContainer.setVisibility(View.GONE);
        loginMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomerNya = loginNomor.getText().toString().trim();
                Log.i(TAG, "onClick: login clickked");

                loginLoading.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(nomerNya)){
                    loginLoading.setVisibility(View.GONE);
                    ket = "nomer gak bole kosong";
                    loginKeterangan.append(ket);
                    return;
                }

                ket = "the number is"+nomerNya;
                Log.i(TAG, "onClick: "+nomerNya);
                loginKeterangan.append(ket);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        nomerNya,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        activity,               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks
                Log.i(TAG, "onClick: ke callbak");
            }
        });

    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.i(TAG, "onVerificationCompleted: proses callback");

            final String codenya = phoneAuthCredential.getSmsCode();
            codeContainer.setVisibility(View.VISIBLE);
            nomerContainer.setVisibility(View.GONE);

            Log.i(TAG, "onVerificationCompleted: "+codenya);

            loginLoading.setVisibility(View.GONE);

            codeCocok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginLoading.setVisibility(View.VISIBLE);
                    String smsCode = codeSms.getText().toString().trim();
                    boleHitung = false;

                    if (TextUtils.isEmpty(smsCode)){
                        Toast.makeText(getContext(),"code gk bole kosong",Toast.LENGTH_LONG).show();
                        loginLoading.setVisibility(View.GONE);
                        loginKeterangan.append("gak bole kosong");
                        return;
                    }
                    if (!codenya.equals(smsCode)){
                        Toast.makeText(getContext(),"kode tidak cocok",Toast.LENGTH_LONG).show();
                        loginKeterangan.append("kodenya gk cocok loh");
                        loginLoading.setVisibility(View.GONE);
                        activity.startActivity(activity.getIntent());
                        return;
                    }

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(ferivicationId,codenya);

                    signInWithPhoneAuthCredential(credential);
                }
            });
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            loginLoading.setVisibility(View.GONE);
            Log.i(TAG, "onVerificationFailed: error "+e);
            loginKeterangan.setText(String.valueOf(e));
        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
          ferivicationId = verificationId;
          tokenNya = token;

            Log.i(TAG, "onCodeSent: kode dikirim");
            loginKeterangan.setText("kode dikirim");


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (boleHitung){
                        hitung--;
                        if (hitung > 1){
                            loginKeterangan.setText(String.valueOf(hitung));
                            handler.postDelayed(this,1000);
                        }else {
                            handler.removeCallbacks(this);
                        }
                    }
                }
            },1000);
            // ...
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            Toast.makeText(getContext(),"selamat datang"+user.getPhoneNumber(),Toast.LENGTH_LONG).show();
                            loginLoading.setVisibility(View.GONE);


                        } else {
                            loginLoading.setVisibility(View.GONE);
                            loginKeterangan.setText(String.valueOf("terjadi kesalahan tak terduga"));

                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                                loginLoading.setVisibility(View.GONE);
                                loginKeterangan.setText("kodenya salah");
                            }
                        }
                    }
                });
    }
}
