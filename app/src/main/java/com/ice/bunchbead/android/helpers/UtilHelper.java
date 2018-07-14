package com.ice.bunchbead.android.helpers;

import android.content.Context;
import android.os.Build;

/**
 * Created by rizky Kharisma on 14/07/18.
 */
public class UtilHelper {
    public static int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }
}
