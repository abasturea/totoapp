package com.alex.totoapp.rssfetcher;

import org.xmlpull.v1.XmlPullParser;

import java.net.URL;

/**
 * Created by Kylarme on 12.11.2014.
 */
public interface RssFetcher {

    public static final String TAG = "Fetcher";

    /**
     * @param aUrl
     * @return
     */
    public boolean createSession(URL aUrl);


    /**
     * @return
     */
    public boolean destroySession();


    /**
     * @return
     */
    public XmlPullParser fetchXML();

}
