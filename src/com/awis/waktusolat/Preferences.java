package com.awis.waktusolat;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;


public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	private ListPreference mListPreference;
	private GoogleAnalyticsTracker tracker;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setTheme(R.style.Theme_Sherlock);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		tracker = WaktuSolatNew.tracker;
		tracker.trackPageView("/WaktuSolat_setting");
        tracker.dispatch();
		addPreferencesFromResource(R.xml.preferences);
		mListPreference = (ListPreference)getPreferenceScreen().findPreference("locationPref");
        }
	
	@SuppressWarnings("deprecation")
	@Override
    protected void onResume() {
        super.onResume();
        
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes            
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);    
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
		if  (key.equals("locationPref")){
			tracker.trackEvent(
    	            "Setting",  // Category
    	            "location changed",  // Action
    	            mListPreference.getValue(), // Label
    	            1);       // Value
    		tracker.dispatch();
			this.finish();
		}
		
		
	}
	
//	@Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                this.finish();
//                overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    this.finish();
	    finish();
	    overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
	}

}

