package com.untitledhorton.projectarchive.utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.untitledhorton.projectarchive.MainActivity;
import com.untitledhorton.projectarchive.R;

public class NotificationService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getExtras().getString("message");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context).
                setSmallIcon(R.mipmap.ic_launcher).
                setContentIntent(pendingIntent).
                setContentText(message).
                setContentTitle("ArcHive").
                setStyle(new Notification.BigTextStyle().bigText(message)).
                setSound(alarmSound).
                setAutoCancel(true);

        notificationManager.notify(100,builder.build());

    }
}