package com.vortexwolf.chan.settings;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.vortexwolf.chan.R;
import com.vortexwolf.chan.common.Factory;
import com.vortexwolf.chan.common.MainApplication;
import com.vortexwolf.chan.common.utils.StringUtils;
import com.vortexwolf.chan.services.MyTracker;

public class ApplicationPreferencesActivity extends PreferenceActivity {
    private static final String TAG = "ApplicationPreferencesActivity";
    
    private ApplicationSettings mSettings;
    private Resources mResources;
    private SharedPreferences mSharedPreferences;
    private SharedPreferenceChangeListener mSharedPreferenceChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainApplication application = (MainApplication) this.getApplication();
        this.mSettings = application.getSettings();
        this.mResources = application.getResources();
        
        this.setTheme(this.mSettings.getTheme()); // set theme before creating
        super.onCreate(savedInstanceState);
        
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        this.mSharedPreferenceChangeListener = new SharedPreferenceChangeListener();

        this.addPreferencesFromResource(R.xml.preferences);

        this.mSharedPreferenceChangeListener.onSharedPreferenceChanged(this.mSharedPreferences, this.mResources.getString(R.string.pref_theme_key));
        this.mSharedPreferenceChangeListener.onSharedPreferenceChanged(this.mSharedPreferences, this.mResources.getString(R.string.pref_text_size_key));

        Factory.getContainer().resolve(MyTracker.class).trackActivityView(TAG);
        
        this.updateNameSummary();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.mSharedPreferences.registerOnSharedPreferenceChangeListener(this.mSharedPreferenceChangeListener);
        this.mSettings.startTrackChanges();
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this.mSharedPreferenceChangeListener);
        this.mSettings.stopTrackChanges();
    }
    
    private class SharedPreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(ApplicationPreferencesActivity.this.mResources.getString(R.string.pref_theme_key)) || key.equals(ApplicationPreferencesActivity.this.mResources.getString(R.string.pref_text_size_key))) {
                ListPreference preference = (ListPreference) ApplicationPreferencesActivity.this.getPreferenceManager().findPreference(key);
                preference.setSummary(preference.getEntry());
            } else if (key.equals(ApplicationPreferencesActivity.this.mResources.getString(R.string.pref_name_key))) {
                ApplicationPreferencesActivity.this.updateNameSummary();
            }
        }
    }
    
    private void updateNameSummary(){
        EditTextPreference preference = (EditTextPreference) this.getPreferenceManager().findPreference(this.mResources.getString(R.string.pref_name_key));
        String text = preference.getText();
        if (!StringUtils.isEmpty(text)) {
            preference.setSummary(text);
        } else {
            preference.setSummary(ApplicationPreferencesActivity.this.mResources.getString(R.string.pref_name_summary, ""));
        }
    }
}
