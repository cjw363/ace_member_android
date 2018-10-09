package com.ace.member.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

public class GridViewNumber extends GridView {
	public GridViewNumber(Context context) {
		super(context);
	}

	public GridViewNumber(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GridViewNumber(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public GridViewNumber(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}


	private OnActionUpListener onActionUpListener;

	public void setOnActionUpListener(OnActionUpListener onActionUpListener) {
		this.onActionUpListener = onActionUpListener;
	}

	;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//当手指按下的坐标，获取到点击的位置
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		int position = pointToPosition(x, y);

		View child = (View) this.getChildAt(position);
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
				break;
			case MotionEvent.ACTION_UP:
				this.getParent().getParent().requestDisallowInterceptTouchEvent(false);
				if (onActionUpListener != null && child != null && position > -1) {
					onActionUpListener.onActionUp(child, position);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				this.getParent().getParent().requestDisallowInterceptTouchEvent(false);
				break;
		}

		return super.onTouchEvent(ev);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//  AT_MOST参数表示控件可以自由调整大小，最大不超过Integer.MAX_VALUE/4
		int height = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, height);
	}

	/*
	监听用户手指抬起
	* */
	public interface OnActionUpListener {
		/**
		 * 手指抬起
		 *
		 * @param view     当前手底下的球
		 * @param position 位置
		 */
		void onActionUp(View view, int position);
	}
}
