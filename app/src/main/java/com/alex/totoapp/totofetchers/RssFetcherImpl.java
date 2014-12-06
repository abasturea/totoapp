package com.alex.totoapp.totofetchers;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kylarme on 12.11.2014.
 *
 */
public class RssFetcherImpl implements RssFetcher{

    private HttpURLConnection connection = null;
    private InputStream stream           = null;

    // Possible session id

    @Override
    public boolean createSession( URL aUrl ) {
        if( aUrl == null ) {
            Log.e(TAG, "Given url is null: " + aUrl);
            return false;
        }

        try {
            Log.i(TAG, "Connecting to this url: " + aUrl);

            connection = (HttpURLConnection)aUrl.openConnection();
            connection.setReadTimeout(1000);
            connection.setConnectTimeout(1500);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            connection.connect();
            stream = connection.getInputStream();

            return true;
        } catch (IOException e) {
            Log.e(TAG, "Could not connect to this url: " + aUrl);
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean destroySession(  ) {
        try {
            Log.i(TAG, "");
            stream.close();
            connection = null;
            stream     = null;

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public XmlPullParser fetchXML(  ) {
        XmlPullParser xmlPullParser = null;
        XmlPullParserFactory factory;

        try {
            factory       = XmlPullParserFactory.newInstance();
            xmlPullParser = factory.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(stream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return xmlPullParser;
    }
}
