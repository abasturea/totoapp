package com.alex.totoapp.totoadapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alex.totoapp.R;
import com.alex.totoapp.totoitems.FeedItem;

import java.util.ArrayList;

public class FeedAdapter extends BaseAdapter {

    private Context context = null;
    private ArrayList<FeedItem> feedDrawerItems = null;

    public FeedAdapter(Context context, ArrayList<FeedItem> feedDrawerItems) {
        this.context = context;
        this.feedDrawerItems = feedDrawerItems;
    }

    public void setFeedDrawerItems(ArrayList<FeedItem> feedDrawerItems) {
        this.feedDrawerItems.clear();
        this.feedDrawerItems.addAll(feedDrawerItems);
    }

    @Override
    public int getCount() {
        return feedDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return feedDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_feed_item, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(feedDrawerItems.get(position).getTitle());

        return convertView;
    }
}