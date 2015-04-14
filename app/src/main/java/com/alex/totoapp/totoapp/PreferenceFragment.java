package com.alex.totoapp.totoapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.alex.totoapp.R;

public class PreferenceFragment extends android.preference.PreferenceFragment {


    public static final String SHARED_PREFERENCES_NAME = "toto_pref";

    private SharedPreferences mSharedPreferences;

    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        mActivity = getActivity();

        mSharedPreferences = mActivity.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
