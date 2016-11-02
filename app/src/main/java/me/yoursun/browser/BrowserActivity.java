package me.yoursun.browser;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BrowserActivity extends AppCompatActivity {
    private Tab mWebViewWrapper;
    private EditText mEditAddress;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        mEditAddress = (EditText)findViewById(R.id.edit_address);
        mEditAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    mWebViewWrapper.loadUrl(textView.getText().toString(), false);
                    mProgressBar.setProgress(0);
                    mProgressBar.setVisibility(View.VISIBLE);
                    ((InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            mEditAddress.getWindowToken(), 0);
                    mWebViewWrapper.requestFocus();
                    return true;
                }
                return false;
            }
        });
        mWebViewWrapper = new Tab(this, (WebView)findViewById(R.id.webview_main));
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mWebViewWrapper.canGoBack()) {
            mWebViewWrapper.goBack();
            return;
        }
        super.onBackPressed();
    }

    public void updateWebViewProgress(int progress) {
        mProgressBar.setProgress(progress);
        if (progress == 100) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }, 1000);
        }
    }

    public void updateUrl(String url) {
        mEditAddress.setText(url);
    }
}