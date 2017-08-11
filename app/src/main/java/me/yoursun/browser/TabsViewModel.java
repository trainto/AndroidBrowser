package me.yoursun.browser;

import android.graphics.Bitmap;

import me.yoursun.browser.tab.Tab;
import me.yoursun.browser.tab.TabManager;

class TabsViewModel {

    private TabManager mTabManager;
    private Bitmap captured;

    TabsViewModel() {
        mTabManager = TabManager.getInstance();
    }

    String getTitle(int position) {
        Tab tab = mTabManager.getTab(position);
        if (tab != null) {
            return tab.getTitle();
        }
        return "";
    }

    Bitmap getCaptured(int position) {
        Tab tab = mTabManager.getTab(position);
        if (tab != null) {
            return tab.getLastCaptured();
        }
        return null;
    }

    int getTabCount() {
        return mTabManager.getTabCount();
    }
}
