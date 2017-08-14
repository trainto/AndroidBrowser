package me.yoursun.browser.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

import me.yoursun.browser.utils.Logger;

public class CustomWebView extends WebView {

    private static final String TAG = CustomWebView.class.getSimpleName();

    private WeakReference<Tab.WebViewCallback> callbackWeakRef;

    public CustomWebView(Context context) {
        super(context);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (callbackWeakRef.get() != null) {
                    callbackWeakRef.get().onProgressChanged(newProgress);
                }
            }
        });

        setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                Logger.e(TAG, "Received Error::" + error.toString());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Logger.d(TAG, "onPageStarted: " + url);
                if (callbackWeakRef.get() != null) {
                    callbackWeakRef.get().onPageStarted(url);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Logger.d(TAG, "onPageFinished: " + url);
                if (callbackWeakRef.get() != null) {
                    callbackWeakRef.get().onPageFinished(url);
                }
            }
        });

        setLongClickable(true);
        setOnLongClickListener(v -> {
            WebView.HitTestResult result = getHitTestResult();
            Logger.d(TAG, "onLongClick: " + result.getType() + " : " + result.toString());
            return false;
        });
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (callbackWeakRef.get() != null) {
            if (t > oldt) {
                callbackWeakRef.get().onScrollDown(t - oldt);
            } else if (t < oldt) {
                callbackWeakRef.get().onScrollUp(oldt - t);
            }
        }
    }

    public void setCallback(Tab.WebViewCallback callback) {
        callbackWeakRef = new WeakReference<>(callback);
    }
}
