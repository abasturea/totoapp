package com.github.kylarme.totoapp.totoloaders;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.github.kylarme.totoapp.totoadapters.FeedAdapter;
import com.github.kylarme.totoapp.totoapp.UpdateRssItemsService;
import com.github.kylarme.totoapp.totoitems.FeedItem;
import com.github.kylarme.totoapp.totoproviders.FeedContentProvider;
import com.github.kylarme.totoapp.totoproviders.FeedItemsHandler;
import com.github.kylarme.totoapp.totoproviders.RssItemsHandler;

import java.util.ArrayList;

public class FeedItemsLoader implements LoaderManager.LoaderCallbacks<ArrayList<FeedItem>> {

    public static final String TAG = "FeedItemsLoader";

    public static int sLoaderId;

    private Activity mActivity = null;

    private FeedAdapter mFeedAdapter = null;

    public FeedItemsLoader(Activity activity, FeedAdapter adapter) {
        mActivity = activity;
        mFeedAdapter = adapter;
    }

    @Override
    public Loader<ArrayList<FeedItem>> onCreateLoader(int id, Bundle bundle) {
        Log.i(TAG, "Creating FeedsLoader.");

        sLoaderId = id;

        return new FeedProvider(mActivity);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FeedItem>> arrayListLoader, ArrayList<FeedItem> feedItems) {
        if (feedItems == null) {
            Log.w(TAG, "There are no feed items..");
            return;
        }

        if (feedItems.isEmpty()) {
            Log.w(TAG, "There are no feed items..");
            return;
        }

        mFeedAdapter.setFeedDrawerItems(feedItems);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FeedItem>> arrayListLoader) {
        Log.i(TAG, "reset loader");

    }

    private static class FeedProvider extends AsyncTaskLoader<ArrayList<FeedItem>> {

        private final FeedItemsHandler mFeedItemsHandler;
        private final RssItemsHandler mRssItemsHandler;

        public FeedProvider(Context context) {
            super(context);

            context.getContentResolver().registerContentObserver(FeedContentProvider.FEED_ITEM_CONTENT_URI, true, new FeedsObserver(new Handler()));

            mFeedItemsHandler = new FeedItemsHandler(context);
            mRssItemsHandler = new RssItemsHandler(context);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();

            forceLoad();
        }

        @Override
        public ArrayList<FeedItem> loadInBackground() {

            return mFeedItemsHandler.getFeedItems();
        }

        private class FeedsObserver extends ContentObserver {

            public FeedsObserver(Handler handler) {
                super(handler);
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange);

                FeedItem feedItem = mFeedItemsHandler.getFeedItem(uri);

                if (feedItem != null) {
                    UpdateRssItemsService.updateRssItems(feedItem, new UpdateRssItemsService.ItemUpdateCallback() {
                        @Override
                        public void onItemUpdate(boolean success) {
                            // don't do
                        }
                    });
                }

                forceLoad();
            }
        }
    }
}
