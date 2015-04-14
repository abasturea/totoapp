package com.alex.totoapp.totoapp;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alex.totoapp.R;
import com.alex.totoapp.totoadapters.FeedAdapter;
import com.alex.totoapp.totoitems.FeedItem;
import com.alex.totoapp.totoloaders.FeedItemsLoader;
import com.alex.totoapp.totoloaders.RssItemsLoader;
import com.alex.totoapp.totoproviders.FeedItemsHandler;

import java.util.ArrayList;

public class NavigationDrawerFragment extends Fragment {

    private static final String TAG = "NavDrawerFragment";

    private Activity mActivity = null;
    private ListView mDrawerList = null;

    private ArrayList<FeedItem> mFeedItems = null;

    private DrawerLayout mDrawerLayout = null;

    private Toolbar mToolbar = null;

    private LoaderManager mLoaderManager;

    private FeedItemsHandler mFeedHandler = null;

    public NavigationDrawerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");

        mActivity = getActivity();

        mLoaderManager = getLoaderManager();

        mFeedHandler = new FeedItemsHandler(mActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView");

        Utils.Device.setScreenOrientation(mActivity, ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = mActivity.getMenuInflater();
        inflater.inflate(R.menu.feed_edit_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                Toast.makeText(mActivity, "Delete " + info.position, Toast.LENGTH_LONG).show();
                mFeedHandler.deleteFeedItem(mFeedItems.get(info.position).getId());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mLoaderManager.destroyLoader(FeedItemsLoader.sLoaderId);
    }

    public void setUp(DrawerLayout drawerLayout, Toolbar toolbar) {

        Log.i(TAG, "setUp");

        mDrawerLayout = drawerLayout;
        mToolbar = toolbar;
        mDrawerList = (ListView) mActivity.findViewById(R.id.list_slidermenu);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(mActivity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Utils.hideKeyboard(mActivity);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(drawerToggle);

        drawerToggle.syncState();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        registerForContextMenu(mDrawerList);

        mFeedItems = new ArrayList<FeedItem>();

        FeedAdapter feedAdapter = new FeedAdapter(mActivity, mFeedItems);

        mDrawerList.setAdapter(feedAdapter);

        FeedItemsLoader feedItemsLoader = new FeedItemsLoader(mActivity, feedAdapter);
        mLoaderManager.initLoader(FeedItemsLoader.sLoaderId, null, feedItemsLoader);
        Loader feedsLoader = mLoaderManager.getLoader(FeedItemsLoader.sLoaderId);

        if (feedsLoader != null) {
            feedsLoader.startLoading();
        }
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(TAG, "onItemClick: " + position);

            displayFeedView(position);
        }

        private void displayFeedView(int position) {
            Fragment fragment = new RssFeedFragment();

            Log.i(TAG, "Creating fragment");

            Bundle fragmentArgs = new Bundle();

            FeedItem feedItem = mFeedItems.get(position);

            fragmentArgs.putSerializable(RssItemsLoader.RSS_FEED_ITEM, feedItem);
            fragment.setArguments(fragmentArgs);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commitAllowingStateLoss();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mToolbar.setTitle(feedItem.getTitle());
            mDrawerLayout.closeDrawers();
        }
    }
}
