package me.yoursun.browser;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BrowserActivity extends AppCompatActivity
        implements MainView, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "BrowserActivity";

    private MainPresenter mController;
    private MainModel mMainModel;

    private EditText mEditAddress;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        mMainModel = new MainModel();
        mController = new MainPresenter(this, mMainModel);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mEditAddress = (EditText)findViewById(R.id.edit_address);
        mEditAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE) {
                    mController.onLoadUrl(textView.getText().toString(), false);
                    ((InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            mEditAddress.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        mController.onSetupWebView(this);
    }

    @Override
    public void onBackPressed() {
        mController.onBackPressed();
    }

    @Override
    public void pauseActivity() {
        super.onBackPressed();
    }

    @Override
    public void updateUrl(String url) {
        mEditAddress.setText(url);
    }

    @Override
    public void updateTabsSize(int size) {
        ((TextView) findViewById(R.id.tab_size)).setText(String.valueOf(size));
    }

    public void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_tab:
                mController.onNewTab(this);
                return true;
            case R.id.new_secret_tab:
                return true;
            case R.id.bookmarks:
                return true;
            case R.id.find_in_page:
                return true;
            case R.id.settings:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void setWebView(WebView webView) {
        Log.v(TAG, "setWebView() : " + webView.toString());
        ((LinearLayout) findViewById(R.id.webview_position)).removeAllViews();
        ((LinearLayout) findViewById(R.id.webview_position)).addView(webView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((LinearLayout) findViewById(R.id.webview_position)).removeAllViews();
    }

    @Override
    public void showProgressBar() {
        if (mProgressBar.getVisibility() != View.VISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            ((InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    mEditAddress.getWindowToken(), 0);
        }
    }

    @Override
    public void updateProgress(int progress) {
        Log.d(TAG, "progress: " + progress);
        mProgressBar.setProgress(progress);
    }

    @Override
    public void hideProgressBar() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.INVISIBLE);
                mProgressBar.setProgress(0);
            }
        }, 500);
    }
}