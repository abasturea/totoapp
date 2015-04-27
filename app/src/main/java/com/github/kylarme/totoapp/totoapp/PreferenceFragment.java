package com.github.kylarme.totoapp.totoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.SwitchPreference;

import com.github.kylarme.totoapp.R;

public class PreferenceFragment extends android.preference.PreferenceFragment {

    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        mActivity = getActivity();

        final SwitchPreference showImage = (SwitchPreference) findPreference(getResources().getString(R.string.pref_show_image));
        final SwitchPreference autoRefreshRate = (SwitchPreference) findPreference(getResources().getString(R.string.pref_auto_refresh));
        final ListPreference refreshRate = (ListPreference) findPreference(getResources().getString(R.string.pref_refresh_rate));

        showImage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return true;
            }
        });

        autoRefreshRate.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean value = (Boolean) newValue;

                int serviceStatus = Utils.UpdateService.getServiceStatus(mActivity, UpdateRssItemsService.class);

                Intent updateService = new Intent(mActivity, UpdateRssItemsService.class);

                if (value) {
                    if (serviceStatus == Utils.UpdateService.STOPPED) {
                        mActivity.startService(updateService);
                    }
                } else {
                    if (serviceStatus == Utils.UpdateService.RUNNING) {
                        mActivity.stopService(updateService);
                    }
                }

                return true;
            }
        });

        refreshRate.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                String value = (String) newValue;

                int updateTime = Utils.UpdateService.mapUpdateTime(mActivity, value);

                UpdateRssItemsService.setUpdateTime(updateTime);

                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mActivity = null;
    }
}
