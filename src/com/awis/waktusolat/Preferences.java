package com.awis.waktusolat;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
//import android.preference.ListPreference;

import android.preference.ListPreference;
import android.preference.PreferenceActivity;


public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	private ListPreference mListPreference;
	static String m;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		mListPreference = (ListPreference)getPreferenceScreen().findPreference("locationPref");
        m = mListPreference.getEntry().toString();
        }
	
	@Override
    protected void onResume() {
        super.onResume();
        m = mListPreference.getEntry().toString();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes            
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);    
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
		if (key.equals("locationPref")) {
	          m = mListPreference.getEntry().toString();
	        }
		
	}
	public static String getM(){
		//new Preferences();
		return m;
	}
}

