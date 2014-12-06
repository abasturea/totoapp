package com.alex.totoapp.rssprovider;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.alex.totoapp.rssitem.RssItem;
import com.alex.totoapp.rssfetcher.RssFetcher;
import com.alex.totoapp.rssfetcher.RssFetcherImpl;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Provider extends AsyncTaskLoader<List<RssItem>> {

    private static final String TAG = "Provider";

    private URL url = null;
    private List<RssItem> rssItems = new ArrayList<RssItem>();

    private RssFetcher fetcher = new RssFetcherImpl();

    public Provider(Context context, URL url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<RssItem> loadInBackground() {
        try {
            fetcher.createSession(url);
            getItems(fetcher.fetchXML());
            fetcher.destroySession();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rssItems;
    }

    public void getItems(XmlPullParser xpp) throws XmlPullParserException, IOException {
        if (url != null) {

            Log.i(TAG, "Loading items:...");

            boolean insideItem = false;
            int eventType = xpp.getEventType();

            String headline = "";
            String link = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {

                    if (xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = true;
                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (insideItem)
                            headline = xpp.nextText();
                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                        if (insideItem)
                            link = xpp.nextText();
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                    RssItem item = new RssItem(headline, link);
                    rssItems.add(item);
                    Log.i(TAG, "Item loaded:" + item);
                    insideItem = false;
                }
                eventType = xpp.next();
            }
        }
    }
}
