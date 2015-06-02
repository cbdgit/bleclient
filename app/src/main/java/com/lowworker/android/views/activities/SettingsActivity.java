package com.lowworker.android.views.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.lowworker.android.R;
import com.lowworker.android.patternTools.service.BeaconTrackServiceSettings;


/**
 * Created by lowworker on 2015/5/8.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener , SharedPreferences.OnSharedPreferenceChangeListener   {
    public static final String PREF_KEY_BOOT_START = "pref_key_boot_start";
    public static final String PREF_KEY_WORK_BACKGROUND = "pref_key_background_work";
    public static final String PREF_KEY_SEND_NOTIFICATION = "pref_key_send_notificatoin";
    public static final String PREF_KEY_CONTACT = "pref_key_contact";
    public static final String PREF_KEY_ABOUT = "pref_key_about";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.view_settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addPreferencesFromResource(R.xml.pref_setting);
    }


    @Override
    public boolean onPreferenceClick(final Preference preference) {
        if (preference.getKey().equals(PREF_KEY_CONTACT)) {
            return true;
        } else if (preference.getKey().equals(PREF_KEY_ABOUT)) {
            return true;
        } else {
            return false;
        }

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREF_KEY_BOOT_START)) {
            boolean isChecked = sharedPreferences.getBoolean(PREF_KEY_BOOT_START, true);

            if (isChecked) {
                BeaconTrackServiceSettings.setStartOnDeviceBoot(SettingsActivity.this, true);
            } else {
                BeaconTrackServiceSettings.setStartOnDeviceBoot(SettingsActivity.this, false);
            }
            Log.d("StartOnDeviceBoot",BeaconTrackServiceSettings.shouldStartOnDeviceBoot(getApplicationContext()) + "");

        }else  if (key.equals(PREF_KEY_WORK_BACKGROUND)) {
            boolean isChecked = sharedPreferences.getBoolean(PREF_KEY_WORK_BACKGROUND, true);
            if (isChecked) {
                BeaconTrackServiceSettings.setAlwaysWorkInBackground(SettingsActivity.this, true);
            } else {
                BeaconTrackServiceSettings.setAlwaysWorkInBackground(SettingsActivity.this, false);
            }
            Log.d("AlwaysWorkInBackground", BeaconTrackServiceSettings.shouldAlwaysWorkInBackground(getApplicationContext()) + "");

        }
        else  if (key.equals(PREF_KEY_SEND_NOTIFICATION)) {
            boolean isChecked = sharedPreferences.getBoolean(PREF_KEY_SEND_NOTIFICATION, true);
            if (isChecked) {
                BeaconTrackServiceSettings.setSendNotification(SettingsActivity.this, true);
            } else {
                BeaconTrackServiceSettings.setSendNotification(SettingsActivity.this, false);
            }
            Log.d("SendNotification", BeaconTrackServiceSettings.shouldSendNotification(getApplicationContext()) + "");

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
// Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
// Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}

