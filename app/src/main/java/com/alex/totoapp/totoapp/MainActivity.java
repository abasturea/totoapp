package com.alex.totoapp.totoapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alex.totoapp.R;
import com.alex.totoapp.totoadapters.FeedAdapter;
import com.alex.totoapp.totoitems.FeedItem;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final String RSS_FEED_LINK = "link";
    private static final String TITLE = "title";

    private DrawerLayout drawerLayout = null;
    private ListView drawerList = null;
    private ActionBarDrawerToggle drawerToggle = null;

    private CharSequence drawerTitle;
    private CharSequence title;

    private ArrayList<FeedItem> feedItems = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");

        if(savedInstanceState != null && savedInstanceState.getCharSequence(TITLE) != null) {
            title = savedInstanceState.getCharSequence(TITLE);
        } else {
            title = getTitle();
        }

        drawerTitle = getTitle();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerList = (ListView) findViewById(R.id.list_slidermenu);
        drawerList.setOnItemClickListener(new SlideMenuClickListener());

        feedItems = new ArrayList<FeedItem>();
        /* hardcoded */
        feedItems.add(new FeedItem("Realitatea", "http://rss.realitatea.net/stiri.xml"));
        feedItems.add(new FeedItem("ProTv", "http://www.protv.ro/rss"));

        FeedAdapter feedAdapter = new FeedAdapter(getApplicationContext(), feedItems);
        drawerList.setAdapter(feedAdapter);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setTitle(title);
        }

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        if (getActionBar() != null) {
            getActionBar().setTitle(this.title);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(TITLE, title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
        }

        private void displayView(int position) {

            Fragment fragment = new RssFeedFragment();

            Log.i(TAG, "Creating fragment");

            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putString(RSS_FEED_LINK, feedItems.get(position).getUrl());
            fragment.setArguments(fragmentArgs);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            drawerList.setItemChecked(position, true);
            drawerList.setSelection(position);
            setTitle(feedItems.get(position).getTitle());
            drawerLayout.closeDrawer(drawerList);
        }
    }
}