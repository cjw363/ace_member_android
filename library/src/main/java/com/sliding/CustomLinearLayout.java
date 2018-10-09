package com.sliding;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by CF on 2016/7/4.
 * 自定义具有弹性的LinearLayout组件
 */
public class CustomLinearLayout extends LinearLayout {
	private static final String TAG = "CustomLinearLayout";
	private static final float MOVE_FACTOR = 0.3f;//相当阻力系数
	private static final int ANIM_TIME = 300;//还原时间
	private View contentView;
	private float startY;
	private Rect originalRect = new Rect();
	private boolean isMoved = false;

	public CustomLinearLayout(Context context) {
		super(context);
		init();
	}

	public CustomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		RotateAnimation animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		RotateAnimation reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (getChildCount() > 0) {
			contentView = getChildAt(0);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (contentView == null) return;
		// ScrollView中的唯一子控件的位置信息, 这个位置信息在整个控件的生命周期中保持不变
		originalRect.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
	}

	/**
	 * 在触摸事件中, 处理上拉和下拉的逻辑
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (contentView == null) {
			return super.dispatchTouchEvent(ev);
		}
		// 手指是否移动到了当前ScrollView控件之外
		boolean isTouchOutOfScrollView = ev.getY() >= this.getHeight() || ev.getY() <= 0;
		if (isTouchOutOfScrollView) {
			if (isMoved) // 如果当前contentView已经被移动, 首先把布局移到原位置, 然后消费点这个事件
				boundBack();
			return true;
		}

		int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				// 判断是否可以上拉或下拉
				isMoved = canScroll();
				// 记录按下时的Y值
				startY = ev.getY();
				break;
			case MotionEvent.ACTION_UP:
				boundBack();
				break;
			case MotionEvent.ACTION_MOVE:
				// 在移动的过程中， 既没有滚动到可以上拉的程度， 也没有滚动到可以下拉的程度
				if (!canScroll()) {
					startY = ev.getY();
					break;
				}
				// 计算手指移动的距离
				float nowY = ev.getY();
				int deltaY = (int) (nowY - startY);
				boolean shouldMove = (canScroll() && deltaY > 0) || (canScroll() && deltaY < 0);
				if (shouldMove) {
					// 计算偏移量
					int offset = (int) (deltaY * MOVE_FACTOR);
					// 随着手指的移动而移动布局
					contentView.layout(originalRect.left, originalRect.top + offset, originalRect.right, originalRect.bottom + offset);
					isMoved = true; // 记录移动了布局
				}
				break;
			default:
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private boolean canScroll() {
		View childView;
		if (getChildCount() > 0) {
			childView = this.getChildAt(0);
			if (childView instanceof AbsListView) {
				int top = ((AbsListView) childView).getChildAt(0).getTop();
				int pad = ((AbsListView) childView).getListPaddingTop();
				return ((Math.abs(top - pad)) < 3 && ((AbsListView) childView).getFirstVisiblePosition() == 0);
			} else if (childView instanceof ScrollView) {
				ScrollView scrollView = (ScrollView) childView;
				View view = scrollView.getChildAt(0);
				//判断是否滚动到顶部或滚动到底部
				return (scrollView.getScrollY() == 0 || view.getHeight() <= scrollView.getHeight() + scrollView.getScrollY());
			}
		}
		return canScroll(this);
	}

	public boolean canScroll(CustomLinearLayout view) {
		return false;
	}

	/**
	 * 将内容布局移动到原位置 可以在UP事件中调用, 也可以在其他需要的地方调用, 如手指移动到当前ScrollView外时
	 */
	private void boundBack() {
		if (!isMoved) return; // 如果没有移动布局， 则跳过执行
		// 开启动画
		TranslateAnimation anim = new TranslateAnimation(0, 0, contentView.getTop(), originalRect.top);
		anim.setDuration(ANIM_TIME);
		contentView.startAnimation(anim);
		// 设置回到正常的布局位置
		contentView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);
		isMoved = false;
	}
}
