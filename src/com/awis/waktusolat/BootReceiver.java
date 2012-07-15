package com.awis.waktusolat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class BootReceiver extends BroadcastReceiver { 

	public void onReceive(Context context, Intent intent) {
		// in our case intent will always be BOOT_COMPLETED, so we can just set // the alarm // Note that a BroadcastReceiver is *NOT* a Context. Thus, we can't use // "this" whenever we need to pass a reference to the current context. // Thankfully, Android will supply a valid Context as the first parameter 
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context); 
		int minutes = 1; 
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); 
		Intent i = new Intent(context, NotificationService.class); 
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0); 
		am.cancel(pi); // by my own convention, minutes <= 0 means notifications are disabled 
		if (minutes > 0) { 
			am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
					SystemClock.elapsedRealtime() + minutes*60*1000, minutes*60*1000, pi); 
			} 
		}
	}
