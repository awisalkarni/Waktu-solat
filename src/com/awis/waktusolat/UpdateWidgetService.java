package com.awis.waktusolat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.StaleDataException;
import android.graphics.Color;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
	
	private String loc2;
	private String date2;
	private String imsak2;
	private String subuh2;
	private String syuruk2;
	private String zohor2;
	private String asar2;
	private String maghrib2;
	private String isyak2;
	private ContentDBAdapter db;
	private SharedPreferences prefs ;
	private RemoteViews remoteViews;
	private PendingIntent pendingIntent;
	private int mYear, mMonth, mDay, realMonth;
	private int mHour;
    private int mMinute;
	private String tarikhHariIni;
	private String locationPrefs, currentTime;
	private Date currTime;
	private Map<String,String> locs = new HashMap<String, String>();
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		db = new ContentDBAdapter(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(this
				.getApplicationContext());
		
		getLocationNames();
		initVar();
		getFromDBandPopulate();
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
				.getApplicationContext());
		
		int[] allWidgetIds = intent
				.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

		for (int widgetId : allWidgetIds) {
			// Create some random data
//			int number = (new Random().nextInt(100));

			remoteViews = new RemoteViews(this
					.getApplicationContext().getPackageName(),
					R.layout.widget_layout);
			remoteViews.setTextViewText(R.id.location, loc2);
			remoteViews.setTextViewText(R.id.txtDate, date2);
			remoteViews.setTextViewText(R.id.imsak, imsak2);
			remoteViews.setTextViewText(R.id.subuh, subuh2);
			remoteViews.setTextViewText(R.id.syuruk, syuruk2);
			remoteViews.setTextViewText(R.id.zohor, zohor2);
			remoteViews.setTextViewText(R.id.asar, asar2);
			remoteViews.setTextViewText(R.id.maghrib, maghrib2);
			remoteViews.setTextViewText(R.id.isyak, isyak2);
			try{
				setHighlight();
			}catch (NullPointerException e){
				e.printStackTrace();
			}
			
			// Register an onClickListener
			Intent clickIntent = new Intent(this.getApplicationContext(),
					WidgetProvider.class);
			
			Intent appIntent = new Intent(this.getApplicationContext(),
					WaktuSolatNew.class);

			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
					allWidgetIds);

			pendingIntent = PendingIntent.getBroadcast(
					getApplicationContext(), 0, clickIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			
			PendingIntent appPendingIntent = PendingIntent.getActivity(getApplicationContext()
					, 0, appIntent, 0);

			
			remoteViews.setOnClickPendingIntent(R.id.widget_refresh, pendingIntent);
			remoteViews.setOnClickPendingIntent(R.id.wswidget_layout, appPendingIntent);
			
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
		stopSelf();
		super.onStart(intent, startId);
	}
	private static String pad(int c) {
	    if (c >= 10)
	        return String.valueOf(c);
	    else
	        return "0" + String.valueOf(c);
	}
	public void initVar(){
		final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        currentTime = new StringBuilder().append(pad(mHour)).append(":").append(pad(mMinute)).toString();
//        Log.d("Time", ""+currentTime);
        realMonth = mMonth+1;
        locationPrefs = prefs.getString("locationPref", "sgr03");
        if (realMonth<10){
        	if (mDay<10){
        		tarikhHariIni = new StringBuilder()
        		.append("0").append(mDay).append("-")
                // Month is 0 based so add 1
                .append("0").append(mMonth + 1).append("-")
                .append(mYear).toString();
        	}else {
        		tarikhHariIni = new StringBuilder()
                .append(mDay).append("-")
                // Month is 0 based so add 1
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
	
	public void formatTimeAndPopulate(){
		SimpleDateFormat formattedTime = new SimpleDateFormat("h:mm a");
		formattedTime.setTimeZone(TimeZone.getTimeZone("MYT"));

		SimpleDateFormat timeBefore = new SimpleDateFormat("H:m");
		timeBefore.setTimeZone(TimeZone.getTimeZone("MYT"));

		try {
		currentTime = formattedTime.format(timeBefore.parse(currentTime));
		imsak2 = formattedTime.format(timeBefore.parse(imsak2));
		subuh2 = formattedTime.format(timeBefore.parse(subuh2));
		syuruk2 = formattedTime.format(timeBefore.parse(syuruk2));
		zohor2 = formattedTime.format(timeBefore.parse(zohor2));
		asar2 = formattedTime.format(timeBefore.parse(asar2));
		maghrib2 = formattedTime.format(timeBefore.parse(maghrib2));
		isyak2 = formattedTime.format(timeBefore.parse(isyak2));
		} catch (Exception e) {
		e.printStackTrace();
		}
		
	}
	
public void getLocationNames(){
    	
    	String[] locNames = getResources().getStringArray(R.array.location);
    	String[] locIds = getResources().getStringArray(R.array.location_values);
    	
    	for (int i = 0; i <  locIds.length; i++) {
    		locs.put(locIds[i], locNames[i]);
    		}
    }

public void setHighlight(){
	SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
	timeFormat.setTimeZone(TimeZone.getTimeZone("MYT"));
//	Calendar cal1 = Calendar.getInstance();
//	String timeNow = timeFormat.format(cal1.getTime());
	
	
	Date imsakTime = null, subuhTime = null, syurukTime = null, zohorTime = null, 
			asarTime = null, maghribTime = null, isyakTime = null;
//			, pukul12=null, pukul1 = null;
	try {
		currTime = (Date) timeFormat.parse(currentTime);
		
//		pukul12 = (Date) timeFormat.parse("11:59 PM");
//		pukul1 = (Date) timeFormat.parse("12:00 AM");
		imsakTime = (Date) timeFormat.parse(imsak2);
		subuhTime = (Date) timeFormat.parse(subuh2);
		syurukTime = (Date) timeFormat.parse(syuruk2);
		zohorTime = (Date) timeFormat.parse(zohor2);
		asarTime = (Date) timeFormat.parse(asar2);
		maghribTime = (Date) timeFormat.parse(maghrib2);
		isyakTime = (Date) timeFormat.parse(isyak2);
	
	
	if(currTime.equals(imsakTime) || (currTime.after(imsakTime) && currTime.before(subuhTime))){
			remoteViews.setTextColor(R.id.imsak,Color.GRAY);
//			if (currTime.equals(imsakTime)){
//				notifyWaktu("imsak");
//			}
		}
		else{
			remoteViews.setTextColor(R.id.imsak,Color.BLACK);
		}
		if(currTime.equals(subuhTime) || (currTime.after(subuhTime) && currTime.before(syurukTime))){
			remoteViews.setTextColor(R.id.subuh,Color.GRAY);
//			if (currTime.equals(subuhTime)){
//				notifyWaktu("subuh");
//			}
		}
		else{
			remoteViews.setTextColor(R.id.subuh,Color.BLACK);
		}
		if(currTime.equals(syurukTime) || (currTime.after(syurukTime) && currTime.before(zohorTime))){
			remoteViews.setTextColor(R.id.syuruk,Color.GRAY);
//			if (currTime.equals(syurukTime)){
//				notifyWaktu("syuruk");
//			}
		}
		else{
			remoteViews.setTextColor(R.id.syuruk,Color.BLACK);
		}
		if(currTime.equals(zohorTime) || (currTime.after(zohorTime) && currTime.before(asarTime))){
			remoteViews.setTextColor(R.id.zohor,Color.GRAY);
//			if (currTime.equals(zohorTime)){
//				notifyWaktu("zohor");
//			}
		}
		else{
			remoteViews.setTextColor(R.id.zohor,Color.BLACK);
		}
		if(currTime.equals(asarTime) || (currTime.after(asarTime) && currTime.before(maghribTime))){
			remoteViews.setTextColor(R.id.isyak,Color.GRAY);
//			if (currTime.equals(asarTime)){
//				notifyWaktu("asar");
//			}
		}
		else{
			remoteViews.setTextColor(R.id.isyak,Color.BLACK);
		}
		if(currTime.equals(maghribTime) || (currTime.after(maghribTime) && currTime.before(isyakTime))){
			remoteViews.setTextColor(R.id.maghrib,Color.GRAY);
//			if (currTime.equals(maghribTime)){
//				notifyWaktu("maghrib");
//			}
		}
		else{
			remoteViews.setTextColor(R.id.maghrib,Color.BLACK);
		}
		if(currTime.equals(isyakTime) || currTime.after(isyakTime) || currTime.before(imsakTime)){
			remoteViews.setTextColor(R.id.isyak,Color.GRAY);
//			if (currTime.equals(isyakTime)){
//				notifyWaktu("isyak");
//			}
		}
		else{
			remoteViews.setTextColor(R.id.isyak,Color.BLACK);
		}
		
	} catch (ParseException e) {
		e.printStackTrace();
	}
	
}


	
	public void getFromDBandPopulate(){
		db.open();
		Cursor c = db.fetchOneContent(tarikhHariIni, locationPrefs);
		try{
			
	        int tarikh = c.getColumnIndex("tarikh");
	        int imsak = c.getColumnIndex("imsak");
	        int subuh = c.getColumnIndex("subuh");
	        int syuruk = c.getColumnIndex("syuruk");
	        int zohor = c.getColumnIndex("zohor");
	        int asar = c.getColumnIndex("asar");
	        int maghrib = c.getColumnIndex("maghrib");
	        int isyak = c.getColumnIndex("isyak");
	    	c.moveToFirst();
	    	
	    	if (c.getCount() == 1){
	    		date2 = c.getString(tarikh);
	    		loc2 = locs.get(locationPrefs);
	    		imsak2 = c.getString(imsak);
	    		subuh2 = c.getString(subuh);
	    		syuruk2 = c.getString(syuruk);
	    		zohor2 = c.getString(zohor);
	    		asar2 = c.getString(asar);
	    		maghrib2 = c.getString(maghrib);
	    		isyak2 = c.getString(isyak);
//	    		Log.e("Count", ""+c.getCount());
	    	}else if(c.getCount() == 0){
	    		
	    		Log.e("Count", ""+c.getCount());
	    	}else {
	    		date2 = c.getString(tarikh);
	    		loc2 = locs.get(locationPrefs);
	    		imsak2 = c.getString(imsak);
	    		subuh2 = c.getString(subuh);
	    		syuruk2 = c.getString(syuruk);
	    		zohor2 = c.getString(zohor);
	    		asar2 = c.getString(asar);
	    		maghrib2 = c.getString(maghrib);
	    		isyak2 = c.getString(isyak);
	    		Log.e("Error", "More than one table returned : "+c.getCount());
	    	}
	       	    
	    	formatTimeAndPopulate();
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
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	


}
