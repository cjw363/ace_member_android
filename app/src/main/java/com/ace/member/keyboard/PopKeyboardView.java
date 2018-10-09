package com.ace.member.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;

import com.ace.member.R;
import com.og.LibApplication;

import java.util.List;

public class PopKeyboardView extends KeyboardView {
	private int rightType = 1;// 右下角
	private KeyboardUtil mKeyboardUtil;

	public PopKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PopKeyboardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setKeyboardUtil(KeyboardUtil keyboardUtil) {
		this.mKeyboardUtil = keyboardUtil;
	}

	/**
	 * 重新画一些按键
	 */
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Keyboard keyBoard = mKeyboardUtil.getKeyBoardType();
		List<Key> keys = keyBoard.getKeys();

		for (Key key : keys) {
			// 数字键盘的处理
			initRightType(key);
			drawNumSpecialKey(key, canvas);
		}
	}

	//数字键盘
	private void drawNumSpecialKey(Key key, Canvas canvas) {
		// 右下角的按键
		if (key.codes[0] == -5) {
			drawKeyBackground(R.drawable.ic_backspace, canvas, key);
		}

		// 左下角的按键
		//if (key.codes[0] == 0) {
		//drawKeyBackground(R.drawable.keyboard_general_nom, canvas, key);
		//}
	}

	private void drawKeyBackground(int drawableId, Canvas canvas, Key key) {
		try {
			//			Drawable npd = Utils.getDrawable(drawableId); // 华为兼容有问题
			VectorDrawableCompat v = VectorDrawableCompat.create(LibApplication.getContext()
				.getResources(), drawableId, null);
			int[] drawableState = key.getCurrentDrawableState();
			if (key.codes[0] != 0) {
				v.setState(drawableState);
			}
			if (key.codes[0] == -5) {
				v.setBounds(key.x + key.width * 4 / 10, key.y + key.height * 2 / 7, key.x + key.width * 6 / 10, key.y + key.height * 5 / 7);
			} else {
				v.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
			}
			v.draw(canvas);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initRightType(Key key) {
		if (key.codes[0] == 0) {
			rightType = 1;//0
		} else if (key.codes[0] == 46) {
			rightType = 3; //点
		}
	}

	private void drawText(Canvas canvas, Key key) {
		Rect bounds = new Rect();
		Paint paint = new Paint();
		paint.setTextAlign(Paint.Align.CENTER);
		if (key.codes[0] == 46) {
			paint.setTextSize(70);
		} else {
			paint.setTextSize(40);
		}
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		if (key.label != null) {
			//数字
			paint.getTextBounds(key.label.toString(), 0, key.label.toString().length(), bounds);
			canvas.drawText(key.label.toString(), key.x + (key.width / 2), (key.y + key.height / 2) + bounds
				.height() / 2, paint);
		} else if (key.codes[0] == -5) {
			//Back Space
			key.icon.setBounds(key.x + (int) (0.4 * key.width), key.y + (int) (0.328 * key.height), key.x + (int) (0.6 * key.width), key.y + (int) (0.672 * key.height));
			key.icon.draw(canvas);
		}
	}
}
