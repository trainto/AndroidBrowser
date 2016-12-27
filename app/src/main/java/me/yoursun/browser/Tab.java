package me.yoursun.browser;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import me.yoursun.browser.utils.SmartUrl;

class Tab {
    private final String TAG = "Tab";
    private WebView mWebView;

    private Context mContext;

    private String mLastQuery;

    private WebViewCallbacks mCallbacks;

    Tab(Context context, WebView webView) {
        this.mContext = context;
        this.mWebView = webView;
        initWebView();
    }

    Tab(Context context) {
        this.mContext = context;
        this.mWebView = new WebView(mContext);
        initWebView();
    }

    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mCallbacks.onProgressChanged(newProgress);
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
                Log.d(TAG, "onPageStarted: " + url);
                mCallbacks.onPageStarted(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "onPageFinished: " + url);
                mCallbacks.onPageFinished(url);
            }

        });
    }

    void loadUrl(String query, boolean isSearch) {
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

    boolean canGoBack() {
        return mWebView.canGoBack();
    }

    void goBack() {
        mWebView.goBack();
    }

    void requestFocus() {
        mWebView.requestFocus();
    }

    WebView getWebView() {
        return mWebView;
    }

    void disconnectWebView() {
        mWebView = null;
    }

    void setCallbacks(WebViewCallbacks callbacks) {
        this.mCallbacks = callbacks;
    }
}
