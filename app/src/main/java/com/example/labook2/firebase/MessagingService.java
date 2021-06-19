package com.example.labook2.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.example.labook2.ChartPenyewaanActivity;
import com.example.labook2.MainPeminjamActivity;
import com.example.labook2.Preferences;
import com.example.labook2.R;
import com.example.labook2.apihelper.RetrofitClient;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;

public class MessagingService extends FirebaseMessagingService {
    Preferences sharedPrefManager;
    Context mContext;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        sharedPrefManager = new Preferences(MessagingService.this.getApplicationContext());
        mContext = this;

        Log.e("TOKENs",s);
        sharedPrefManager.saveSPString(sharedPrefManager.FCM_TOKEN,s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getNotification().getTitle();
        String msg = remoteMessage.getNotification().getBody();

        mContext = this;
        Log.e("TITLE", title);

        Intent intent = new Intent(this, MainPeminjamActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (mContext.getApplicationInfo().targetSdkVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "my_channel")
                    .setContentText(msg)
                    .setContentTitle(title)
                    .setSound(sound)
                    .setSmallIcon(R.drawable.add_lab)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);


            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notification.build());
        }


    }
}