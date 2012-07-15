package com.awis.waktusolat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Waktusolat extends SherlockActivity implements OnClickListener{
	private DataProvider provider = new DataProvider(this);
	private Map<String,String> locs = new HashMap<String, String>();
//	private TextView txtView;
	private TextView location, date, nowDate, imsak, subuh, syuruk, zohor, asar, maghrib, isyak;
//	private Button btQibla;
//	private ProgressDialog pDialog;
	private LinearLayout loc_layout;
	private SharedPreferences prefs ;
	private String sendData = "";
	protected static String realLoc;
	private String loc2;
	private String date2;
	private String imsak2;
	private String subuh2;
	private String syuruk2;
	private String zohor2;
	private String asar2;
	private String maghrib2;
	private String isyak2;
	private String toSave;
	
	private boolean settingChanged;
	public final static int THEME = R.style.Theme_Sherlock;
	private static SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
	private static Calendar cal = Calendar.getInstance();
//	private String newCDate = fmt.format(cal.getTime());
//	private static String dateToSend = fmt.format(cal.getTime());
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(THEME);
        getLocationNames();
//        requestWindowFeature(Window.FEATURE_PROGRESS);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.main);
        setSupportProgressBarIndeterminateVisibility(false);
//        setSupportProgressBarVisibility(false);
        //pDialog = ProgressDialog.show(Waktusolat.this,"","Connecting to server...");
        settingChanged = false;
        initUI();
        try{
        	readData();
            writeSaved();
        }catch (Exception e){
        	
        }
        
        
    }
 
    
public void initUI(){
    	
//    	txtView = (TextView) findViewById(R.id.textView1);
    	location = (TextView) findViewById(R.id.location);
    	date = (TextView) findViewById(R.id.txtDate);
    	nowDate = (TextView) findViewById(R.id.txtNow);
    	imsak = (TextView) findViewById(R.id.imsak);
    	subuh = (TextView) findViewById(R.id.subuh);
    	syuruk = (TextView) findViewById(R.id.syuruk);
    	zohor = (TextView) findViewById(R.id.zohor);
    	asar = (TextView) findViewById(R.id.asar);
    	maghrib = (TextView) findViewById(R.id.maghrib);
    	isyak = (TextView) findViewById(R.id.isyak);
    	
//    	btRefresh = (Button) findViewById(R.id.btRefresh);
//    	btRefresh.setOnClickListener(this);
    	
//    	btRefresh2 = (Button) findViewById(R.id.btRefresh2);
//    	btRefresh2.setOnClickListener(this);
    	
    	loc_layout = (LinearLayout) findViewById(R.id.loc_layout);
    	loc_layout.setOnClickListener(this);
    	
//    	btLoc = (Button) findViewById(R.id.btLoc);
//    	btLoc.setOnClickListener(this);
    	
//    	btQibla = (Button) findViewById(R.id.btQibla);
//    	btQibla.setOnClickListener(this);
    	
//    	SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
//    	
//    	Calendar cal = Calendar.getInstance();
//    	String newCDate = fmt.format(cal.getTime());
    	
    }


    public final static String getTime(){
    	return fmt.format(cal.getTime());
    }
    
    public void getLocationNames(){
    	
    	String[] locNames = getResources().getStringArray(R.array.location);
    	String[] locIds = getResources().getStringArray(R.array.location_values);
    	
    	for (int i = 0; i <  locIds.length; i++) {
    		locs.put(locIds[i], locNames[i]);
    		}
    }
    
    
    
    public void fetcher(){
    	final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Waktusolat.CONNECTIVITY_SERVICE);
    	final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
    	try{
    		if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED) {
//			txtView.setText("");
//			pDialog = ProgressDialog.show(Waktusolat.this, "","Mengemaskini...", true);
			tasker();
			
		} else {
			Toast toast = Toast.makeText(getApplicationContext(), "Kemaskini gagal.\n Tiada sambungan internet.",Toast.LENGTH_SHORT);
				toast.show();
			readData();
			writeSaved();
		}
    }catch (Exception e){
    	
    }
	 
}




public void tasker(){
//	setSupportProgressBarVisibility(true);
     setSupportProgressBarIndeterminateVisibility(true);
	DownloaderTask task = new DownloaderTask();
	task.execute(new String[] {prefs.getString("locationPref", "sgr03")});
	
//	getLocationNames();
	realLoc = locs.get(prefs.getString("locationPref", "sgr03"));
}
  
   
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//    	
//    	menu.add("Qiblat")
//        .setIcon(R.drawable.qibla)
//        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//    	
//    	menu.add("Kemaskini")
//        .setIcon(R.drawable.navigation_refresh)
//        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//    	
//    	
//    	
//    	MenuInflater inflater = getSupportMenuInflater();
//    	inflater.inflate(R.menu.menu, menu);
//    	return true;
//    } 
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//    	settingChanged = false;
//    	if (item.getTitle().equals("Kemaskini")){
//    		fetcher();
//    	}else if (item.getTitle().equals("Qiblat")){
//    		Intent qibla = new Intent(Waktusolat.this, QiblaActivity.class);
//    		startActivity(qibla);
//    		overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
//    	}else {
//    		switch (item.getItemId()){
//    	case R.id.lokasi : 
//    		settingChanged = true;
//    		Intent settingsActivity = new Intent(Waktusolat.this,Preferences.class);
//    		startActivity(settingsActivity);
//    		break;
//    	case R.id.refresh :
//    		fetcher();
//    		break;
//    	}
//    	}
//    	
//    	
//    	return true;
//    }
    
    public void onClick(View v) {
    	if (v==loc_layout){
    		settingChanged = true;
    		Intent settingsActivity = new Intent(Waktusolat.this,Preferences.class);
    		startActivity(settingsActivity);
    		overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    	}
    }
    
    public void sendLoc(String location) throws IOException{
    				String loc2 = location;
    	    		provider.getLoc(loc2);
    	    		sendData = provider.controller();
    	        	provider.convert(sendData);
    }
    
    public void writeTotxt(){
    	location.setText(provider.getLocation());
    	date.setText(provider.getDate());
    	
    	imsak.setText(provider.getImsak());
    	subuh.setText(provider.getSubuh());
    	syuruk.setText(provider.getSyuruk());
    	zohor.setText(provider.getZohor());
    	asar.setText(provider.getAsar());
    	maghrib.setText(provider.getMaghrib()); 
    	isyak.setText(provider.getIsyak());
    	
    }
    
    
     
    
    
    @Override
	protected void onResume() {
    	cal = Calendar.getInstance();
    	nowDate.setText("Tarikh hari ini: " + fmt.format(cal.getTime()));
    	
    	if (settingChanged == true && locs.get(prefs.getString("locationPref", "sgr03")).equals(location.getText()) != true){
    		fetcher();
    	}
    	
    	if (fmt.format(cal.getTime()).equals(date.getText()) != true){
    		fetcher();
    	}
    	
		super.onResume();
	}


	public void setHighlight(){
    	SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
    	Calendar cal1 = Calendar.getInstance();
    	String timeNow = timeFormat.format(cal1.getTime());
    	
    	
    	Date currTime = null, imsakTime = null, subuhTime = null, syurukTime = null, zohorTime = null, 
    			asarTime = null, maghribTime = null, isyakTime = null, pukul12=null, pukul1 = null;
    	try {
    		currTime = (Date) timeFormat.parse(timeNow);
    		pukul12 = (Date) timeFormat.parse("23:59");
    		pukul1 = (Date) timeFormat.parse("00:00");
			imsakTime = (Date) timeFormat.parse(imsak2);
			subuhTime = (Date) timeFormat.parse(subuh2);
			syurukTime = (Date) timeFormat.parse(syuruk2);
			zohorTime = (Date) timeFormat.parse(zohor2);
			asarTime = (Date) timeFormat.parse(asar2);
			maghribTime = (Date) timeFormat.parse(maghrib2);
			isyakTime = (Date) timeFormat.parse(isyak2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	//location.append(currTime.toLocaleString());
    	if (currTime.compareTo(subuhTime)==1 && currTime.compareTo(syurukTime)==-1){
    		subuh.setTextColor(Color.GRAY);
    	}else if (currTime.compareTo(zohorTime)==1 && currTime.compareTo(asarTime)==-1){
    		zohor.setTextColor(Color.GRAY);
    	}else if (currTime.compareTo(asarTime)==1 && currTime.compareTo(maghribTime)==-1){
    		asar.setTextColor(Color.GRAY);
    	}else if (currTime.compareTo(maghribTime)==1 && currTime.compareTo(isyakTime)==-1){
    		maghrib.setTextColor(Color.GRAY);
    	}else if (currTime.compareTo(isyakTime)==1 && currTime.compareTo(pukul12)==-1){
    		isyak.setTextColor(Color.GRAY);
    	}else if (currTime.compareTo(pukul1)==1 && currTime.compareTo(imsakTime)==-1){
    		isyak.setTextColor(Color.GRAY);
    	}else if (currTime.compareTo(imsakTime)==1 && currTime.compareTo(subuhTime)==-1){
    		imsak.setTextColor(Color.GRAY);
    	} 	
    }
    
    
    public void saveData(){
    	writeTotxt();
    	toSave = location.getText()+"\n"+
    	date.getText()+"\n"+
    	imsak.getText()+"\n"+
    	subuh.getText()+"\n"+
    	syuruk.getText()+"\n"+
    	zohor.getText()+"\n"+
    	asar.getText()+"\n"+
    	maghrib.getText()+"\n"+
    	isyak.getText();
    			
    	FileOutputStream fOut = null;
        OutputStreamWriter osw = null;
        try{
         fOut = openFileOutput("waktusolat.dat",MODE_PRIVATE);      
		  osw = new OutputStreamWriter(fOut);
         osw.write(toSave);
         osw.flush();
         //Toast.makeText(context, "Settings saved",Toast.LENGTH_SHORT).show();
         }
         catch (Exception e) {      
         e.printStackTrace();
         //Toast.makeText(context, "Settings not saved",Toast.LENGTH_SHORT).show();
         }
         finally {
            try {
            	osw.close();
            	fOut.close();
            	} catch (IOException e) {
            		e.printStackTrace();
            		}
            }
    }
    
public boolean readData(){
	try {
	    // open the file for reading
	    InputStream instream = openFileInput("waktusolat.dat");
	 
	    // if file the available for reading
	    if (instream != null) {
	    	
	      // prepare the file for reading
	      InputStreamReader inputreader = new InputStreamReader(instream);
	      BufferedReader buffreader = new BufferedReader(inputreader);
	 
	      //String line;
	 
	      // read every line of the file into the line-variable, on line at the time
	      try {
	    	  //txtView.setText("\nsaved data:");
	    	 // line=buffreader.readLine();
			//while (( line = buffreader.readLine()) != null) {
	    	  loc2 = buffreader.readLine();
	    	  date2 = buffreader.readLine();
	    	  imsak2= buffreader.readLine();
		      subuh2= buffreader.readLine();
		      syuruk2= buffreader.readLine();
		      zohor2= buffreader.readLine();
		      asar2= buffreader.readLine();
		      maghrib2= buffreader.readLine();
		      isyak2= buffreader.readLine();
	    	  
		    
				//txtView.append("\n"+line);
			 // }
		} catch (IOException e) {
			e.printStackTrace();
		}
	      
	    }
	 
	    // close the file again
	    try {
			instream.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	  } catch (FileNotFoundException e) {
	    return false;
	  }
	setHighlight();
	return true;
}

public void writeSaved(){
	location.setText(loc2);
	date.setText(date2);
  	imsak.setText(imsak2);
  	subuh.setText(subuh2);
  	syuruk.setText(syuruk2);
  	zohor.setText(zohor2);
  	asar.setText(asar2);
  	maghrib.setText(maghrib2); 
  	isyak.setText(isyak2);
  	
//  	try{
//  		if (date2.equals(newCDate)!=true){
//  	    	fetcher();
//  	    }
//  	}catch (NullPointerException e){
//  		
//  	}
  	
}

private class DownloaderTask extends AsyncTask<String, Integer, String> {
	
    @Override 
	protected String doInBackground(String... loc) {
    	try {
			sendLoc(loc[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return asar2;
     }
    @Override
	protected void onPostExecute(String result) {
    	
		saveData();
		readData();
		writeSaved();
		
//		pDialog.dismiss();
    	Toast toast = Toast.makeText(getApplicationContext(), "Kemaskini berjaya.",Toast.LENGTH_SHORT);
		toast.show();
//		setSupportProgressBarVisibility(false);
		setSupportProgressBarIndeterminateVisibility(false);
		//Service
		//startService(new Intent(Waktusolat.this,NotificationService.class));
		
	}

}


}





