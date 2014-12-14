package com.alex.totoapp.totoapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alex.totoapp.R;
import com.alex.totoapp.totoadapters.RssAdapter;
import com.alex.totoapp.totoitems.RssItem;
import com.alex.totoapp.totoloaders.RssLoader;
import com.alex.totoapp.totoloaders.RssLoaderImpl;

import java.util.ArrayList;

public class RssFeedFragment extends Fragment {

    private static final String TAG = "RssFeedFragment";

    private static final String RSS_FEED_LINK = "link";
    private static final int RSS_FEED_LINK_ID = 0;

    private static View rootView = null;
    private static ArrayList<RssItem> rssItems = new ArrayList<RssItem>();

    private String urlText = null;
    private ListView itemsListView = null;
    private RssAdapter rssAdapter = null;

    public RssFeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_rss_feed, container, false);
            loadRssItems();
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy");

        if(rootView != null) {
            rssAdapter.clear();
            rssAdapter.notifyDataSetChanged();

            urlText = null;
            itemsListView = null;
            rssAdapter = null;

            rootView = null;
        }
    }

    private void loadRssItems() {
        Bundle loaderBundle = getArguments();

        urlText = loaderBundle.get(RSS_FEED_LINK).toString();
        itemsListView = (ListView) rootView.findViewById(R.id.listView);
        rssAdapter = new RssAdapter(getActivity(), android.R.layout.simple_list_item_1, rssItems);

        itemsListView.setAdapter(rssAdapter);

        Log.i(TAG, "Started setting a new list for: " + urlText);

        RssLoader rssLoader = new RssLoaderImpl(getActivity(), rssAdapter);

        if (getLoaderManager().getLoader(RSS_FEED_LINK_ID) != null) {
            getLoaderManager().restartLoader(RSS_FEED_LINK_ID, loaderBundle, rssLoader);
        } else {
            getLoaderManager().initLoader(RSS_FEED_LINK_ID, loaderBundle, rssLoader);
        }

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Clicked on position:" + position);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssItems.get(position).getLink()));
                startActivity(browserIntent);
            }
        });
    }
}
