package me.yoursun.browser;

import android.webkit.WebView;

interface MainView {
    void setWebView(WebView webView);

    void pauseActivity();

    void showProgressBar();

    void updateWebViewProgress(int progress);

    void updateUrl(String url);

    void updateTabsSize(int tabsSize);
}