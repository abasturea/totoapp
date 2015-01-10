package com.alex.totoapp.totoloaders;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import com.alex.totoapp.totoadapters.FeedAdapter;
import com.alex.totoapp.totoitems.FeedItem;
import com.alex.totoapp.totoproviders.FeedProvider;

import java.util.ArrayList;

/**
 *
 * Created by Kylarme on 26-Dec-14.
 */
public class FeedsLoader implements LoaderManager.LoaderCallbacks<ArrayList<FeedItem>> {

    public static final String TAG = "FeedsLoader";

    //public static final String FEEDS_LINK = "feeds_link";
    public static final int FEEDS_LINK_ID = 1;

    private Context context = null;
    private FeedAdapter adapter = null;

    public FeedsLoader(Context context, FeedAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public Loader<ArrayList<FeedItem>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Creating FeedsLoader.");

        return new FeedProvider(context);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FeedItem>> loader, ArrayList<FeedItem> data) {
        if (data.isEmpty()) {
            Log.w(TAG, "There are no feed items..");
            return;
        }

        adapter.setFeedDrawerItems(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FeedItem>> loader) {
        // don't do shit
        Log.i(TAG, "reset loader");
    }
}
