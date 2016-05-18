package com.jtager.libs.utils;

import android.content.Context;

public class ActionConstants {
	
	public static final String ACTION_ALARM_SERVICE = ".ALARM";

	private static String pkgName = "com.hehp.action"; // 默认action 前缀...
	
	public final static void init(Context context){
		pkgName = context.getPackageName() ;
	}
	
	public final static String getAction(String action){
		return pkgName + action ;
	}
}
