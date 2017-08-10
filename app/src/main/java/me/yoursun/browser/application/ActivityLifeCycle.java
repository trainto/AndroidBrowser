package me.yoursun.browser.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import me.yoursun.browser.utils.Logger;

public class ActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "LifeCycle";

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Logger.d(TAG, activity.getClass().getSimpleName() + " is created.");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Logger.d(TAG, activity.getClass().getSimpleName() + " is started.");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Logger.d(TAG, activity.getClass().getSimpleName() + " is resumed.");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Logger.d(TAG, activity.getClass().getSimpleName() + " is created.");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Logger.d(TAG, activity.getClass().getSimpleName() + " is created.");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Logger.d(TAG, activity.getClass().getSimpleName() + " is created.");
    }
}
