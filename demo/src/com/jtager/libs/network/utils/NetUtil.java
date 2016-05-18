package com.jtager.libs.network.utils;

import android.content.Context;

import com.jtager.libs.network.framework.NetListener;

public class NetUtil {
	public final static long INVALID_REQ_ID = -1 ;
	
	/**
	 * @param context
	 * @param url
	 * @param listener
	 * @return 返回请求参数。
	 */
	public final static long post(Context context , String url , NetListener listener){
		long reqId = getReqId(); // 标记请求...
		
		return reqId;
	}

	private static long REQ_ID = 1 ;
	private synchronized final static long getReqId(){
		return REQ_ID ++ ;
	}
}
