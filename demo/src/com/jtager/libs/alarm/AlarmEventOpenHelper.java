package com.jtager.libs.alarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmEventOpenHelper extends SQLiteOpenHelper {

	public AlarmEventOpenHelper(Context context) {
		super(context, "sys_event.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table alarm_event (id INTEGER PRIMARY KEY AUTOINCREMENT ,flag , alarmDate , alarmTime ,description ,repeat, action);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
