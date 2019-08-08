package com.fahmyuliono.bismillah.kitasehat;

import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static android.app.Notification.DEFAULT_ALL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()){
                    task.getResult().getToken();
                }
            }
        });

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, "CHANEL_ID")
                .setContentTitle("halo")
                .setContentText("pesan")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setDefaults(DEFAULT_ALL);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        notificationManager.notify(0,mBuilder.build());
    }
}
