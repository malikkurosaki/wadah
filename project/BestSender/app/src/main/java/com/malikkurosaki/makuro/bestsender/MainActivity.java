package com.malikkurosaki.makuro.bestsender;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.OnNmeaMessageListener;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mega4tech.whatsappapilibrary.WhatsappApi;
import com.mega4tech.whatsappapilibrary.exception.WhatsappNotInstalledException;
import com.mega4tech.whatsappapilibrary.liseteners.GetContactsListener;
import com.mega4tech.whatsappapilibrary.liseteners.SendMessageListener;
import com.mega4tech.whatsappapilibrary.model.WContact;
import com.mega4tech.whatsappapilibrary.model.WMessage;
import com.miguelgaeta.media_picker.MediaPicker;
import com.miguelgaeta.media_picker.RequestType;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MediaPicker.Provider,MediaPicker.OnError{

    Process p;
    String TAG = "-->";

    HashMap<WContact, Integer> mDictionary;
    List<WContact> mAllContacts;
    List<WContact> mReceivers;
    File attachmentFile;
    String[] mContacts;

    Button kirim,ambil;
    EditText isinya;

    Uri us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            // Preform su to get root privledges
            p = Runtime.getRuntime().exec("su");
            // Attempt to write a file to a root-only
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n");
            // Close the terminal
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();
                if (p.exitValue() != 255) {
                    // jika sukses
                    toastMessage("root");
                }
                else {
                    // jika tidak sukses
                    toastMessage("not root");
                    return;
                }
            } catch (InterruptedException e) {
                // jika terganggu
                toastMessage("terganggu > not root");
                return;
            }
        } catch (IOException e) {
            // jika error
            toastMessage("error > not root");
            return;
        }


        attachmentFile = null;
        kirim = findViewById(R.id.kirim);
        ambil = findViewById(R.id.ambil);
        isinya = findViewById(R.id.isinya);
        mDictionary = new HashMap<>();
        mReceivers = new LinkedList<>();

        if (!WhatsappApi.getInstance().isWhatsappInstalled()) {
            Toast.makeText(this, "Whatsapp not installed", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!WhatsappApi.getInstance().isRootAvailable()) {
            Toast.makeText(this, "Root is not available", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            WhatsappApi.getInstance().getContacts(this, new GetContactsListener() {
                @Override
                public void receiveWhatsappContacts(List<WContact> contacts) {
                    mAllContacts = contacts;
                    mContacts = new String[contacts.size()];

                    int i = 0;
                    for (WContact contact : contacts) {
                        mContacts[i] = contact.getName();//+ ", " + contact.getId().split("@")[0];
                        i++;

                    }

                    mReceivers.addAll(mAllContacts);

                }
            });
        } catch (WhatsappNotInstalledException e) {
            e.printStackTrace();
        }

        ambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        MediaPicker.startForDocuments(MainActivity.this,MainActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(getApplicationContext(),"permisi tidak diberikan , periksa setting > app > permisssion",Toast.LENGTH_LONG).show();
                    }
                };

                TedPermission.with(MainActivity.this)
                        .setPermissionListener(permissionListener)
                        .setDeniedCloseButtonText("permisi harus diberikan atau aplikasi tidak akan berjalan normal")
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
            }

        });

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = (!TextUtils.isEmpty(isinya.getText()))? isinya.getText().toString():"halo selamat siang";
                WMessage message = new WMessage(text,attachmentFile, getApplicationContext());
                try {
                    WhatsappApi.getInstance().sendMessage(mAllContacts, message, MainActivity.this, new SendMessageListener() {
                        @Override
                        public void finishSendWMessage(List<WContact> list, WMessage wMessage) {
                            Toast.makeText(getApplicationContext(),"berhasil dikirim",Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (IOException | WhatsappNotInstalledException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void toastMessage(String hh){
        Toast.makeText(getApplicationContext(),hh,Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MediaPicker.handleActivityResult(MainActivity.this, requestCode, resultCode, data, new MediaPicker.OnResult() {
            @Override
            public void onSuccess(Uri uri, RequestType request) {
                attachmentFile = new File(uri.getLastPathSegment());
            }

            @Override
            public void onCancelled() {

            }

            @Override
            public void onError(IOException e) {

            }
        });

    }



    @Override
    public void onError(IOException e) {
        e.printStackTrace();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public File getImageFile() {
        return null;
    }
}
