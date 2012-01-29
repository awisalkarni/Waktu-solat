package com.awis.waktusolat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class NotificationService extends Service {
	DataProvider provider = new DataProvider();
	String imsakT,subuhT,syurukT,zohorT,asarT,maghribT,isyakT;
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
    @Override
    public void onCreate() {
          super.onCreate();
          imsakT = provider.getImsak();
          subuhT = provider.getSubuh();
          syurukT = provider.getSyuruk();
          zohorT = provider.getZohor();
          asarT = provider.getAsar();
          maghribT = provider.getMaghrib();
          isyakT = provider.getIsyak();
          
          Toast.makeText(this,"Servis notifikasi dimulakan...", Toast.LENGTH_LONG).show();
    }
    
    public void createNotification(){
    	
    }
   
    @Override
    public void onDestroy() {
          super.onDestroy();
          Toast.makeText(this, "Servis notifikasi dimatikan...", Toast.LENGTH_LONG).show();
    }
	
}
