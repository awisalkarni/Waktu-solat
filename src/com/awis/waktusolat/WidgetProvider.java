package com.awis.waktusolat;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class WidgetProvider extends AppWidgetProvider {

	private ImageButton widget_update;
//	private DataProvider provider;
	private SharedPreferences prefs;
	private View layout;
	private Context context;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		this.context = context;
//		provider = new DataProvider(context);
		initUI();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		// Get all ids
				ComponentName thisWidget = new ComponentName(context,
						WidgetProvider.class);
				int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

				// Build the intent to call the service
				Intent intent = new Intent(context.getApplicationContext(),
						UpdateWidgetService.class);
				intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

				// Update the widgets via the service
				context.startService(intent);
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	public void initUI(){
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = layoutInflater.inflate(R.layout.widget_layout, null);
		widget_update = (ImageButton) layout.findViewById(R.id.widget_refresh);
		widget_update.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(context, "Mengemaskini.",Toast.LENGTH_SHORT);
				toast.setView(layout);
				toast.show();
				DownloaderTask task = new DownloaderTask();
				task.execute(new String[] {prefs.getString("locationPref", "sgr03")});
			}
			
		});
	}
	
//	public void saveData(){
////    	writeTotxt();
//    	String toSave = provider.getLocation()+"\n"+
//    	provider.getDate()+"\n"+
//    	provider.getImsak()+"\n"+
//    	provider.getSubuh()+"\n"+
//    	provider.getSyuruk()+"\n"+
//    	provider.getZohor()+"\n"+
//    	provider.getAsar()+"\n"+
//    	provider.getMaghrib()+"\n"+
//    	provider.getIsyak();
//    			
//    	FileOutputStream fOut = null;
//        OutputStreamWriter osw = null;
//        try{
//         fOut = context.openFileOutput("waktusolat.dat",Context.MODE_PRIVATE);      
//		  osw = new OutputStreamWriter(fOut);
//         osw.write(toSave);
//         osw.flush();
//         //Toast.makeText(context, "Settings saved",Toast.LENGTH_SHORT).show();
//         }
//         catch (Exception e) {      
//         e.printStackTrace();
//         //Toast.makeText(context, "Settings not saved",Toast.LENGTH_SHORT).show();
//         }
//         finally {
//            try {
//					osw.close();
//                   fOut.close();
//                   } catch (IOException e) {
//                   e.printStackTrace();
//                   }
//         }
//    }
//	
//	
//	
//	public void sendLoc(String location) throws IOException{
//		String loc2 = location;
//		provider.getLoc(loc2);
//    	provider.convert(provider.controller());
//}
	
	private class DownloaderTask extends AsyncTask<String, Integer, String> {
		
	    @Override 
		protected String doInBackground(String... loc) {
//	    	try {
//				sendLoc(loc[0]);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			return null;
	     }
	    @Override
		protected void onPostExecute(String result) {
	    	
//			saveData();
			Toast toast = Toast.makeText(context.getApplicationContext(), "Kemaskini berjaya",Toast.LENGTH_SHORT);
			toast.show();
			
		}

	}

}
