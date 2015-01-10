package com.alex.totoapp.totoloaders;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import com.alex.totoapp.totoadapters.RssAdapter;
import com.alex.totoapp.totoitems.RssItem;
import com.alex.totoapp.totoproviders.RssProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RssLoader implements LoaderManager.LoaderCallbacks<List<RssItem>> {

    public static final String TAG = "RssLoader";

    public static final String RSS_LINK = "rss_links";
    public static final int RSS_LINK_ID = 0;

    private Context context = null;
    private RssAdapter adapter = null;

    public RssLoader(Context context, RssAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public Loader<List<RssItem>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Creating RssLoader.");

        try {
            return new RssProvider(context, new URL(args.get(RSS_LINK).toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<RssItem>> loader, List<RssItem> data) {
        if (data.isEmpty()) {
            Log.w(TAG, "There are no rss items..");
            return;
        }

        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<RssItem>> loader) {
        // don't do shit
        Log.i(TAG, "reset loader");
    }
}
