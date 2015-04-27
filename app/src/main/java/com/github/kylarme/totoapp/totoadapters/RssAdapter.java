package com.github.kylarme.totoapp.totoadapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kylarme.totoapp.R;
import com.github.kylarme.totoapp.totoapp.DetailedRssActivity;
import com.github.kylarme.totoapp.totoitems.RssItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.RssItemViewHolder> {

    public static final String RSS_ITEM_ID = "rss_item_id";

    private final Context mActivity;

    private final boolean mShowImage;

    private List<RssItem> mRssItemsList;

    public RssAdapter(Activity activity) {
        mActivity = activity;
        mRssItemsList = new ArrayList<RssItem>();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);

        String showImagePref = mActivity.getResources().getString(R.string.pref_show_image);

        mShowImage = sharedPreferences.getBoolean(showImagePref, true);
    }

    @Override
    public RssItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_rss_item, viewGroup, false);

        return new RssItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RssItemViewHolder rssItems, int i) {
        final RssItem rssItem = mRssItemsList.get(i);
        rssItems.mTitle.setText(rssItem.getHeadline());
        rssItems.mImage.setImageDrawable(null);

        if (mShowImage) {
            String imageLink = rssItem.getImageLink();

            if (!imageLink.equals("")) {
                Picasso.with(mActivity).load(imageLink).resize(650, 350).centerInside().into(rssItems.mImage);
            }
        }

        rssItems.setOnClickListener(new RssItemViewHolder.OnCardClickListener() {
            @Override
            public void onCardClick() {

                Intent detailedRssActivity = new Intent(mActivity, DetailedRssActivity.class);
                detailedRssActivity.putExtra(RSS_ITEM_ID, rssItem.getId());

                mActivity.startActivity(detailedRssActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRssItemsList.size();
    }

    public void addItem(int i, RssItem item) {
        mRssItemsList.add(i, item);
        notifyItemInserted(i);
    }

    public void deleteItem(int i) {
        mRssItemsList.remove(i);
        notifyItemRemoved(i);
    }

    public void moveItem(int i, int location) {
        move(mRssItemsList, i, location);
        notifyItemMoved(i, location);
    }

    private void move(List<RssItem> data, int a, int b) {
        RssItem temp = data.remove(a);
        data.add(b, temp);
    }

    public void setData(final List<RssItem> data) {
        // Remove all deleted items.
        for (int i = mRssItemsList.size() - 1; i >= 0; --i) {
            if (getLocation(data, mRssItemsList.get(i)) < 0) {
                deleteItem(i);
            }
        }

        // Add and move items.
        for (int i = 0; i < data.size(); ++i) {
            RssItem item = data.get(i);
            int location = getLocation(mRssItemsList, item);
            if (location < 0) {
                addItem(i, item);
            } else if (location != i) {
                moveItem(i, location);
            }
        }
    }

    private int getLocation(List<RssItem> data, RssItem item) {
        for (int j = 0; j < data.size(); ++j) {
            RssItem newItem = data.get(j);
            if (item.equals(newItem)) {
                return j;
            }
        }

        return -1;
    }

    public static class RssItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final View mItemView;

        protected TextView mTitle = null;
        protected ImageView mImage = null;

        protected OnCardClickListener mListener;

        public RssItemViewHolder(View itemView) {
            super(itemView);

            mItemView = itemView;

            mTitle = (TextView) itemView.findViewById(R.id.rss_item_title);
            mImage = (ImageView) itemView.findViewById(R.id.rss_item_image);
        }

        public void setOnClickListener(OnCardClickListener listener) {
            mItemView.setOnClickListener(this);

            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            mListener.onCardClick();
        }

        public static interface OnCardClickListener {
            public void onCardClick();
        }
    }
}
