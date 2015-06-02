package com.lowworker.android.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.lowworker.android.R;
import com.lowworker.android.model.entities.NotificationsWrapper;
import com.lowworker.android.views.activities.CardDetailActivity;


public class NotificationTools {

    public static final String ACTION_RETRY = "ble.scut" + "retry";

    public static void postOngoingNotification(String title, String msg, int notificationId, Context context,
                                               NotificationManager manager, Notification.Builder builder) {
        long[] vibratePattern = {1, 600, 1000};
        Intent notifyIntent = new Intent(context, Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(
                context,
                0,
                new Intent[]{notifyIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setContentText(msg)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setVibrate(vibratePattern)
                .setContentIntent(pendingIntent);
        manager.notify(notificationId, builder.build());
    }



    public static void updateNotification(String title, String msg, int notificationId, Context context,
                                    NotificationManager manager, Notification.Builder builder) {
        Intent notifyIntent = new Intent(context, Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(
                context,
                0,
                new Intent[]{notifyIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setContentText(msg)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        manager.notify(notificationId, builder.build());
    }

    public static void postNotificationWrapper(NotificationsWrapper notificationsWrapper,  Context context,
                                        NotificationManager manager, Notification.Builder builder) {

        int  size =  notificationsWrapper.getData().size();
        for(int i = 0; i<size;i++){

            Intent notifyIntent = new Intent(context, CardDetailActivity.class);
            String card_id = notificationsWrapper.getData().get(i).getCard_id();
            String card_body = notificationsWrapper.getData().get(i).getBody();
            String card_title = notificationsWrapper.getData().get(i).getTitle();
            notifyIntent.putExtra("card_id",card_id);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    i,
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT );

            builder
                    .setContentText(card_body)
                    .setContentTitle(card_title)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setAutoCancel(false)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent);
            manager.notify(i, builder.build());
        }

    }

    public static void postErrorNotification(String error,  Context context,
                                        NotificationManager manager, Notification.Builder builder) {


        Intent retryIntent = new Intent(NotificationTools.ACTION_RETRY);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                retryIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setContentText(context.getResources().getString(R.string.notification_error_msg
                ))
                .setContentTitle(context.getResources().getString(R.string.notification_error_title
                ))
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        manager.notify(1, builder.build());
    }
    public static void postLoginNotification(String error,  Context context,
                                             NotificationManager manager, Notification.Builder builder) {


        Intent loginIntent = new Intent(NotificationTools.ACTION_RETRY);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                loginIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setContentText(context.getResources().getString(R.string.notification_error_login_msg
                ))
                .setContentTitle(context.getResources().getString(R.string.notification_error_login_title
                ))
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        manager.notify(1, builder.build());
    }
}
