package com.alex.totoapp.totoapp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alex.totoapp.R;
import com.alex.totoapp.totoadapters.FeedLinkAdapter;
import com.alex.totoapp.totoitems.FeedItem;
import com.alex.totoapp.totoitems.FeedLinkItem;
import com.alex.totoapp.totoparsers.FeedLinkParser;
import com.alex.totoapp.totoproviders.FeedItemsHandler;

import java.util.ArrayList;

public class AddFeedFragment extends Fragment {

    private static final String TAG = "AddFeedFragment";

    private Activity mActivity;
    private View mRootView = null;

    private FeedLinkAdapter mFeedLinkAdapter = null;
    private ArrayList<FeedLinkItem> mFeedLinks = null;

    private FeedItemsHandler mFeedHandler = null;

    public AddFeedFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView");

        mActivity = getActivity();

        Utils.Device.setScreenOrientation(mActivity, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_add_feed, container, false);
        }

        mFeedHandler = new FeedItemsHandler(mActivity);

        mFeedLinks = new ArrayList<FeedLinkItem>();

        mFeedLinkAdapter = new FeedLinkAdapter(mActivity, mFeedLinks);

        ListView listViewFeedLink = (ListView) mRootView.findViewById(R.id.searched_feeds_list);
        listViewFeedLink.setAdapter(mFeedLinkAdapter);

        final Button searchFeed = (Button) mRootView.findViewById(R.id.search_feed_button);

        final EditText feedUrlEditText = (EditText) mRootView.findViewById(R.id.feed_url_edit_text);
        final EditText feedTitleEditText = ((EditText) mRootView.findViewById(R.id.feed_title_edit_text));

        searchFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideKeyboard(mActivity);

                new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Void... params) {

                        String feedUrl;
                        ArrayList<FeedLinkItem> linksItems;

                        FeedLinkParser feedLinkParser = new FeedLinkParser();

                        feedUrl = feedUrlEditText.getText().toString().trim();

                        if (feedUrl.equals("")) {
                            return false;
                        }

                        if (feedLinkParser.isFeedLink(feedUrl)) {
                            mFeedLinks.add(new FeedLinkItem(feedUrl, feedUrl));
                            return true;
                        }

                        feedLinkParser.connectToUrl(feedUrl);

                        linksItems = feedLinkParser.getAllRssLinks();

                        if (linksItems.size() != 0) {
                            mFeedLinks.addAll(linksItems);
                            return true;
                        }

                        linksItems = feedLinkParser.getAllRssLinks(feedLinkParser.getAllLinks());

                        if (linksItems == null) {
                            return false;
                        }

                        if (linksItems.size() != 0) {
                            mFeedLinks.addAll(linksItems);
                            return true;
                        }

                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);

                        if (!aBoolean) {
                            Utils.showToast(mActivity, "The link returned nothing.", Toast.LENGTH_SHORT);
                        } else {
                            mFeedLinkAdapter.notifyDataSetChanged();

                            feedUrlEditText.getText().clear();
                        }
                    }
                }.execute();
            }
        });

        listViewFeedLink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.hideKeyboard(mActivity);

                final FeedItem feedItem = new FeedItem();

                String feedTitle = feedTitleEditText.getText().toString();

                if (feedTitle.equals("")) {
                    Utils.showToast(mActivity, "Please add a title.", Toast.LENGTH_SHORT);
                    return;
                }

                feedItem.setTitle(feedTitle);
                feedItem.setLink(mFeedLinks.get(position).getUrl());

                feedTitleEditText.getText().clear();

                mFeedLinkAdapter.clear();

                mFeedHandler.addFeed(feedItem);
            }
        });

        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mRootView != null) {
            mRootView = null;
        }

        if (mActivity != null) {
            mActivity = null;
        }
    }
}
