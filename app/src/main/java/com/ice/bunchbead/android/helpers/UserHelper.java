package com.ice.bunchbead.android.helpers;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ice.bunchbead.android.LoginActivity;

/**
 * Created by rizky Kharisma on 14/07/18.
 */
public class UserHelper {

    public static void checkLogin(FirebaseAuth auth, UserNotExistAction onNotExist) {
        FirebaseUser currentUser = auth.getCurrentUser();
        // If don't have user account run UserNotExistListener
        if (currentUser == null) {
            onNotExist.onUserNotExist();
        }
    }

    public static void runLogin(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
    }

    public static void runLogout(Activity activity) {
        FirebaseAuth.getInstance().signOut();
        runLogin(activity);
    }


    public interface UserNotExistAction {
        void onUserNotExist();
    }
}
