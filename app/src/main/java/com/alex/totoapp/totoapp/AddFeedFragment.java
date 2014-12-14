package com.alex.totoapp.totoapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.totoapp.R;

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

        return rootView;
    }
}
