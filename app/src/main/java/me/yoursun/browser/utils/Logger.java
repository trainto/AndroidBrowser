package me.yoursun.browser.utils;

import android.util.Log;

public class Logger {
    private static final String TAG = "Sattleliter";

    private static boolean debuggable;

    public static void setDebuggable(boolean debuggable) {
        Logger.debuggable = debuggable;
    }

    public static void v(String prefix, String msg) {
        v(prefix + ":: " + msg);
    }

    public static void v(String msg) {
        if (debuggable) {
            Log.v(TAG, msg);
        }
    }

    public static void d(String prefix, String msg) {
        d(prefix + ":: " + msg);
    }

    public static void d(String msg) {
        if (debuggable) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String prefix, String msg) {
        e(prefix + ":: " + msg);
    }

    public static void e(String msg) {
        if (debuggable) {
            Log.e(TAG, msg);
        }
    }
}
