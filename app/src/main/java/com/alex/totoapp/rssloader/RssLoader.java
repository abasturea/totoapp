package com.alex.totoapp.rssloader;

import android.app.LoaderManager;

import com.alex.totoapp.rssitem.RssItem;

import java.util.List;


public interface RssLoader extends LoaderManager.LoaderCallbacks<List<RssItem>> {

    public static final String TAG = "RssLoader";
}
