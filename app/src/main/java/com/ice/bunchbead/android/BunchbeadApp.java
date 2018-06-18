package com.ice.bunchbead.android;

import android.app.Application;
import android.util.Log;

import timber.log.Timber;

public class BunchbeadApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Build Log
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    if (priority == Log.DEBUG || priority == Log.VERBOSE) return;
                    Log.println(priority, tag, message);
                }
            });
        }
    }
}
