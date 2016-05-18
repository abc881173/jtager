package com.jtager.app.activity;

import com.hehp.app.R;
import com.jtager.libs.alarm.AlarmUtil;
import com.jtager.libs.entity.AlarmEvent;
import com.jtager.libs.map.gaode.activity.GaodeMapActivity;
import com.jtager.libs.utils.JLogUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String action = "com.hehp.action.event" ; 
		AlarmEvent event = new AlarmEvent();
		event.setAction(action);
		event.setAlarmTime("18:00");
		event.setRepeat("0");
		AlarmUtil.addRemindEvent(this, event);
		
		IntentFilter filter = new IntentFilter();
//		filter.addAction(ActionConstants.getAction(ActionConstants.ACTION_ALARM_SERVICE));
		filter.addAction(action);
		registerReceiver(receiver, filter);
	}
	
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	};
	
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.open_map:
			Intent intent = new Intent(this , GaodeMapActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	BroadcastReceiver receiver = new BroadcastReceiver(){
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			JLogUtil.i("BroadcastReceiver", arg1.getAction());
			Toast.makeText(MainActivity.this,"action:" + arg1.getAction(), Toast.LENGTH_LONG).show();
		}
		
	};
}
