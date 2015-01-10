package com.alex.totoapp.totoproviders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

import com.alex.totoapp.totoitems.FeedItem;

import java.util.ArrayList;

/**
 * Gets the FeedItems from the content provider.
 */
public class FeedProvider extends AsyncTaskLoader<ArrayList<FeedItem>> {

    private Context context = null;

    public FeedProvider(Context context) {
        super(context);
        this.context = context;
        this.context.getContentResolver().registerContentObserver(FeedContentProvider.CONTENT_URI, true,new FeedsObserver(new Handler()));
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<FeedItem> loadInBackground() {
        return getFeedItems();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        loadInBackground();
    }

    private ArrayList<FeedItem> getFeedItems() {
        Cursor c = context.getContentResolver().query(FeedContentProvider.CONTENT_URI, null, null, null, FeedContentProvider.FEED_ID);
        ArrayList<FeedItem> feedItems = new ArrayList<FeedItem>();

        if (c == null) {
            return feedItems;
        }

        if (c.moveToFirst()) {

            do {
                feedItems.add(new FeedItem(
                        c.getString(c.getColumnIndex(FeedContentProvider.FEED_TITLE)),
                        c.getString(c.getColumnIndex(FeedContentProvider.FEED_URL))));
            } while (c.moveToNext());
        }

        return feedItems;
    }

    private class FeedsObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public FeedsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            forceLoad();
        }
    }
}
