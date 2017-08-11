package me.yoursun.browser.tab;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

public class TabManager {
    private static final String TAG = TabManager.class.getSimpleName();

    private ArrayList<Tab> tabList = new ArrayList<>();
    private int currentTabId = -1;

    private TabManager() {}

    private static class Singleton {
        private static final TabManager instance = new TabManager();
    }

    public static TabManager getInstance() {
        return Singleton.instance;
    }

    public int getTabCount() {
        return tabList.size();
    }

    @Nullable
    public Tab getCurrentTab() {
        if (tabList.size() != 0 && currentTabId != -1) {
            return tabList.get(currentTabId);
        }

        return null;
    }

    @Nullable
    public Tab getTab(int index) {
        if (index < 0 || index >= tabList.size()) {
            return null;
        }

        return tabList.get(index);
    }

    public Tab addTab(Context context) {
        Tab newTab = new Tab(context);
        tabList.add(newTab);
        currentTabId = tabList.size() - 1;

        Log.v(TAG, "New tab added - Id: " + currentTabId);
        return newTab;
    }

    public void removeTab(int id) {
        tabList.get(id).disconnectWebView();
        tabList.remove(id);
        if (id == currentTabId) {
            currentTabId--;
        }
    }

    public void clearTabs() {
        for (Tab tab : tabList) {
            tab.disconnectWebView();
            tab = null;
        }
        tabList.clear();
        currentTabId = -1;
    }
}
