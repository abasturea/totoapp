package com.alex.totoapp.rssapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.alex.totoapp.rssitem.RssItem;

import java.util.List;

public class RssAdapter extends ArrayAdapter<RssItem> {

    // TODO: BaseAdapter ?
    public RssAdapter(Context context, int textViewResourceId, List<RssItem> objects) {
        super(context, textViewResourceId, objects);
    }
}
