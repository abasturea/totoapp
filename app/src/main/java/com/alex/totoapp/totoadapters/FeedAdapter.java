package com.alex.totoapp.totoadapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alex.totoapp.R;
import com.alex.totoapp.totoitems.FeedItem;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends BaseAdapter {

    private final Activity mActivity;
    private List<FeedItem> mFeedDrawerItems;

    public FeedAdapter(Activity activity, ArrayList<FeedItem> feedDrawerItems) {
        mActivity = activity;
        mFeedDrawerItems = feedDrawerItems;
    }

    public void setFeedDrawerItems(ArrayList<FeedItem> feedDrawerItems) {
        mFeedDrawerItems.clear();
        mFeedDrawerItems.addAll(feedDrawerItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFeedDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mFeedDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_feed_item, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(mFeedDrawerItems.get(position).getTitle());

        return convertView;
    }
}