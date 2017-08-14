package me.yoursun.browser;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

import me.yoursun.browser.tab.Tab;
import me.yoursun.browser.tab.TabManager;
import me.yoursun.browser.utils.Logger;
import me.yoursun.browser.utils.PreferenceHelper;

public class BrowserViewModel {

    private static final String TAG = BrowserViewModel.class.getSimpleName();

    public final ObservableBoolean isLoading = new ObservableBoolean(false);
    public final ObservableInt progress = new ObservableInt(0);
    public final ObservableField<String> currentUrl = new ObservableField<>("");
    public final ObservableField<String> tabCount = new ObservableField<>("0");

    private WeakReference<BrowserNavigator> navigator;

    private TabManager mTabManager;
    private Tab.WebViewCallback webViewCallback = new Tab.WebViewCallback() {
        @Override
        public void onProgressChanged(int progress) {
            if (isLoading.get() && BrowserViewModel.this.progress.get() < progress) {
                BrowserViewModel.this.progress.set(progress);
            }
        }

        @Override
        public void onPageStarted(String url) {
            currentUrl.set(url);
        }

        @Override
        public void onPageFinished(String url) {
            new Handler().postDelayed(() -> {
                isLoading.set(false);
                progress.set(0);
            }, 500);
        }

        @Override
        public void onScrollDown(int scroll) {

        }

        @Override
        public void onScrollUp(int scroll) {

        }
    };

    BrowserViewModel() {
        mTabManager = TabManager.getInstance();
    }

    void setNavigator(BrowserNavigator navigator) {
        this.navigator = new WeakReference<>(navigator);
    }

    void onCreate(Context context) {
        if (navigator.get() != null) {
            Tab tab = mTabManager.getCurrentTab();
            if (tab == null) {
                tab = mTabManager.addTab(context);
            }
            tab.setCallbacks(webViewCallback);
            navigator.get().switchTab(tab);
            updateTabCount();
            if (TextUtils.isEmpty(tab.getUrl())) {
                onLoadUrl(PreferenceHelper.getInstance().getDefaultHome(), false);
            }
        }
    }

    void onPause() {
        Tab tab = mTabManager.getCurrentTab();
        if (tab != null) {
            tab.pauseWebView();
            Logger.d(TAG, "WebView timer paused!");
            tab.capture();
        }
    }

    void onResume() {
        if (mTabManager.getCurrentTab() != null) {
            mTabManager.getCurrentTab().resumeWebView();
            Logger.d(TAG, "WebView timer resumed!");

            currentUrl.set(mTabManager.getCurrentTab().getUrl());
        }
    }

    void onSaveInstanceState(Bundle outState) {
    }

    void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    boolean onBackPressed() {
        if (mTabManager.getCurrentTab() != null && mTabManager.getCurrentTab().canGoBack()) {
            mTabManager.getCurrentTab().goBack();
            return true;
        }
        return false;
    }

    void onLoadUrl(String url, boolean isSearch) {
        Tab tab = mTabManager.getCurrentTab();
        if (tab != null) {
            tab.loadUrl(url, isSearch);
            isLoading.set(true);
            progress.set(0);
        }
    }

    void addNewTab(Context context) {
        if (navigator.get() != null) {
            Tab tab = TabManager.getInstance().addTab(context);
            tab.setCallbacks(webViewCallback);
            navigator.get().switchTab(tab);
            updateTabCount();

            if (TextUtils.isEmpty(tab.getUrl())) {
                onLoadUrl(PreferenceHelper.getInstance().getDefaultHome(), false);
            }
        }
    }

    private void updateTabCount() {
        if (Integer.parseInt(tabCount.get()) != mTabManager.getTabCount()) {
            tabCount.set(mTabManager.getTabCount() + "");
        }
    }

    void onSwitchTab(int position) {
        Tab tab = mTabManager.getTab(position);
        if (tab != null) {
            mTabManager.setCurrentTab(position);
            navigator.get().switchTab(tab);
        } else {
            Logger.e(TAG, "position " + position + " tab requested to switch, but it is null!!");
        }
    }
}
