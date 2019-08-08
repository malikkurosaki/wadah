package com.fahmyuliono.bismillah;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static android.app.Notification.DEFAULT_ALL;

public class MyMessagingService extends FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        /*NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "CHANEL_ID")
                .setSmallIcon(R.drawable.icon_sehat)
                .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                .setContentText(Objects.requireNonNull(remoteMessage.getNotification()).getBody())
                .setDefaults(DEFAULT_ALL);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0,mBuilder.build());*/


        int mNotificationId = 1;

        // Build Notification , setOngoing keeps the notification always in status bar
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this,"CHANEL_ID")
                        .setSmallIcon(R.drawable.icon_sehat)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setOngoing(true)
                        .setDefaults(DEFAULT_ALL);
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Map<String, String> paketan = new HashMap<>();
        paketan.put("judul",remoteMessage.getNotification().getTitle());
        paketan.put("isi",remoteMessage.getNotification().getBody());
        intent.putExtra("paketan", (Serializable) paketan);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        NotificationManagerCompat mNotificationManager =  NotificationManagerCompat.from(this);
        mNotificationManager.notify(mNotificationId, mBuilder.build());


       /* // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, Main3Activity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID");
        builder.setContentIntent(resultPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
*/



    }
}