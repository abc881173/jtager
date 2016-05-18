package com.jtager.libs.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DragLayout extends RelativeLayout implements OnGestureListener {

	/**
	 * �Ƿ������ק��Ĭ�ϲ�����
	 */
	private boolean isDrag = false;

	private int mDownX;
	private int mDownY;
	private int moveX;
	private int moveY;
	/**
	 * ������ק��position
	 */
	private int mDragPosition;
	/**
	 * ���յ�λ��position
	 */
	private int mLastPosition;

	/**
	 * �տ�ʼ��ק��item��Ӧ��View
	 */
	private View mStartDragItemView = null;

	/**
	 * ������ק�ľ�������ֱ����һ��ImageView
	 */
	private ImageView mDragImageView;

	/**
	 * ����
	 */
	private Vibrator mVibrator;

	private WindowManager mWindowManager;
	/**
	 * item����Ĳ��ֲ���
	 */
	private WindowManager.LayoutParams mWindowLayoutParams;

	/**
	 * ������ק��item��Ӧ��Bitmap
	 */
	private Bitmap mDragBitmap;

	/**
	 * ���µĵ㵽����item���ϱ�Ե�ľ���
	 */
	private int mPoint2ItemTop;

	/**
	 * ���µĵ㵽����item�����Ե�ľ���
	 */
	private int mPoint2ItemLeft;

	/**
	 * DragGridView������Ļ������ƫ����
	 */
	private int mOffset2Top;

	/**
	 * DragGridView������Ļ��ߵ�ƫ����
	 */
	private int mOffset2Left;

	/**
	 * ״̬���ĸ߶�
	 */
	private int mStatusHeight;

	/**
	 * DragGridView�Զ����¹����ı߽�ֵ
	 */
	// private int mDownScrollBorder;

	/**
	 * DragGridView�Զ����Ϲ����ı߽�ֵ
	 */
	// private int mUpScrollBorder;

	private AbsListView curLv, lastLv;
	private GestureDetector detector;
	private OnDragChangedListener listener;

	public DragLayout(Context context) {
		this(context, null);
	}

	public DragLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DragLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mVibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mStatusHeight = getStatusHeight(context); // ��ȡ״̬���ĸ߶�
		detector = new GestureDetector(getContext(), this);
	}

	private Handler mHandler = new Handler();

	// ���������Ƿ�Ϊ������Runnable
	private Runnable mLongClickRunnable = new Runnable() {

		@Override
		public void run() {
			isDrag = true; // ���ÿ�����ק
			mVibrator.vibrate(50); // ��һ��
			mStartDragItemView.setVisibility(View.INVISIBLE);// ���ظ�item

			// �������ǰ��µĵ���ʾitem����
			createDragImage(mDragBitmap, mDownX, mDownY);
		}
	};

	@Override
	public void onLongPress(MotionEvent ev) {
		// TODO Auto-generated method stub
		mDownX = (int) ev.getX();
		mDownY = (int) ev.getY();
		curLv = null;// ��һ��ѡ����...
		lastLv = null; // ���һ��ѡ����...
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof AbsListView) {
				curLv = (AbsListView) view;
				mDownY = mDownY - curLv.getTop();
			}

			// TODO:���ݰ��µ�X,Y�����ȡ�����item��position
			mDragPosition = pointToPosition(curLv, mDownX, mDownY);

			if (mDragPosition >= 0) {
				break;
			}
		}
		if (curLv == null) {
			return;
		}

		// TODO:����position��ȡ��item����Ӧ��View
		mStartDragItemView = getChildAt(curLv, mDragPosition);
		if (mStartDragItemView == null) {
			return;
		}
		// ʹ��Handler�ӳ�dragResponseMSִ��mLongClickRunnable
		mHandler.postDelayed(mLongClickRunnable, 0);

		// �����⼸�������ҿ��Բο��ҵĲ��������ͼ�������
		mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
		mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();

		mOffset2Top = (int) (ev.getRawY() - mDownY);
		mOffset2Left = (int) (ev.getRawX() - mDownX);

		// //��ȡDragGridView�Զ����Ϲ�����ƫ������С�����ֵ��DragGridView���¹���
		// mDownScrollBorder = getHeight() / 5;
		// //��ȡDragGridView�Զ����¹�����ƫ�������������ֵ��DragGridView���Ϲ���
		// mUpScrollBorder = getHeight() * 4/5;

		// ����mDragItemView��ͼ����
		mStartDragItemView.setDrawingCacheEnabled(true);
		// ��ȡmDragItemView�ڻ����е�Bitmap����
		mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
		// ��һ���ܹؼ����ͷŻ�ͼ���棬��������ظ��ľ���
		mStartDragItemView.destroyDrawingCache();

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		detector.onTouchEvent(ev);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			if (isDrag && mDragImageView != null) {
				moveX = (int) ev.getX();
				moveY = (int) ev.getY();
				// �϶�item
				onDragItem(moveX, moveY - curLv.getTop());
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:

			if (isDrag && mDragImageView != null) {
				onStopDrag();
				isDrag = false;
				return true;
			}
			mHandler.removeCallbacks(mLongClickRunnable);
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private View getChildAt(AbsListView view, int position) {
		if (view == null) {
			return null;
		}
		return view.getChildAt(position - view.getFirstVisiblePosition());
	}

	private int pointToPosition(AbsListView view, int x, int y) {
		if (view == null) {
			return -1;
		}
		return view.pointToPosition(x, y);
	}

	/**
	 * �����϶��ľ���
	 * 
	 * @param bitmap
	 * @param downX
	 *            ���µĵ���Ը��ؼ���X����
	 * @param downY
	 *            ���µĵ���Ը��ؼ���X����
	 */
	private void createDragImage(Bitmap bitmap, int downX, int downY) {
		mWindowLayoutParams = new WindowManager.LayoutParams();
		mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; // ͼƬ֮��������ط�͸��
		mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
		mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
		mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top
				- mStatusHeight;
		mWindowLayoutParams.alpha = 0.55f; // ͸����
		mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

		mDragImageView = new ImageView(getContext());
		mDragImageView.setImageBitmap(bitmap);
		mWindowManager.addView(mDragImageView, mWindowLayoutParams);
	}

	/**
	 * �ӽ��������ƶ��϶�����
	 */
	private void removeDragImage() {
		if (mDragImageView != null) {
			mWindowManager.removeView(mDragImageView);
			mDragImageView = null;
		}
	}

	/**
	 * �϶�item��������ʵ����item�����λ�ø��£�item���໥�����Լ�GridView�����й���
	 * 
	 * @param x
	 * @param y
	 */
	private void onDragItem(int moveX, int moveY) {
		int temp = moveY;
		lastLv = null;
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof AbsListView) {
				lastLv = (AbsListView) view;
				temp = moveY - lastLv.getTop() + curLv.getTop();
			}

			// TODO:���ݰ��µ�X,Y�����ȡ�����item��position
			mLastPosition = pointToPosition(lastLv, moveX, temp);

			if (mLastPosition >= 0) {
				break;
			}
		}

		mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
		mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top
				- mStatusHeight;
		mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); // ���¾����λ��
		// TODO:�޸�.. �������棬���mLastPosition
	}

	/**
	 * ֹͣ��ק���ǽ�֮ǰ���ص�item��ʾ���������������Ƴ�
	 */
	private void onStopDrag() {
		// TODO:..
		View view = getChildAt(curLv, mDragPosition);
		if (view != null) {
			view.setVisibility(View.VISIBLE);
		}
		// TODO:���նһ�..
		if (curLv != null && lastLv != null
				&& mLastPosition != AdapterView.INVALID_POSITION) {
			if (listener != null) {
				listener.onChangedItem(curLv, mDragPosition, lastLv,
						mLastPosition);
			}
		}
		removeDragImage();
	}

	/**
	 * ��ȡ״̬���ĸ߶�
	 * 
	 * @param context
	 * @return
	 */
	private static int getStatusHeight(Context context) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		((Activity) context).getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = context.getResources().getDimensionPixelSize(i5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	public OnDragChangedListener getListener() {
		return listener;
	}

	public void setListener(OnDragChangedListener listener) {
		this.listener = listener;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	public interface OnDragChangedListener {
		public void onChangedItem(AbsListView startView, int startPos,
				AbsListView endView, int endPos);
	}

}
