package com.awis.waktusolat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.StaleDataException;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class NotificationService extends Service {
	
	private String imsak2;
	private String subuh2;
	private String syuruk2;
	private String zohor2;
	private String asar2;
	private String maghrib2;
	private String isyak2;
	private ContentDBAdapter db;
	private SharedPreferences prefs ;
	private int mYear, mMonth, mDay, realMonth;
	private int mHour;
    private int mMinute;
	private String tarikhHariIni;
	private String locationPrefs, currentTime;
	private Date currTime;
	private Map<String,String> locs = new HashMap<String, String>();
	private NotificationManager mNotificationManager;
	private Timer timer;
	private final IBinder mBinder = new LocalBinder();
	private boolean notifyPrefs;
	
	@Override
    public void onCreate() {
          super.onCreate();
          db = new ContentDBAdapter(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(this
				.getApplicationContext());
		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		getLocationNames();
		initVar();
		getFromDBandPopulate();
//		timer = new Timer();
//        timer.schedule(notifyTasker,0, 70000);
//        Toast.makeText(this,"Servis notifikasi dimulakan...", Toast.LENGTH_LONG).show();
    }
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        notifyTasker.run();
        return START_STICKY;
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
        notifyPrefs = prefs.getBoolean("notifyPref", true);
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

	private TimerTask notifyTasker = new TimerTask() {
		public void run() {
			initVar();
			SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
			timeFormat.setTimeZone(TimeZone.getTimeZone("MYT"));
	
	Date imsakTime = null, subuhTime = null, syurukTime = null, zohorTime = null, 
			asarTime = null, maghribTime = null, isyakTime = null;

	try {
		
		currTime = (Date) timeFormat.parse(currentTime);

		imsakTime = (Date) timeFormat.parse(imsak2);
		subuhTime = (Date) timeFormat.parse(subuh2);
		syurukTime = (Date) timeFormat.parse(syuruk2);
		zohorTime = (Date) timeFormat.parse(zohor2);
		asarTime = (Date) timeFormat.parse(asar2);
		maghribTime = (Date) timeFormat.parse(maghrib2);
		isyakTime = (Date) timeFormat.parse(isyak2);
	
		if (notifyPrefs){
			if (currTime.equals(imsakTime)){
				notifyWaktu("imsak");
				
		}if (currTime.equals(subuhTime)){
				notifyWaktu("subuh");
				
		}if (currTime.equals(syurukTime)){
				notifyWaktu("syuruk");
				
		}if (currTime.equals(zohorTime)){
				notifyWaktu("zohor");
				
		}if (currTime.equals(asarTime)){
				notifyWaktu("asar");
				
		}if (currTime.equals(maghribTime)){
				notifyWaktu("maghrib");
				
		}if (currTime.equals(isyakTime)){
				notifyWaktu("isyak");
				
		}
	}
		

		
	} catch (ParseException e) {
		e.printStackTrace();
	}
	
}
};

@SuppressWarnings("deprecation")
	public void notifyWaktu(String waktu){
	     // In this sample, we'll use the same text for the ticker and the expanded notification
	        CharSequence text = "Telah masuk waktu " + waktu+" bagi "+locs.get(locationPrefs);;

	        // Set the icon, scrolling text and timestamp
	        Notification notification = new Notification(R.drawable.icon2, text, System.currentTimeMillis());
	        
	        notification.defaults = Notification.DEFAULT_ALL;
//	        notification.tickerText = text;

	        // The PendingIntent to launch our activity if the user selects this notification
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, WaktuSolatNew.class), 0);

	        // Set the info for the views that show in the notification panel.
	        notification.setLatestEventInfo(this, "Waktu Solat",text, contentIntent);
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;
	       
	        // Send the notification.
	        mNotificationManager.notify(0, notification);
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
	    		c.getString(tarikh);
	    		locs.get(locationPrefs);
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
	    		c.getString(tarikh);
	    		locs.get(locationPrefs);
	    		imsak2 = c.getString(imsak);
	    		subuh2 = c.getString(subuh);
	    		syuruk2 = c.getString(syuruk);
	    		zohor2 = c.getString(zohor);
	    		asar2 = c.getString(asar);
	    		maghrib2 = c.getString(maghrib);
	    		isyak2 = c.getString(isyak);
	    		Log.e("Error", "More than one table returned : "+c.getCount());
	    	}
	       	    
//	    	formatTimeAndPopulate();
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
		notifyTasker.run();
		return mBinder;
	}
	
	public class LocalBinder extends Binder {
		NotificationService getService() {
	            return NotificationService.this;
	        }
	    }

}
