package me.yoursun.browser.tab;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import me.yoursun.browser.utils.Logger;
import me.yoursun.browser.utils.SmartUrl;

import static android.content.Context.DOWNLOAD_SERVICE;

public class Tab extends FrameLayout {

    private static final String TAG = Tab.class.getSimpleName();

    private WebView webView;

    private String mLastQuery;

    private WebViewCallbacks mCallbacks;

    public Tab(@NonNull Context context) {
        super(context);
        init(context);
    }

    public Tab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Tab(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(final Context context) {
        webView = new WebView(context);
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
        addView(webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mCallbacks.onProgressChanged(newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                Logger.e(TAG, "Received Error::" + error.toString());
                loadUrl(mLastQuery, true);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Logger.d(TAG, "onPageStarted: " + url);
                mCallbacks.onPageStarted(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Logger.d(TAG, "onPageFinished: " + url);
                mCallbacks.onPageFinished(url);
            }
        });

        webView.setLongClickable(true);
        webView.setOnLongClickListener(v -> {
            WebView.HitTestResult result = webView.getHitTestResult();
            Logger.d(TAG, "onLongClick: " + result.getType() + " : " + result.toString());
            return false;
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));
                request.setMimeType(mimetype);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition,
                        mimetype));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                url, contentDisposition, mimetype));
                DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(context.getApplicationContext(), "Downloading File",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadUrl(String query, boolean isSearch) {
        mLastQuery = query;
        String url;
        SmartUrl smartUrl = new SmartUrl(query);
        if (isSearch) {
            url = smartUrl.getSearchUrl();
        } else if (smartUrl.isSearch()) {
            url = smartUrl.getSearchUrl();
        } else {
            url = smartUrl.getUrl();
        }
        webView.loadUrl(url);
        requestFocus();
    }

    public String getUrl() {
        return webView.getUrl();
    }

    public boolean canGoBack() {
        return webView.canGoBack();
    }

    public void goBack() {
        webView.goBack();
    }

    public void pauseWebView() {
        webView.pauseTimers();
    }

    public void resumeWebView() {
        webView.resumeTimers();
    }

    void disconnectWebView() {
        webView = null;
    }

    public void setCallbacks(WebViewCallbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public interface WebViewCallbacks {
        void onProgressChanged(int progress);

        void onPageStarted(String url);

        void onPageFinished(String url);
    }
}
