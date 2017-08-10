package me.yoursun.browser.tab;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

public class TabManager {
    private static final String TAG = "TabManager";

    private ArrayList<Tab> mTabList = new ArrayList<>();
    private int mCurrentTabId = -1;

    private TabManager() {}

    private static class Singleton {
        private static final TabManager instance = new TabManager();
    }

    public static TabManager getInstance() {
        return Singleton.instance;
    }

    public int getTabCount() {
        return mTabList.size();
    }

    @Nullable
    public Tab getCurrentTab() {
        if (mTabList.size() != 0 && mCurrentTabId != -1) {
            return mTabList.get(mCurrentTabId);
        }

        return null;
    }

    public Tab addTab(Context context) {
        Tab newTab = new Tab(context);
        mTabList.add(newTab);
        mCurrentTabId = mTabList.size() - 1;

        Log.v(TAG, "New tab added - Id: " + mCurrentTabId);
        return newTab;
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
