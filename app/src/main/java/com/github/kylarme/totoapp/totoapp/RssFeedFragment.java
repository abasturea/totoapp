package com.github.kylarme.totoapp.totoapp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.kylarme.totoapp.R;
import com.github.kylarme.totoapp.totoadapters.RssAdapter;
import com.github.kylarme.totoapp.totoitems.FeedItem;
import com.github.kylarme.totoapp.totoitems.RssItem;
import com.github.kylarme.totoapp.totoproviders.RssItemsHandler;

import java.util.ArrayList;

public class RssFeedFragment extends Fragment {

    private static final String TAG = "RssFeedFragment";

    private static final int PHONE_SPAN_COUNT = 1;
    private static final int TABLET_SPAN_COUNT = 2;

    private static FeedItem sFeedItem;

    private static View sRootView;

    private static Activity mActivity;

    private static RecyclerView mItemsView;

    private StaggeredGridLayoutManager mGridLayoutManager;
    private int mSpanCount;

    private static RssAdapter sRssAdapter;

    private static RssItemsHandler sRssItemsHandler;

    public RssFeedFragment() {
    }

    protected static boolean updateRssItems() {

        if (sRootView == null || sFeedItem == null)
            return false;

        UpdateRssItemsService.updateRssItems(sFeedItem, new UpdateRssItemsService.ItemUpdateCallback() {
            @Override
            public void onItemUpdate(boolean success) {
                if (success) {
                    new LoadItemsTask().execute(sFeedItem);
                }
            }
        });

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();

        sRssItemsHandler = new RssItemsHandler(mActivity);
        sRssAdapter = new RssAdapter(mActivity);

        mSpanCount = 1;

        Bundle loaderBundle = getArguments();

        sFeedItem = (FeedItem) loaderBundle.get(NavigationDrawerFragment.RSS_FEED_ITEM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        if (sRootView == null) {
            Utils.Device.setScreenOrientation(mActivity, ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

            sRootView = inflater.inflate(R.layout.fragment_rss_feed, container, false);

            setViewCards();

            new LoadItemsTask().execute(sFeedItem);
        }

        return sRootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i(TAG, "onStop");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy");

        mItemsView = null;
        sRssAdapter = null;

        sRootView = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mGridLayoutManager.setSpanCount(mSpanCount);
        }

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridLayoutManager.setSpanCount(mSpanCount + 1);
        }

        mItemsView.setLayoutManager(mGridLayoutManager);
    }

    private void setViewCards() {

        mItemsView = (RecyclerView) sRootView.findViewById(R.id.rss_feed_view);
        mItemsView.setHasFixedSize(true);
        mItemsView.setAdapter(sRssAdapter);

        if (Utils.Device.getDeviceType(mActivity) == Utils.Device.PHONE) {
            mSpanCount = PHONE_SPAN_COUNT;
        } else {
            mSpanCount = TABLET_SPAN_COUNT;
        }

        if (Utils.Device.getDeviceScreenOrientation(mActivity) == Configuration.ORIENTATION_LANDSCAPE) {
//            mGridLayoutManager = new GridLayoutManager(mActivity, mSpanCount + 1);
            mGridLayoutManager = new StaggeredGridLayoutManager(mSpanCount + 1, StaggeredGridLayoutManager.VERTICAL);
        } else {
//            mGridLayoutManager = new GridLayoutManager(mActivity, mSpanCount);
            mGridLayoutManager = new StaggeredGridLayoutManager(mSpanCount , StaggeredGridLayoutManager.VERTICAL);
        }

//        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mItemsView.setLayoutManager(mGridLayoutManager);
    }

    private static class LoadItemsTask extends AsyncTask<FeedItem, Void, Boolean> {
        ArrayList<RssItem> rssItems;

        @Override
        protected Boolean doInBackground(FeedItem... params) {

            long feedItemId = params[0].getId();

            Log.i(TAG, "Started setting a new list for: " + sFeedItem);

            rssItems = sRssItemsHandler.getRssItems(feedItemId);

            if (rssItems == null) {
                return false;
            }

            if (rssItems.isEmpty()) {
                Log.w(TAG, "There are no rss items...");
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                mItemsView.invalidate();
                sRssAdapter.setData(rssItems);
            }
        }
    }
}
