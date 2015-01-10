package com.alex.totoapp.totoapp;

import android.app.Fragment;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alex.totoapp.R;
import com.alex.totoapp.totoproviders.FeedContentProvider;

public class AddFeedFragment extends Fragment{

    private static final String TAG = "AddFeedFragment";
    private static View rootView = null;

    public AddFeedFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_add_feed, container, false);
        }

        final Button addFeed = (Button)rootView.findViewById(R.id.add_feed_button);

        addFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();

                cv.put(FeedContentProvider.FEED_TITLE, ((EditText)rootView.findViewById(R.id.feed_title_edit_text)).getText().toString());
                cv.put(FeedContentProvider.FEED_URL, ((EditText)rootView.findViewById(R.id.feed_url_edit_text)).getText().toString());

                Uri insertedUri = getActivity().getContentResolver().insert(FeedContentProvider.CONTENT_URI, cv);
            }
        });

        final Button deleteFeed = (Button)rootView.findViewById(R.id.delete_feed_button);

        deleteFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] selectionArgs = {((EditText)rootView.findViewById(R.id.feed_delete_edit_text)).getText().toString()};

                getActivity().getContentResolver().delete(FeedContentProvider.CONTENT_URI, null, selectionArgs);
            }
        });

        return rootView;
    }
}
