package com.jtager.libs.alarm.framework;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.jtager.libs.alarm.AlarmUtil;
import com.jtager.libs.entity.AlarmEvent;
import com.jtager.libs.utils.ActionConstants;
import com.jtager.libs.utils.JLogUtil;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * 
 * @author Helping 2016年5月5日11:43:27
 * 每隔1分钟发送一个广播..
 * 
 */
@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class AlarmService extends Service {
	private int count = 0 ;
	private Timer timer ;
	public static long period =  60; // 
	public static boolean isLogin = false ;
	SimpleDateFormat fDate ,fTime;
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
		timer = new Timer(true);// 创建守护线程...
		fDate = new SimpleDateFormat("yyyy-MM-dd");
		fTime = new SimpleDateFormat("HH:mm");
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		JLogUtil.i("AlarmService",  "onStartCommand");
		synchronized (AlarmService.this) {
			count ++ ;
			int curMs = (int) ((System.currentTimeMillis() / 1000) % 60) ;
			int start = curMs < 3 ? 0 : 60 - curMs ;
			timer.schedule(new AlarmTask(count), start , period * 1000);// 8秒后触发执行..
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void onDestroy() {
		super.onDestroy();
		// 服务关闭执行重启服务..
		Intent intent = new Intent("com.hehp.libs.RESTART_ALARM_SERVICE");
		sendBroadcast(intent);
	}
	
	class AlarmTask extends TimerTask{
		private int remindId ;
		
		public AlarmTask(int remindId) {
			super();
			this.remindId = remindId;
		}

		public void run() {
			synchronized (AlarmService.this) {
				if(remindId != count){// 屏蔽失效时钟..
					cancel();
					return ;
				}
				Date date = new Date();
				String d = fDate.format(date);
				String time = fTime.format(date);
				JLogUtil.println("当前时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				// 定时每分钟发送一次广播...
				Intent intent = new Intent(ActionConstants.getAction(ActionConstants.ACTION_ALARM_SERVICE)) ;
				sendBroadcast(intent);
				// TODO:发送指定事件广播...
				AlarmEvent event = AlarmUtil.getRemindEvent(getContext(), time, d);
				JLogUtil.println("alarm_event: " + event);
				if(event == null){
					return ;
				}
				intent = new Intent(event.getAction()) ;
				intent.putExtra("alarm_event", event);
				sendBroadcast(intent);
//				handler.sendEmptyMessage(-1);	
			}
		}
	}
	
	public Context getContext(){
		return this ;
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
		};
	};
}
