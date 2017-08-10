package me.yoursun.browser.application;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import me.yoursun.browser.utils.Logger;

public class SatteliterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ActivityLifeCycle());

        Logger.setDebuggable(isDebuggable());
    }

    private boolean isDebuggable() {
        boolean debuggable = false;

        PackageManager pm = getApplicationContext().getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(
                    getApplicationContext().getPackageName(), 0);
            debuggable = (0 != (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            // Do Nothing!
        }

        return debuggable;
    }
}
