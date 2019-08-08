package probus.malikkurosaki.probussystem;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static androidx.core.app.NotificationCompat.DEFAULT_ALL;

public class MyMessagingService extends FirebaseMessagingService {

    private static final int MY_NOTIFICATION_ID=1;
    NotificationManager notificationManager;
    Notification myNotification;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        int mNotificationId = 1;
        Intent myIntent = new Intent(this, Activity_Probus.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, myIntent,PendingIntent.FLAG_ONE_SHOT);
        Notification myNotification = new NotificationCompat.Builder(this,"ID")
                .setContentTitle(String.valueOf(remoteMessage.getNotification().getTitle()))
                .setContentText(String.valueOf(remoteMessage.getNotification().getBody()))
                .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(DEFAULT_ALL)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .build();

        NotificationManager mNotificationManager =  (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mNotificationId,myNotification);
    }
}