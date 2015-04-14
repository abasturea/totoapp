package com.alex.totoapp.totoproviders;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.alex.totoapp.totoitems.RssItem;

import java.util.ArrayList;

public final class RssItemsHandler {

    private ContentResolver mContentResolver;

    public RssItemsHandler(Context context) {
        mContentResolver = context.getContentResolver();
    }

    public Uri addRssItem(RssItem rssItem) {
        ContentValues cv = new ContentValues();

        cv.put(FeedContentProvider.RSS_ITEM_HEADLINE, rssItem.getHeadline());
        cv.put(FeedContentProvider.RSS_ITEM_URL, rssItem.getLink());
        cv.put(FeedContentProvider.RSS_ITEM_IMAGE_LINK, rssItem.getImageLink());
        cv.put(FeedContentProvider.RSS_ITEM_DESCRIPTION, rssItem.getDescription());
        cv.put(FeedContentProvider.RSS_ITEM_FEED_ID, rssItem.getFeedId());

        return mContentResolver.insert(FeedContentProvider.RSS_ITEM_CONTENT_URI, cv);
    }

    public int addRssItems(ArrayList<RssItem> rssItems) {
        ContentValues[] valueList = new ContentValues[rssItems.size()];

        int insertCount;

        int i = 0;
        for (RssItem rssItem : rssItems) {
            ContentValues cv = new ContentValues();

            cv.put(FeedContentProvider.RSS_ITEM_HEADLINE, rssItem.getHeadline());
            cv.put(FeedContentProvider.RSS_ITEM_URL, rssItem.getLink());
            cv.put(FeedContentProvider.RSS_ITEM_IMAGE_LINK, rssItem.getImageLink());
            cv.put(FeedContentProvider.RSS_ITEM_DESCRIPTION, rssItem.getDescription());
            cv.put(FeedContentProvider.RSS_ITEM_FEED_ID, rssItem.getFeedId());

            valueList[i++] = cv;
        }

        insertCount = mContentResolver.bulkInsert(FeedContentProvider.RSS_ITEM_CONTENT_URI, valueList);

        return insertCount;
    }

    public int updateRssItem(RssItem rssItem) {

        Uri updateUri = ContentUris.withAppendedId(FeedContentProvider.RSS_ITEM_CONTENT_URI, rssItem.getId());

        ContentValues cv = new ContentValues();

        cv.put(FeedContentProvider.RSS_ITEM_HEADLINE, rssItem.getHeadline());
        cv.put(FeedContentProvider.RSS_ITEM_URL, rssItem.getLink());
        cv.put(FeedContentProvider.RSS_ITEM_IMAGE_LINK, rssItem.getImageLink());
        cv.put(FeedContentProvider.RSS_ITEM_DESCRIPTION, rssItem.getDescription());
        cv.put(FeedContentProvider.RSS_ITEM_FEED_ID, rssItem.getFeedId());

        return mContentResolver.update(updateUri, cv, null, null);
    }

    public int updateRssItems(ArrayList<RssItem> rssItems) {

        int count = 0;
        for (RssItem rssItem : rssItems) {
            count = count + updateRssItem(rssItem);
        }

        return count;
    }

    public RssItem getRssItem(long rssId) {

        Uri getRssUri = ContentUris.withAppendedId(FeedContentProvider.RSS_ITEM_CONTENT_URI, rssId);

        Cursor c = mContentResolver.query(getRssUri, null, null, null, null);

        if (c == null) {
            return null;
        }

        RssItem rssItem = null;

        if (c.moveToFirst()) {
            rssItem = new RssItem(
                    c.getInt(c.getColumnIndex(FeedContentProvider._ID)),
                    c.getString(c.getColumnIndex(FeedContentProvider.RSS_ITEM_HEADLINE)),
                    c.getString(c.getColumnIndex(FeedContentProvider.RSS_ITEM_URL)),
                    c.getString(c.getColumnIndex(FeedContentProvider.RSS_ITEM_IMAGE_LINK)),
                    c.getString(c.getColumnIndex(FeedContentProvider.RSS_ITEM_DESCRIPTION)),
                    c.getInt(c.getColumnIndex(FeedContentProvider.RSS_ITEM_FEED_ID)));
        }

        c.close();

        return rssItem;
    }

    public ArrayList<RssItem> getRssItems(long feedId) {

        Cursor c = mContentResolver.query(FeedContentProvider.RSS_ITEM_CONTENT_URI,
                null,
                FeedContentProvider.RSS_ITEM_FEED_ID + " =?",
                new String[]{String.valueOf(feedId)},
                FeedContentProvider._ID);

        if (c == null) {
            return null;
        }

        ArrayList<RssItem> rssItems = new ArrayList<RssItem>();

        if (c.moveToFirst()) {

            do {
                rssItems.add(new RssItem(
                        c.getInt(c.getColumnIndex(FeedContentProvider._ID)),
                        c.getString(c.getColumnIndex(FeedContentProvider.RSS_ITEM_HEADLINE)),
                        c.getString(c.getColumnIndex(FeedContentProvider.RSS_ITEM_URL)),
                        c.getString(c.getColumnIndex(FeedContentProvider.RSS_ITEM_IMAGE_LINK)),
                        c.getString(c.getColumnIndex(FeedContentProvider.RSS_ITEM_DESCRIPTION)),
                        c.getInt(c.getColumnIndex(FeedContentProvider.RSS_ITEM_FEED_ID))));
            } while (c.moveToNext());
        }

        c.close();

        return rssItems;
    }

    public boolean deleteRssItem(long rssId) {

        int count;

        Uri deleteUri = ContentUris.withAppendedId(FeedContentProvider.RSS_ITEM_CONTENT_URI, rssId);

        count = mContentResolver.delete(deleteUri, null, null);

        return count > 0;
    }

    public boolean deleteRssItems(long feedId) {

        int count;

        count = mContentResolver.delete(FeedContentProvider.RSS_ITEM_CONTENT_URI,
                FeedContentProvider.RSS_ITEM_FEED_ID + " =?",
                new String[]{String.valueOf(feedId)});

        return count > 0;
    }
}
