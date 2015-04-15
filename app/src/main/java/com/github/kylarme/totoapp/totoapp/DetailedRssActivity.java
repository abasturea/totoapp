package com.github.kylarme.totoapp.totoapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kylarme.totoapp.R;
import com.github.kylarme.totoapp.totoadapters.RssAdapter;
import com.github.kylarme.totoapp.totoitems.RssItem;
import com.github.kylarme.totoapp.totoproviders.RssItemsHandler;
import com.squareup.picasso.Picasso;

public class DetailedRssActivity extends ActionBarActivity implements View.OnClickListener {

    private RssItem mRssItem;
    private String mLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detailed);

        Intent intent = getIntent();
        Long rssItemId = intent.getLongExtra(RssAdapter.RSS_ITEM_ID, -1);
        RssItemsHandler rssItemsHandler = new RssItemsHandler(DetailedRssActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (rssItemId != -1) {
            mRssItem = rssItemsHandler.getRssItem(rssItemId);
        }

        if (mRssItem != null) {
            final String title = mRssItem.getHeadline();

            mLink = mRssItem.getLink();

            getSupportActionBar().setTitle(title);

            final String description = mRssItem.getDescription();

            TextView titleView = (TextView) findViewById(R.id.detailed_title);
            TextView descriptionView = (TextView) findViewById(R.id.detailed_description);

            loadImage();

            titleView.setText(title);

            descriptionView.setText(description);

            titleView.setOnClickListener(this);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        loadImage();
    }

    @Override
    public void onClick(View v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mLink));
        startActivity(browserIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadImage() {
        final String imageLink = mRssItem.getImageLink();
        ImageView imageView = (ImageView) findViewById(R.id.detailed_image);

        int orientation = Utils.Device.getDeviceScreenOrientation(DetailedRssActivity.this);
        int device = Utils.Device.getDeviceType(DetailedRssActivity.this);

        if (device == Utils.Device.PHONE) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (!imageLink.equals("")) {
                    Picasso.with(DetailedRssActivity.this).load(imageLink).resize(1100, 500).centerCrop().into(imageView);
                }
            } else {
                if (!imageLink.equals("")) {
                    Picasso.with(DetailedRssActivity.this).load(imageLink).resize(650, 350).centerCrop().into(imageView);
                }
            }
        }

        if (device == Utils.Device.TABLET) {
            if (!imageLink.equals("")) {
                Picasso.with(DetailedRssActivity.this).load(imageLink).resize(1100, 500).centerCrop().into(imageView);
            }
        }

        imageView.setOnClickListener(this);
    }
}
