package me.yoursun.browser.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.Map;

import me.yoursun.browser.utils.Logger;

public class Tab extends WebView {

    private static final String TAG = Tab.class.getSimpleName();

    private WeakReference<WebViewCallback> callbackWeakRef;

    public Tab(Context context) {
        super(context);
        init();
    }

    public Tab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Tab(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        LinearLayout.LayoutParams newParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        super.setLayoutParams(newParams);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        super.loadUrl(url, additionalHttpHeaders);
        requestFocus();
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

    public void setCallback(WebViewCallback callback) {
        callbackWeakRef = new WeakReference<>(callback);
    }


    public interface WebViewCallback {
        void onProgressChanged(int progress);

        void onPageStarted(String url);

        void onPageFinished(String url);

        void onScrollDown(int scroll);

        void onScrollUp(int scroll);
    }
}
