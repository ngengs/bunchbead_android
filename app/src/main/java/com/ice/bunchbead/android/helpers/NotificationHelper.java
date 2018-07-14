package com.ice.bunchbead.android.helpers;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by rizky Kharisma on 14/07/18.
 */
public class NotificationHelper {
    public static void registerNotification() {
        FirebaseMessaging.getInstance().subscribeToTopic("rank-notification");
    }

    public static void unregisterNotification() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("rank-notification");
    }
}
