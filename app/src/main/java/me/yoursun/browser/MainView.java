package me.yoursun.browser;

import android.webkit.WebView;

interface MainView {
    void setWebView(WebView webView);

    void pauseActivity();

    void updateUrl(String url);

    void updateTabsSize(int tabsSize);

    void showProgressBar();

    void updateProgress(int progress);

    void hideProgressBar();
}
