package com.alex.totoapp.totoproviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.alex.totoapp.totoitems.FeedItem;

import java.util.ArrayList;

public final class FeedItemsHandler {

    private ContentResolver mContentResolver;

    public FeedItemsHandler(Context context) {
        mContentResolver = context.getContentResolver();
    }

    public Uri addFeed(FeedItem feedItem) {
        ContentValues cv = new ContentValues();

        cv.put(FeedContentProvider.FEED_ITEM_TITLE, feedItem.getTitle());
        cv.put(FeedContentProvider.FEED_ITEM_URL, feedItem.getLink());

        return mContentResolver.insert(FeedContentProvider.FEED_ITEM_CONTENT_URI, cv);
    }

    public void addFeedItems(ArrayList<FeedItem> feedItems) {

        for (FeedItem item : feedItems) {
            ContentValues cv = new ContentValues();

            cv.put(FeedContentProvider.FEED_ITEM_TITLE, item.getTitle());
            cv.put(FeedContentProvider.FEED_ITEM_URL, item.getLink());

            mContentResolver.insert(FeedContentProvider.FEED_ITEM_CONTENT_URI, cv);
        }
    }

    public FeedItem getFeedItem(long feedId) {

        Uri getFeedUri = ContentUris.withAppendedId(FeedContentProvider.FEED_ITEM_CONTENT_URI, feedId);

        Cursor c = mContentResolver.query(getFeedUri, null, null, null, null);

        if (c == null) {
            return null;
        }

        FeedItem feedItem = null;

        if (c.moveToFirst()) {
            feedItem = new FeedItem(
                    c.getInt(c.getColumnIndex(FeedContentProvider._ID)),
                    c.getString(c.getColumnIndex(FeedContentProvider.FEED_ITEM_TITLE)),
                    c.getString(c.getColumnIndex(FeedContentProvider.FEED_ITEM_URL)));
        }

        c.close();

        return feedItem;
    }

    public FeedItem getFeedItem(Uri getFeedUri) {

        Cursor c = mContentResolver.query(getFeedUri, null, null, null, null);

        if (c == null) {
            return null;
        }

        FeedItem feedItem = null;

        if (c.moveToFirst()) {
            feedItem = new FeedItem(
                    c.getInt(c.getColumnIndex(FeedContentProvider._ID)),
                    c.getString(c.getColumnIndex(FeedContentProvider.FEED_ITEM_TITLE)),
                    c.getString(c.getColumnIndex(FeedContentProvider.FEED_ITEM_URL)));
        }

        c.close();

        return feedItem;
    }

    public ArrayList<FeedItem> getFeedItems() {

        Cursor c = mContentResolver.query(FeedContentProvider.FEED_ITEM_CONTENT_URI, null, null, null, FeedContentProvider._ID);

        if (c == null) {
            return null;
        }

        ArrayList<FeedItem> feedItems = new ArrayList<FeedItem>();

        if (c.moveToFirst()) {

            do {
                feedItems.add(new FeedItem(
                        c.getInt(c.getColumnIndex(FeedContentProvider._ID)),
                        c.getString(c.getColumnIndex(FeedContentProvider.FEED_ITEM_TITLE)),
                        c.getString(c.getColumnIndex(FeedContentProvider.FEED_ITEM_URL))));
            } while (c.moveToNext());
        }

        c.close();

        return feedItems;
    }

    public boolean deleteFeedItem(long feedId) {

        int count;

        Uri deleteUri = ContentUris.withAppendedId(FeedContentProvider.FEED_ITEM_CONTENT_URI, feedId);

        count = mContentResolver.delete(deleteUri, null, null);

        return count > 0;
    }
}
