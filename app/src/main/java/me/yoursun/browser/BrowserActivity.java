package me.yoursun.browser;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import me.yoursun.browser.databinding.BrowserActivityBinding;
import me.yoursun.browser.databinding.ToolbarTopBinding;
import me.yoursun.browser.tab.Tab;
import me.yoursun.browser.utils.Logger;

public class BrowserActivity extends AppCompatActivity implements BrowserNavigator {

    private static final String TAG = BrowserActivity.class.getSimpleName();

    private BrowserActivityBinding mainBinding;
    private ToolbarTopBinding toolbarTopBinding;
    private BrowserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.browser_activity);
        toolbarTopBinding = mainBinding.toolbarTop;

        viewModel = new BrowserViewModel();
        viewModel.setNavigator(this);
        viewModel.onCreate(this);

        mainBinding.setViewModel(viewModel);
        toolbarTopBinding.setViewModel(viewModel);

        toolbarTopBinding.urlEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE) {
                mainBinding.root.requestFocus();
                viewModel.onLoadUrl(textView.getText().toString());
                InputMethodManager imm = ((InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE));
                if (imm != null) {
                    imm.hideSoftInputFromWindow(
                            toolbarTopBinding.urlEdit.getWindowToken(), 0);
                    return true;
                }
            }
            return false;
        });

        toolbarTopBinding.tabCount.setOnClickListener(view -> {
            Intent intent = new Intent(BrowserActivity.this, TabsActivity.class);
            startActivityForResult(intent, 0);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mainBinding.root.getChildCount() > 1) {
            mainBinding.root.removeViewAt(1);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int position = data.getIntExtra(TabsActivity.TAB_TO_SHOW, -1);
            if (position != -1) {
                viewModel.onSwitchTab(data.getIntExtra(TabsActivity.TAB_TO_SHOW, -1));
            } else {
                Logger.e(TAG, "Can't find tab position from the intent!!");
            }
        }
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
    public void switchTab(Tab tab) {
        Logger.d(TAG, "switchTab: " + tab.toString());
        if (mainBinding.root.getChildCount() > 1) {
            mainBinding.root.removeViewAt(1);
        }

        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT);
        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        mainBinding.root.addView(tab, 1, params);
    }
}