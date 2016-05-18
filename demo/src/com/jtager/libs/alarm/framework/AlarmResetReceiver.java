package com.jtager.libs.alarm.framework;

import com.jtager.libs.utils.JLogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 
 * @author Helping
 *
 */
public class AlarmResetReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		JLogUtil.i("AlarmResetReceiver", intent.getAction());
		Intent service = new Intent(context,AlarmService.class);
		context.startService(service);
	}

}
