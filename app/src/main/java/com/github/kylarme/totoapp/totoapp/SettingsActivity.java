package com.github.kylarme.totoapp.totoapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.github.kylarme.totoapp.R;

public class SettingsActivity extends Activity {

    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");

        setContentView(R.layout.fragment_settings);
    }
}
