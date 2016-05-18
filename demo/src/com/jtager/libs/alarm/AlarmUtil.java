package com.jtager.libs.alarm;

import java.util.ArrayList;

import com.jtager.libs.entity.AlarmEvent;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AlarmUtil {

	public static AlarmEvent getRemindEvent(Context context, String alarmTime,
			String alarmDate) {
		SQLiteDatabase db = new AlarmEventOpenHelper(context)
				.getReadableDatabase();
		String sql = "select id, alarmDate , alarmTime ,description ,repeat, action from alarm_event where (repeat = '1' and alarmTime = ? ) or (repeat = '0' and alarmTime = ? and (alarmDate = '' or alarmDate = null or alarmDate = ? ));";
		Cursor cursor = db.rawQuery(sql, new String[] { alarmTime, alarmTime,
				alarmDate });
		AlarmEvent event = null;
		if (cursor.moveToFirst()) {
			event = new AlarmEvent();
			event.setId(cursor.getString(0));
			event.setAlarmDate(cursor.getString(1));
			event.setAlarmTime(cursor.getString(2));
			event.setDescription(cursor.getString(3));
			event.setRepeat(cursor.getString(4));
			event.setAction(cursor.getString(5));
			if(event.getRepeat() == "0"){
				updateRepeat(context, event);
			}
		}
		cursor.close();
		db.close();
		return event;
	}

	public static void clearAllRemindEvent(Context context) {
		SQLiteDatabase db = new AlarmEventOpenHelper(context)
				.getReadableDatabase();
		db.execSQL("delete from alarm_event;");
		db.close();
	}

	public static void addRemindEvents(Context context,
			ArrayList<AlarmEvent> events) {
		if (events == null) {
			return;
		}
		// clearAllRemindEvent(context); // 不作处理...
		for (int i = 0; i < events.size(); i++) {
			addRemindEvent(context, events.get(i));
		}
	}

	public static void addRemindEvent(Context context, AlarmEvent event) {
		SQLiteDatabase db = new AlarmEventOpenHelper(context)
				.getReadableDatabase();
		String sql = "insert into alarm_event (flag, alarmDate , alarmTime ,description ,repeat, action) values (?,?,?,?,?,?);";
		db.execSQL(sql, new String[] { event.getFlag(), event.getAlarmDate(),
				event.getAlarmTime(), event.getDescription(),
				event.getRepeat(), event.getAction() });
		db.close();
	}

	public static void updateRepeat(Context context, AlarmEvent event) {
		SQLiteDatabase db = new AlarmEventOpenHelper(context)
				.getReadableDatabase();
		String sql = "update alarm_event set repeat = '-1' where id = ? and repeat = '0' ;";
		db.execSQL(sql, new String[] {event.getId()});
		db.close();
	}

}