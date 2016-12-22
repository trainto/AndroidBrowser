package me.yoursun.browser;

/**
 * Created by 1100464 on 2016. 12. 20..
 */

interface WebViewCallbacks {
    void onProgressChanged(int progress);
    void onPageStarted(String url);
    void onPageFinished(String url);
}
