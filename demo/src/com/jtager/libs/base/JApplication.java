package com.jtager.libs.base;

import com.jtager.libs.alarm.framework.AlarmService;
import com.jtager.libs.utils.ActionConstants;

import android.app.Application;
import android.content.Intent;

public class JApplication extends Application {

	public void onCreate() {
		super.onCreate();
		// 初始化action前缀,确保每个app action 都是唯一的。
		ActionConstants.init(this);
		// 启动闹钟  每分钟发送一次广播.
		Intent service = new Intent(this, AlarmService.class);
		startService(service);
		
	}
}
