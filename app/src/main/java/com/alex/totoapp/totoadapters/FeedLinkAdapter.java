package com.alex.totoapp.totoadapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alex.totoapp.R;
import com.alex.totoapp.totoitems.FeedLinkItem;

import java.util.ArrayList;
import java.util.List;

public class FeedLinkAdapter extends ArrayAdapter<FeedLinkItem> {

    private final Activity mActivity;
    private List<FeedLinkItem> mFeedLinkList;

    public FeedLinkAdapter(Activity activity, ArrayList<FeedLinkItem> objects) {
        super(activity, R.layout.feed_link_item, objects);
        mActivity = activity;
        mFeedLinkList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            view = inflater.inflate(R.layout.feed_link_item, null);
        } else {
            view = convertView;
        }

        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.title = (TextView) view.findViewById(R.id.searched_feed_title);
        viewHolder.title.setText(mFeedLinkList.get(position).getTitle());

        return view;
    }

    public void setFeedLinkItems(ArrayList<FeedLinkItem> feedLinks) {
        mFeedLinkList.clear();
        mFeedLinkList.addAll(feedLinks);

        notifyDataSetChanged();
    }

    private static class ViewHolder{
        protected TextView title;
    }
}
