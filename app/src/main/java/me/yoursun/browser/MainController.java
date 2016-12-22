package me.yoursun.browser;

import android.content.Context;
import android.util.Log;

class MainController {
    private static final String TAG = "MainController";

    private MainView mView;
    private MainModel mModel;

    MainController(MainView view, MainModel model) {
        this.mView = view;
        this.mModel = model;
    }

    void onBackPressed() {
        Log.v(TAG, "onBackPressed()");
        if (!mModel.processBackPressed()) {
            mView.pauseActivity();
        }
    }

    void onSetupWebView(Context context) {
        Log.v(TAG, "onSetupWebView()");
        mView.setWebView(mModel.getWebViewToShow(context, new WebViewToUICallbacks()));
        mView.updateTabsSize(mModel.getTabsSize());
    }

    void onLoadUrl(String url, boolean isSearch) {
        Log.v(TAG, "onLoadUrl()");
        mModel.loadUrl(url, isSearch);
    }

    void onNewTab(Context context) {
        Log.v(TAG, "onNewTab()");
        mModel.addNewTab(context);
        onSetupWebView(context);
        mView.updateUrl("");
    }

    private class WebViewToUICallbacks implements WebViewCallbacks {
        @Override
        public void onProgressChanged(int progress) {
            mView.updateWebViewProgress(progress);
        }

        @Override
        public void onPageStarted(String url) {
            mView.showProgressBar();
        }

        @Override
        public void onPageFinished(String url) {
            mView.updateUrl(url);
        }
    }
}
