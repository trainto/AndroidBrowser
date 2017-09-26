package me.yoursun.browser.tab;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import me.yoursun.browser.utils.Logger;

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

    public void setCurrentTab(int id) {
        if (id >= 0 && id < tabList.size()) {
            currentTabId = id;
            return;
        }
        Logger.e(TAG, id + " does not exist in tab list");
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

    public Tab addTab(Context context, Tab.WebViewCallback callback) {
        Tab tab = addTab(context);
        if (tab != null) {
            tab.setCallback(callback);
        }

        return tab;
    }

    public void removeTab(int id) {
        tabList.remove(id);
        if (id == currentTabId) {
            currentTabId--;
        }
    }

    public void clearTabs() {
        for (Tab tab : tabList) {
            tab = null;
        }
        tabList.clear();
        currentTabId = -1;
    }

    public void pauseWebView() {
        if (getCurrentTab() != null) {
            getCurrentTab().pauseTimers();
            Log.v(TAG, "WebView timer paused!!");
        }
    }

    public void resumeWebView() {
        if (getCurrentTab() != null) {
            getCurrentTab().resumeTimers();
            Log.v(TAG, "WebView timer resumed!!");
        }
    }
}
