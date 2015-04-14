package com.alex.totoapp.totoloaders;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.alex.totoapp.totoadapters.RssAdapter;
import com.alex.totoapp.totoitems.FeedItem;
import com.alex.totoapp.totoitems.RssItem;
import com.alex.totoapp.totoproviders.RssItemsHandler;

import java.util.ArrayList;
import java.util.List;

public class RssItemsLoader implements LoaderManager.LoaderCallbacks<List<RssItem>> {

    public static final String TAG = "RssItemsLoader";

    public static final String RSS_FEED_ITEM = "rss_links";

    public static int sLoaderId;

    private static RssItemsHandler sRssItemsHandler;

    private Activity mActivity = null;

    private RssAdapter mRssAdapter = null;

    public RssItemsLoader(Activity context, RssAdapter adapter) {
        mActivity = context;
        mRssAdapter = adapter;
    }

    @Override
    public Loader<List<RssItem>> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Creating RssLoader.");

        sLoaderId = id;
        sRssItemsHandler = new RssItemsHandler(mActivity);

        return new RssProvider(mActivity, (FeedItem) args.get(RSS_FEED_ITEM));
    }

    @Override
    public void onLoadFinished(Loader<List<RssItem>> loader, List<RssItem> data) {

        if (data == null) {
            return;
        }

        if (data.isEmpty()) {
            Log.w(TAG, "There are no rss items...");
            return;
        }

        mRssAdapter.setData(data);

    }

    @Override
    public void onLoaderReset(Loader<List<RssItem>> loader) {
        Log.i(TAG, "reset loader");

    }

    private static class RssProvider extends AsyncTaskLoader<List<RssItem>> {

        private final FeedItem mFeedItem;
        private ArrayList<RssItem> mRssItems;

        public RssProvider(Context context, FeedItem feedItem) {
            super(context);
            mRssItems = new ArrayList<RssItem>();
            mFeedItem = feedItem;
        }

        @Override
        protected void onReset() {
            Log.i(TAG + " onReset", "Don't reset loader.");

        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();

            loadInBackground();
        }

        @Override
        public ArrayList<RssItem> loadInBackground() {

            mRssItems = new ArrayList<RssItem>();

            mRssItems = sRssItemsHandler.getRssItems(mFeedItem.getId());

            deliverResult(mRssItems);

            return mRssItems;
        }
    }
}
