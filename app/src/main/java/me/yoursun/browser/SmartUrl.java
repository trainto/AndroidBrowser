package me.yoursun.browser;

import android.webkit.URLUtil;

class SmartUrl {
    private String mQuery;
    private boolean mIsSearch = false;

    public SmartUrl(String query) {
        this.mQuery = query.trim();
        mIsSearch = mQuery.contains(" ") || !mQuery.contains(".");
    }

    public String getUrl() {
       return URLUtil.guessUrl(mQuery);
    }

    public boolean isSearch() {
        return mIsSearch;
    }

    public String getSearchUrl() {
        return URLUtil.composeSearchUrl(mQuery, "http://google.com/search?q=holder", "holder");
    }
}
