package me.yoursun.browser;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

// TODO: Need more proper name
class MainModel {
    private static final String TAG = "MainModel";

    private TabManager mTabManager;

    MainModel() {
        mTabManager = TabManager.getInstance();
    }

    public void pause() {
        mTabManager.getCurrentTab().getWebView().pauseTimers();
        Log.v(TAG, "WebView timer paused!");
    }

    public void resume() {
        mTabManager.getCurrentTab().getWebView().resumeTimers();
        Log.v(TAG, "WebView timer resumed!");
    }

    boolean processBackPressed() {
        if (mTabManager.getCurrentTab().canGoBack()) {
            mTabManager.getCurrentTab().goBack();
            return true;
        }

        return false;
    }

    WebView getWebViewToShow(Context context, WebViewCallbacks callbacks) {
        TabManager tabManager = TabManager.getInstance();
        Tab currentTab;
        if (tabManager.size() != 0) {
            currentTab = tabManager.getCurrentTab();
        } else {
            tabManager.addTab(context);
            currentTab = tabManager.getCurrentTab();
        }

        // Set callbacks for WebView status
        currentTab.setCallbacks(callbacks);

        return currentTab.getWebView();
    }

    void loadUrl(String url, boolean isSearch) {
        Tab tab = TabManager.getInstance().getCurrentTab();
        tab.loadUrl(url, isSearch);
        tab.requestFocus();
    }

    void addNewTab(Context context) {
        TabManager.getInstance().addTab(context);
    }

    public int getTabsSize() {
        return TabManager.getInstance().size();
    }
}
