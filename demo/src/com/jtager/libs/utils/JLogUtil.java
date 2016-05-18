package com.jtager.libs.utils;

import java.util.Locale;

import android.util.Log;

/**
 * 
 * @author Helping 2016年5月5日14:06:21
 *
 */
public class JLogUtil{
	public static boolean debug = true ;
	
	public final static void i(String tag, String msg){
		if(debug){
			Log.i(tag, msg);
		}
	}

	public final static void i(String tag, String msg, Throwable tr){
		if(debug){
			Log.i(tag, msg, tr);
		}
	}
	
	public final static void e(String tag, String msg){
		if(debug){
			Log.e(tag, msg);
		}
	}

	public final static void e(String tag, String msg, Throwable tr){
		if(debug){
			Log.e(tag, msg, tr);
		}
	}
	
	public final static void w(String tag, String msg){
		if(debug){
			Log.w(tag, msg);
		}
	}

	public final static void w(String tag, String msg, Throwable tr){
		if(debug){
			Log.w(tag, msg, tr);
		}
	}
	
	public final static void d(String tag, String msg){
		if(debug){
			Log.d(tag, msg);
		}
	}

	public final static void d(String tag, String msg, Throwable tr){
		if(debug){
			Log.d(tag, msg, tr);
		}
	}
	
	public final static void v(String tag, String msg){
		if(debug){
			Log.v(tag, msg);
		}
	}

	public final static void v(String tag, String msg, Throwable tr){
		if(debug){
			Log.v(tag, msg, tr);
		}
	}
	
	public final static void println(){
		if(debug){
			System.out.println();
		}
	}
	
	public final static void println(Object object){
		if(debug){
			System.out.println(object);
		}
	}
	
	public final static void print(Object object){
		if(debug){
			System.out.print(object);
		}
	}

	public final static void printl(String format, Object... args){
		if(debug){
			System.out.printf(format, args);
		}
	}

	public final static void printl(Locale l, String format, Object... args){
		if(debug){
			System.out.printf(l, format, args);
		}
	}
}
