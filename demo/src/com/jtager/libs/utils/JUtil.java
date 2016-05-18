package com.jtager.libs.utils;

public class JUtil {
	private static long lastClickTime = 0 ;
	
	public final static boolean isClicked(){
		long curTime = System.currentTimeMillis() ;
		if((curTime - lastClickTime) < 200){
			return false ;
		}
		return true ;
	}
}
