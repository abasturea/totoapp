package com.alex.totoapp.totoloaders;

import android.app.LoaderManager;

import com.alex.totoapp.totoitems.RssItem;

import java.util.List;


public interface RssLoader extends LoaderManager.LoaderCallbacks<List<RssItem>> {

    public static final String TAG = "RssLoader";
}
