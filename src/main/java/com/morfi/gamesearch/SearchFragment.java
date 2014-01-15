package com.morfi.gamesearch;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Morfi on 06.01.14.
 */
public class SearchFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}