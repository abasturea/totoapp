package com.alex.totoapp.rssloader;

import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import com.alex.totoapp.rssapp.RssAdapter;
import com.alex.totoapp.rssitem.RssItem;
import com.alex.totoapp.rssprovider.Provider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RssLoaderImpl implements RssLoader {

    private Context context = null;
    private RssAdapter adapter = null;

    public RssLoaderImpl(Context context, RssAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public Loader<List<RssItem>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Creating RssLoader.");

        try {
            return new Provider(context, new URL(args.get("link").toString()));
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
