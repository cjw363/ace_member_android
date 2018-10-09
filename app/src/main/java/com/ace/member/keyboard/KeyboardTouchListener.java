package com.ace.member.keyboard;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;


public class KeyboardTouchListener implements View.OnTouchListener {
	private KeyboardUtil mKeyboardUtil;
	private int keyboardType = 1;
	private int scrollTo = -1;

	public KeyboardTouchListener(KeyboardUtil util, int keyboardType, int scrollTo) {
		this.mKeyboardUtil = util;
		this.keyboardType = keyboardType;
		this.scrollTo = scrollTo;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (mKeyboardUtil != null && mKeyboardUtil.getEditText() != null && v.getId() != mKeyboardUtil.getEditText().getId()) {
				mKeyboardUtil.showKeyBoardLayout((EditText) v, keyboardType, scrollTo);
			} else if (mKeyboardUtil != null && mKeyboardUtil.getEditText() == null) {
				mKeyboardUtil.showKeyBoardLayout((EditText) v, keyboardType, scrollTo);
			} else {
				if (mKeyboardUtil != null) {
					mKeyboardUtil.setKeyBoardCursorNew((EditText) v);
				}
			}
		}
		return false;
	}
}
