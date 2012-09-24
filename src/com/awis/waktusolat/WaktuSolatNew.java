package com.awis.waktusolat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.StaleDataException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class WaktuSolatNew extends SherlockActivity {
	
	private WaktuAdapter adapter;
	public final static int THEME = R.style.Theme_Sherlock;
	private DataProvider dp;
	private ListView listWaktu;
	private ProgressDialog pDialog;
	private ContentDBAdapter db;
	private SharedPreferences prefs ;
	private ArrayList<HashMap<String,String>> listTemp = new ArrayList<HashMap<String,String>>();
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<String> listHighlight = new ArrayList<String>();
	private String locationPrefs;
	private boolean time12;
	private int mYear, mMonth, mDay, realMonth;
	private String tarikhHariIni;
	String tarikhD, hariD, imsakT,subuhT,syurukT,zohorT,asarT,maghribT,isyakT;
	String imsakF,subuhF,syurukF,zohorF,asarF,maghribF,isyakF;
	private Map<String,String> locs = new HashMap<String, String>();
	protected static GoogleAnalyticsTracker tracker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(THEME);
		setContentView(R.layout.main_new_layout);
		
		tracker = GoogleAnalyticsTracker.getInstance();

	    // Start the tracker in manual dispatch mode...
	    tracker.startNewSession("UA-32201194-1", this);

	    
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		time12 = prefs.getBoolean("timeFormat", true);
		db = new ContentDBAdapter(this);
		adapter = new WaktuAdapter(this, list);
		dp = new DataProvider();
		initUI();
		initVar();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		tracker.stopSession();
	}

	public void createService(){
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE); 
		Intent i = new Intent(this, NotificationService.class); 
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0); am.cancel(pi);
		int minutes = 1;
		// by my own convention, minutes <= 0 means notifications are disabled 
		if (minutes > 0) { 
			am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
					SystemClock.elapsedRealtime() + minutes*60*1000, minutes*60*1000, pi); }
	}
	
	
	
	public void initVar(){
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        realMonth = mMonth+1;
        locationPrefs = prefs.getString("locationPref", "sgr03");
        time12 = prefs.getBoolean("timeFormat", true);
        if (realMonth<10){
        	if (mDay<10){
        		tarikhHariIni = new StringBuilder()
        		.append("0").append(mDay).append("-")
                .append("0").append(mMonth + 1).append("-")
                .append(mYear).toString();
        	}else {
        		tarikhHariIni = new StringBuilder()
                .append(mDay).append("-")
                .append("0").append(mMonth + 1).append("-")
                .append(mYear).toString();
        	}
        	
        }else{
        	if (mDay<10){
        		tarikhHariIni = new StringBuilder()
        		.append("0").append(mDay).append("-")
                // Month is 0 based so add 1
                .append(mMonth + 1).append("-")
                .append(mYear).toString();
        	}else {
        		tarikhHariIni = new StringBuilder()
                .append(mDay).append("-")
                // Month is 0 based so add 1
                .append(mMonth + 1).append("-")
                .append(mYear).toString();
        	}
        	
        }
        
	}
	
	public void startFetch(){
		pDialog.setMessage("Downloading...");
		pDialog.show();
		new getDataTask().execute();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.onCreate(null);
		initVar();
		getFromDBandPopulate();
        Log.d("tarikh:", tarikhHariIni);
        adapter = new WaktuAdapter(this, list);
        createService();
        tracker.trackPageView("/waktuMain");
        
        tracker.dispatch();
	}

	public void initUI(){
		getLocationNames();
		pDialog = new ProgressDialog(this);
		listWaktu = (ListView) findViewById(R.id.listWaktu);
		listWaktu.setAdapter(adapter);
	}
	
	public void formatTimeAndPopulate(){
		list.clear();
		SimpleDateFormat formattedTime = new SimpleDateFormat("h:mm a");
		formattedTime.setTimeZone(TimeZone.getTimeZone("MYT"));

		SimpleDateFormat timeBefore = new SimpleDateFormat("H:m");
		timeBefore.setTimeZone(TimeZone.getTimeZone("MYT"));

		try {
			
			imsakF = formattedTime.format(timeBefore.parse(imsakT));
			subuhF = formattedTime.format(timeBefore.parse(subuhT));
			syurukF = formattedTime.format(timeBefore.parse(syurukT));
			zohorF = formattedTime.format(timeBefore.parse(zohorT));
			asarF = formattedTime.format(timeBefore.parse(asarT));
			maghribF = formattedTime.format(timeBefore.parse(maghribT));
			isyakF = formattedTime.format(timeBefore.parse(isyakT));
			
		} catch (Exception e) {
				e.printStackTrace();
				
			}
		
		list.add("Tarikh: "+hariD+", "+tarikhD);
		list.add(locs.get(locationPrefs));
		list.add("Imsak: "+imsakF);
		list.add("Subuh: "+subuhF);
		list.add("Syuruk: "+syurukF);
		list.add("Zohor: "+zohorF);
		list.add("Asar: "+asarF);
		list.add("Maghrib: "+maghribF);
		list.add("Isyak: "+isyakF);
		
		adapter.notifyDataSetChanged();
		populateForHightlight();
		
	}
	

	public void getLocationNames(){
    	
    	String[] locNames = getResources().getStringArray(R.array.location);
    	String[] locIds = getResources().getStringArray(R.array.location_values);
    	
    	for (int i = 0; i <  locIds.length; i++) {
    		locs.put(locIds[i], locNames[i]);
    		}
    }
	
	public void getAll(){
		listTemp.clear();
		listTemp = dp.getDataNew(realMonth, locationPrefs);
	}
	
	public void savetodb(){
		
		db.open();
		Cursor cursor  = db.fetchAllContents(realMonth, locationPrefs);
		if (cursor.getCount() == 0){
		try{
		for (int i=0;i<listTemp.size();i++){
		db.saveToDB(""+realMonth,
				listTemp.get(i).get("tarikh").toString(),
				listTemp.get(i).get("hari").toString(),
				listTemp.get(i).get("imsak").toString(),
				listTemp.get(i).get("subuh").toString(),
				listTemp.get(i).get("syuruk").toString(),
				listTemp.get(i).get("zohor").toString(),
				listTemp.get(i).get("asar").toString(),
				listTemp.get(i).get("maghrib").toString(),
				listTemp.get(i).get("isyak").toString(),
				locationPrefs);
		}
		
	}catch (SQLException e){
		 Log.e("databaseInsert", "SQLException");
	 }catch (StaleDataException e){
		 Log.e("databaseInsert", "StaleDataException");
	 }catch (IllegalMonitorStateException e){
		 Log.e("databaseInsert", "IllegalMonitorStateException");
	 }catch (IndexOutOfBoundsException e){
		 Log.e("databaseInsert", "IndexOutOfBoundsException");
	 }finally{
		 db.close();
		 cursor.close();
	 }
		}
	}
	
	public void getFromDBandPopulate(){
		list.clear();
		db.open();
		Cursor c = db.fetchOneContent(tarikhHariIni, locationPrefs);
		try{
			
	        int tarikh = c.getColumnIndex("tarikh");
	        int hari = c.getColumnIndex("hari");
	        int imsak = c.getColumnIndex("imsak");
	        int subuh = c.getColumnIndex("subuh");
	        int syuruk = c.getColumnIndex("syuruk");
	        int zohor = c.getColumnIndex("zohor");
	        int asar = c.getColumnIndex("asar");
	        int maghrib = c.getColumnIndex("maghrib");
	        int isyak = c.getColumnIndex("isyak");
	    	c.moveToFirst();
	    	
	    	if (c.getCount() == 1){
	    		tarikhD = c.getString(tarikh);
	    		hariD = c.getString(hari);
	    		imsakT = c.getString(imsak);
	            subuhT = c.getString(subuh);
	            syurukT = c.getString(syuruk);
	            zohorT = c.getString(zohor);
	            asarT = c.getString(asar);
	            maghribT = c.getString(maghrib);
	            isyakT = c.getString(isyak);
	            listHighlight.add(imsakT);
	            populateToList();
	    		Log.e("Count", ""+c.getCount());
	    	}else if(c.getCount() == 0){
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setMessage("Database waktu solat di "+locs.get(locationPrefs)+" untuk bulan ini tiada." +
	            		" Download waktu solat untuk bulan ini ?")
	                   .setCancelable(false)
	                   .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {
	                            dialog.cancel();
	                       }
	                   }).setPositiveButton("Ya", new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int id) {
	                            startFetch();
	                       }
	                   });
	            AlertDialog alert = builder.create();
	            alert.show();
	    		Log.e("Count", ""+c.getCount());
	    	}else {
	    		tarikhD = c.getString(tarikh);
	    		hariD = c.getString(hari);
	    		imsakT = c.getString(imsak);
	            subuhT = c.getString(subuh);
	            syurukT = c.getString(syuruk);
	            zohorT = c.getString(zohor);
	            asarT = c.getString(asar);
	            maghribT = c.getString(maghrib);
	            isyakT = c.getString(isyak);
	            populateToList();
	    		Log.e("DBWarning", "Warning: More than one table returned : "+c.getCount());
	    	}
	    	
	       	    
	        
		}catch (SQLException e){
			 Log.e("database", "SQLException");
		 }catch (CursorIndexOutOfBoundsException e){
			 Log.e("database", "CursorIndexOutOfBoundsException");
		 }catch (StaleDataException e){
			 Log.e("database", "StaleDataException");
		 }catch (IllegalStateException e){
			 Log.e("database", "IllegalStateException");
		 }catch (IllegalMonitorStateException e){
			 Log.e("database", "IllegalMonitorStateException");
		 }catch (IndexOutOfBoundsException e){
			 Log.e("database", "IndexOutOfBoundsException");
		}finally{
			 db.close();
			 c.close();
		 }
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu){
    	
    	menu.add("Qiblat")
        .setIcon(R.drawable.btn_qiblat1)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    	
    	menu.add("Tetapan")
        .setIcon(R.drawable.setting)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    	
    	
    	
//    	MenuInflater inflater = getSupportMenuInflater();
//    	inflater.inflate(R.menu.menu, menu);
    	return true;
    } 
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	if (item.getTitle().equals("Tetapan")){
    		Intent settingsActivity = new Intent(WaktuSolatNew.this,Preferences.class);
    		startActivity(settingsActivity);
    		tracker.trackEvent(
    	            "Clicks",  // Category
    	            "Setting",  // Action
    	            "clicked", // Label
    	            1);       // Value
    		tracker.dispatch();

    	}else if (item.getTitle().equals("Qiblat")){
    		Intent qibla = new Intent(WaktuSolatNew.this, QiblaActivity.class);
    		startActivity(qibla);
    		overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    		tracker.trackEvent(
    	            "Clicks",  // Category
    	            "Qiblat",  // Action
    	            "clicked", // Label
    	            1);       // Value
    		tracker.dispatch();
    	}else {
    		switch (item.getItemId()){
    	case R.id.lokasi : 
    		
    		
    		break;
    	case R.id.refresh :
    		
    		break;
    	}
    	}
    	
    	
    	return true;
    }
    
    public void populateForHightlight(){
    	listHighlight.clear();
    	listHighlight.add(imsakT);
    	listHighlight.add(subuhT);
    	listHighlight.add(syurukT);
    	listHighlight.add(zohorT);
    	listHighlight.add(asarT);
    	listHighlight.add(maghribT);
    	listHighlight.add(isyakT);
    	adapter.setHighlight(listHighlight, time12);
    	
    }
	
	public void populateToList(){
		if (time12){
			formatTimeAndPopulate();
			
		}else{
			list.add("Tarikh: "+hariD+", "+tarikhD);
			list.add(locs.get(locationPrefs));
			list.add("Imsak: "+imsakT);
			list.add("Subuh: "+subuhT);
			list.add("Syuruk: "+syurukT);
			list.add("Zohor: "+zohorT);
			list.add("Asar: "+asarT);
			list.add("Maghrib: "+maghribT);
			list.add("Isyak: "+isyakT);
			populateForHightlight();
			adapter.notifyDataSetChanged();
		}
	}
	
	public class getDataTask extends AsyncTask<Void,Void,String>{
		
		@Override
		protected String doInBackground(Void... params) {
			getAll();
			savetodb();
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.d("Downloading...","Downloading..."+values);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			if (listTemp.size() == 0){
				Toast.makeText(WaktuSolatNew.this, "Connection timeout. Masalah pada server www.e-solat.gov.my. Sila cuba lagi", Toast.LENGTH_SHORT).show();
			}
			initVar();
			WaktuSolatNew.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
				    getFromDBandPopulate();
				    adapter = new WaktuAdapter(WaktuSolatNew.this, list);
				    populateForHightlight();
				    listWaktu.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				    }
				});

			
			super.onPostExecute(result);
		}
	}
}
