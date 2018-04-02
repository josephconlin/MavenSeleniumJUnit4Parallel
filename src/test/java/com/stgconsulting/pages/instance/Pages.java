package com.stgconsulting.pages.instance;

import com.stgconsulting.pages.instance.yahoo.YHome;
import com.stgconsulting.pages.instance.yahoo.YSearchResults;
import com.stgconsulting.pages.instance.bing.BHome;
import com.stgconsulting.pages.instance.bing.BSearchResults;
import com.stgconsulting.pages.instance.google.GHome;
import com.stgconsulting.pages.instance.google.GSearchResults;

public class Pages {
    public static GHome GHome() {
        return new GHome();
    }
    public static GSearchResults GSearchResults() {
        return new GSearchResults();
    }
    public static YHome YHome() {
        return new YHome();
    }
    public static YSearchResults YSearchResults() {
        return new YSearchResults();
    }
    public static BHome BHome() {
        return new BHome();
    }
    public static BSearchResults BSearchResults() {
        return new BSearchResults();
    }
}
