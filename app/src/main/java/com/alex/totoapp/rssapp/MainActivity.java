package com.alex.totoapp.rssapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alex.totoapp.R;
import com.alex.totoapp.rssitem.RssItem;
import com.alex.totoapp.rssloader.RssLoader;
import com.alex.totoapp.rssloader.RssLoaderImpl;

import java.util.ArrayList;

public class MainActivity extends Activity {

    public static final String RSS_FEED_LINK = "link";
    public static final int RSS_FEED_LINK_ID = 0;

    private static final String TAG = "MainActivity";

    private static ArrayList<RssItem> rssItems = new ArrayList<RssItem>();
    private RssAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new RssAdapter(this, android.R.layout.simple_list_item_1, rssItems);

        Button setButton = (Button) findViewById(R.id.setButton);
        final TextView urlText = (TextView) findViewById(R.id.urlText);

        listView.setAdapter(adapter);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Started setting a new list for: " + urlText);

                RssLoader rssLoader = new RssLoaderImpl(MainActivity.this, adapter);
                Bundle loaderBundle = new Bundle();
                loaderBundle.putString(RSS_FEED_LINK, urlText.getText().toString());

                if (getLoaderManager().getLoader(RSS_FEED_LINK_ID) != null) {
                    getLoaderManager().restartLoader(RSS_FEED_LINK_ID, loaderBundle, rssLoader);
                } else {
                    getLoaderManager().initLoader(RSS_FEED_LINK_ID, loaderBundle, rssLoader);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Clicked on position:" + position);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssItems.get(position).getLink()));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        adapter.notifyDataSetChanged();
    }
}

