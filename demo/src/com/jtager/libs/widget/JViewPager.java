package com.jtager.libs.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class JViewPager extends ViewPager implements OnGestureListener{
	
	public JViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		detector = new GestureDetector(context,this);
	}
	
	private OnClickListener listener ;
	private GestureDetector detector ;
	private boolean isMove = false ;
	private boolean isPress = false ;
	private boolean isLock = false ;
	private float xDown , yDown ,xMove , yMove;
	
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isMove = false ;
			isPress = false ;
			isLock = false ;
			xDown = event.getRawX();
			yDown = event.getRawY();
			break;
		case  MotionEvent.ACTION_MOVE:
			isMove = true ;
			xMove = event.getRawX();
			yMove = event.getRawY();
			// 这里的动作判断是ViewPager滑动,ListView不滑动
			if (!isLock && ( Math.abs(yMove - yDown) - 2 ) <  Math.abs(xMove - xDown) ) {
				getParent().requestDisallowInterceptTouchEvent(true);
			} 
			isLock = true;
			break ;
		case MotionEvent.ACTION_UP:
			if(listener != null && !isMove && !isPress){
				listener.onClick(this);
			}
			break ; 
		}
		return super.onTouchEvent(event);
	}

	
	
	public void setOnClickListener(OnClickListener l) {
		listener = l ;
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		isPress = true ;
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		isPress = true ;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
