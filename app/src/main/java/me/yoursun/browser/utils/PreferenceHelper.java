package me.yoursun.browser.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

public class PreferenceHelper {

    private static final String TAG = PreferenceHelper.class.getSimpleName();

    private static final String SHARED_PREFERENCE = "shared_preference";
    private static final String DEFAULT_HOME = "https://www.google.com";
    private static final String KEY_DEFAULT_HOME = "default_home";

    private SharedPreferences sharedPref;

    private PreferenceHelper() {}

    private static class LazyHolder {
        private static final PreferenceHelper INSTANCE = new PreferenceHelper();
    }

    public static PreferenceHelper getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void init(Context context) {
        sharedPref = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    public void setDefaultHome(String url) {
        setString(url);
    }

    public String getDefaultHome() {
        String url = getString(DEFAULT_HOME);
        if (url == null) {
            return DEFAULT_HOME;
        }

        return url;
    }


    private void setString(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(DEFAULT_HOME, value);
        editor.apply();
    }

    @Nullable
    private String getString(String key) {
        return sharedPref.getString(key, null);
    }
}
