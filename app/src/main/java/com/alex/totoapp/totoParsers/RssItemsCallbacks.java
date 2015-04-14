package com.alex.totoapp.totoparsers;

import com.alex.totoapp.totoitems.RssItem;

public interface RssItemsCallbacks {


    public interface GetItemCallback{
        public void onGetItem(RssItem rssItem);
    }

    public interface GetEntryCallback{
        public void onGetEntry(RssItem rssItem);
    }

    public interface GetRssItemCallback{
        public void onGetRssItem(RssItem rssItem);
    }

    public interface GetImageCallback{
        public void onGetImage(int rssItemId, byte[] image);
    }
}
