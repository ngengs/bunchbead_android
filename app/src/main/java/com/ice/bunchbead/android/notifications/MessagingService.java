package com.ice.bunchbead.android.notifications;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ice.bunchbead.android.NotificationResultActivity;
import com.ice.bunchbead.android.R;
import com.ice.bunchbead.android.helpers.UtilHelper;

import timber.log.Timber;

/**
 * Created by rizky Kharisma on 14/07/18.
 */
public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Timber.d("onMessageReceived() called with: remoteMessage to = [" + remoteMessage.getTo() +
                "]");
        String title = getString(R.string.app_name);
        String body = "Pemberitahuan";
        if (remoteMessage.getNotification() != null) {
            if (remoteMessage.getNotification().getBody() != null) {
                body = remoteMessage.getNotification().getBody();
            }
            if (remoteMessage.getNotification().getTitle() != null) {
                title = remoteMessage.getNotification().getTitle();
            }
        } else if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().containsKey("title")) {
                title = remoteMessage.getData().get("title");
            }
            if (remoteMessage.getData().containsKey("body")) {
                body = remoteMessage.getData().get("body");
            }
        }

        Intent intent = new Intent(this, NotificationResultActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "bunchbead_notification")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentIntent(stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT))
                .setContentTitle(title)
                .setContentText(body)
                .setColor(UtilHelper.getColor(this, R.color.colorPrimary))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(100, notificationBuilder.build());
    }
}
