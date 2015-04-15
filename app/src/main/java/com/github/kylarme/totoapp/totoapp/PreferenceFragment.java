package com.github.kylarme.totoapp.totoapp;

import android.os.Bundle;

import com.github.kylarme.totoapp.R;

public class PreferenceFragment extends android.preference.PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

    }
}
