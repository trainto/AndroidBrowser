package me.yoursun.browser;

import android.content.Context;
import android.util.Log;

class MainPresenter {
    private static final String TAG = "MainPresenter";

    private MainView mView;
    private MainModel mModel;

    MainPresenter(MainView view, MainModel model) {
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
        private int mProgress = 0;
        private boolean mIsInPageLoading = false;

        @Override
        public void onProgressChanged(int progress) {
            if (mIsInPageLoading && mProgress < progress) {
                mView.updateProgress(progress);
            }
        }

        @Override
        public void onPageStarted(String url) {
            mView.updateUrl(url);
            mView.showProgressBar();
            mIsInPageLoading = true;
        }

        @Override
        public void onPageFinished(String url) {
            mView.hideProgressBar();
            mProgress = 0;
            mIsInPageLoading = false;
        }
    }
}
