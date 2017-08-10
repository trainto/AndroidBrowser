package me.yoursun.browser;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import me.yoursun.browser.databinding.BrowserActivityBinding;
import me.yoursun.browser.tab.Tab;
import me.yoursun.browser.utils.Logger;

public class BrowserActivity extends AppCompatActivity implements BrowserNavigator {

    private static final String TAG = BrowserActivity.class.getSimpleName();

    private BrowserActivityBinding binding;
    private BrowserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.browser_activity);

        viewModel = new BrowserViewModel();
        viewModel.setNavigator(this);
        viewModel.onCreate(this);

        binding.setViewModel(viewModel);

        binding.editAddress.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE) {
                viewModel.onLoadUrl(textView.getText().toString(), false);
                ((InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        binding.editAddress.getWindowToken(), 0);
                return true;
            }
            return false;
        });

        binding.tabSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BrowserActivity.this, TabsActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewModel.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (!viewModel.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.new_tab:
                    viewModel.addNewTab(this);
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
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());
        popup.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.tabs.removeAllViews();
    }

    @Override
    public void switchTab(Tab tab) {
        Logger.d(TAG, "switchTab: " + tab.toString());

        binding.tabs.removeAllViews();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        tab.setLayoutParams(params);
        binding.tabs.addView(tab);
    }
}