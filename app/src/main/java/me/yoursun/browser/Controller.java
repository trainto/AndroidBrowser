package me.yoursun.browser;

import android.util.Log;

public class Controller {
    private static final String TAG = "Controller";

    private static Controller sInstance;

    private BrowserActivity mBrowserActivity;

    private Controller() {}

    public static void initialize(BrowserActivity activity) {
        if (sInstance != null) {
            Log.e(TAG, "Controller has been already initialized!!");
            return;
        }

        sInstance = new Controller();
    }

    public static Controller getInstance() {
        return sInstance;
    }

}
