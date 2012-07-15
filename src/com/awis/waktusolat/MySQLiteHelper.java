package com.awis.waktusolat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "waktusolat2";
	private static final int DATABASE_VERSION = 1;
	//command here
	private static final String DATABASE_COMMAND = "CREATE TABLE waktusolat2 " +
			"(_PK INTEGER PRIMARY KEY, " +
			"bulan INTEGER, " +
			"tarikh VARCHAR, " +
			"hari VARCHAR, " +
			"imsak VARCHAR, " +
			"subuh VARCHAR, " +
			"syuruk VARCHAR, " +
			"zohor VARCHAR, " +
			"asar VARCHAR, " +
			"maghrib VARCHAR, " +
			"isyak VARCHAR, " +
			"lokasi VARCHAR );";
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_COMMAND);	
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
