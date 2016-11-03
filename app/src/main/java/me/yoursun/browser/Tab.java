package me.yoursun.browser;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import me.yoursun.browser.utils.SmartUrl;

public class Tab {
    private final String TAG = "Tab";
    private WebView mWebView;

    private BrowserActivity mActivity;

    private String mLastQuery;

    public Tab(BrowserActivity activity, WebView webView) {
        this.mActivity = activity;
        this.mWebView = webView;
        initWebView();
    }

    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mActivity.updateWebViewProgress(newProgress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                Log.e(TAG, "Received Error::" + error.toString());
                loadUrl(mLastQuery, true);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "onPageFinished: " + url);
                mActivity.updateUrl(url);
            }

        });
    }

    public void loadUrl(String query, boolean isSearch) {
        mLastQuery = query;
        String url;
        SmartUrl smartUrl = new SmartUrl(query);
        if (isSearch){
           url = smartUrl.getSearchUrl();
        } else if (smartUrl.isSearch()) {
            url = smartUrl.getSearchUrl();
        } else {
            url = smartUrl.getUrl();
        }
        mWebView.loadUrl(url);
    }

    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    public void goBack() {
        mWebView.goBack();
    }

    public void requestFocus() {
        mWebView.requestFocus();
    }
}
