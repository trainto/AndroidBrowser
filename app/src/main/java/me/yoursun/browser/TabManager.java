package me.yoursun.browser;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

import java.util.ArrayList;

class TabManager {
    private static final String TAG = "TabManager";

    private ArrayList<Tab> mTabList = new ArrayList<>();
    private int mCurrentTabId = -1;

    private TabManager() {}

    private static class Singleton {
        private static final TabManager instance = new TabManager();
    }

    static TabManager getInstance() {
        return Singleton.instance;
    }

    int size() {
        return mTabList.size();
    }

    Tab getCurrentTab() {
        if (mTabList.size() != 0 && mCurrentTabId != -1) {
            return mTabList.get(mCurrentTabId);
        }
        Log.e(TAG, "Tab does not exist!!");
        return null;
    }

    Tab addTab(Context context) {
        Tab newTab = new Tab(context);
        mTabList.add(newTab);
        mCurrentTabId = mTabList.size() - 1;

        Log.v(TAG, "New tab added - Id: " + mCurrentTabId);
        return newTab;
    }

    public void addTab(Context context, WebView webView) {
        mTabList.add(new Tab(context, webView));
        mCurrentTabId = mTabList.size() - 1;
    }

    public void removeTab(int id) {
        mTabList.get(id).disconnectWebView();
        mTabList.remove(id);
        if (id == mCurrentTabId) {
            mCurrentTabId--;
        }
    }

    public void clearTabs() {
        for (Tab tab : mTabList) {
            tab.disconnectWebView();
            tab = null;
        }
        mTabList.clear();
        mCurrentTabId = -1;
    }
}
