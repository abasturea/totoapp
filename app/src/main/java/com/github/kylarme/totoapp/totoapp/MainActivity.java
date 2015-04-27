package com.github.kylarme.totoapp.totoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.kylarme.totoapp.R;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");

        setContentView(R.layout.activity_main);

        Utils.Device.setScreenOrientation(this, ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String autoRefreshPref = getResources().getString(R.string.pref_auto_refresh);

        boolean autoRefresh = sharedPreferences.getBoolean(autoRefreshPref, true);

        if (autoRefresh) {

            int serviceStatus = Utils.UpdateService
                    .getServiceStatus(MainActivity.this, UpdateRssItemsService.class);

            if (serviceStatus == Utils.UpdateService.STOPPED) {

                Intent updateRssItemsService = new Intent(this, UpdateRssItemsService.class);

                startService(updateRssItemsService);
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.layout.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i(TAG, "onCreateOptionsMenu");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh: {

                RssFeedFragment.updateRssItems();

                return true;
            }

            case R.id.add_feed: {

                displayFragment(new AddFeedFragment());

                return true;
            }

            case R.id.settings: {

                startActivity(new Intent(MainActivity.this, SettingsActivity.class));

                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commitAllowingStateLoss();
    }
}