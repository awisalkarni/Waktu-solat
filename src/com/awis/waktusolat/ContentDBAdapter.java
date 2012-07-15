package com.awis.waktusolat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ContentDBAdapter {
	public static final String KEY_ROWID = "_PK";
	private static final String DATABASE_TABLE = "waktusolat2";
	
	private Context context;
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	
	public ContentDBAdapter(Context context){
		this.context = context;
	}
	
	public ContentDBAdapter open() throws SQLException{
		dbHelper = new MySQLiteHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		dbHelper.close();
		database.close();
	}
	
	public void saveToDB(String bulan, String tarikh, String hari, String imsak, 
			String subuh, String syuruk, String zohor, String asar, String maghrib, String isyak, String lokasi){
		
		ContentValues values = new ContentValues();
		values.put("bulan", bulan);
		values.put("tarikh", tarikh);
		values.put("hari", hari);
		values.put("imsak", imsak);
		values.put("subuh", subuh);
		values.put("syuruk", syuruk);
		values.put("zohor", zohor);
		values.put("asar", asar);
		values.put("maghrib", maghrib);
		values.put("isyak", isyak);
		values.put("lokasi", lokasi);
		database.insert(DATABASE_TABLE,null, values);
	}
	
	public void deleteAll() {
	      this.database.delete(DATABASE_TABLE, null, null);
	   }
	
	public Cursor fetchAllContents(int bulan, String lokasi){
		return  database.rawQuery("SELECT * FROM "+DATABASE_TABLE+" WHERE bulan="+bulan+" and lokasi = '"+lokasi+"'", null);	
	}
	
	public Cursor fetchOneContent(String tarikh, String lokasi){
		String query = "SELECT * FROM "+DATABASE_TABLE+" WHERE tarikh='"+tarikh+"' AND lokasi = '"+lokasi+"'";
		Log.d("Query", query);
		return  database.rawQuery(query, null);
		
	}
	
	
	
}
