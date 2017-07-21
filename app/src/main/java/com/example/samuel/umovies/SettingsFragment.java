package com.example.samuel.umovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.samuel.umovies.R;

import java.util.Set;

/**
 * Created by Samuel on 20/07/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        int count = getPreferenceScreen().getPreferenceCount();
        for(int i = 0;i<count;i++){
            Preference preference = getPreferenceScreen().getPreference(i);

            if(!(preference instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(getString(R.string.list_preference_key),"");
                SetPreferenceSummary(preference,value);
            }

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void SetPreferenceSummary(Preference preference, String Value){
        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int index= listPreference.findIndexOfValue(Value);
            if(index >=0) {
                listPreference.setSummary(listPreference.getEntries()[index]);
            }
        }


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if(preference != null) {

            if (preference instanceof ListPreference) {

                String value = sharedPreferences.getString(getString(R.string.list_preference_key), "");
                SetPreferenceSummary(preference, value);

            }
        }
    }
}
