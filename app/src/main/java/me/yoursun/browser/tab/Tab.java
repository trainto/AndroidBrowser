package me.yoursun.browser.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import me.yoursun.browser.utils.SmartUrl;

public class Tab extends FrameLayout {

    private static final String TAG = Tab.class.getSimpleName();

    private CustomWebView webView;
    private SwipeRefreshLayout refreshLayout;
    private Bitmap lastCaptured;

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
        refreshLayout = new SwipeRefreshLayout(context);
        refreshLayout.setOnRefreshListener(() -> webView.reload());
        webView = new CustomWebView(context);
        refreshLayout.addView(webView);
        addView(refreshLayout);

        /*
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
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
        });
        */
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        FrameLayout.LayoutParams newParams = new LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        refreshLayout.setLayoutParams(newParams);
        webView.setLayoutParams(newParams);
    }

    public void setCallbacks(WebViewCallback callback) {
        webView.setCallback(callback);
    }

    public void loadUrl(String query) {
        String url;
        SmartUrl smartUrl = new SmartUrl(query);
        if (smartUrl.isSearch()) {
            url = smartUrl.getSearchUrl();
        } else {
            url = smartUrl.getUrl();
        }
        webView.loadUrl(url);
        requestFocus();
    }

    public String getTitle() {
        return webView.getTitle();
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

    public void capture() {
        Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), (int) (webView.getWidth() * 0.8), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);
        lastCaptured = bitmap;
    }

    public Bitmap getLastCaptured() {
        return lastCaptured;
    }

    public void dismissRefresh() {
        refreshLayout.setRefreshing(false);
    }

    public interface WebViewCallback {
        void onProgressChanged(int progress);

        void onPageStarted(String url);

        void onPageFinished(String url);

        void onScrollDown(int scroll);

        void onScrollUp(int scroll);
    }
}
