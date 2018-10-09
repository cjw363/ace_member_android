package com.ace.member.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ace.member.R;
import com.ace.member.keyboard.KeyboardUtil;
import com.og.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class VerificationCodeView extends ViewGroup {
	private final static String TYPE_NUMBER = "number";
	private final static String TYPE_TEXT = "text";
	private final static String TYPE_PASSWORD = "password";
	private final static String TYPE_PHONE = "phone";

	private static final String TAG = "VerificationCodeView";
	private boolean showCursor = true;
	private int box = 4;
	private int boxWidth = Utils.getDimenPx(R.dimen.width120);
	private int boxHeight = Utils.getDimenPx(R.dimen.height120);
	private int childHPadding = Utils.getDimenPx(R.dimen.padding15);
	private int childVPadding = Utils.getDimenPx(R.dimen.padding15);
	private int textSize = Utils.getDimenPx(R.dimen.txtSize30);
	private String inputType = TYPE_PASSWORD;
	private Drawable boxBgFocus = null;
	private Drawable boxBgNormal = null;
	private Listener listener;
	private List<EditText> mEditTextList = new ArrayList<>();
	private int currentPosition = 0;
	private SparseArray<EditText> map = new SparseArray<>();
	private KeyboardUtil mKeyboardUtil;

	public VerificationCodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeView);
		box = a.getInt(R.styleable.VerificationCodeView_box, 4);

		childHPadding = (int) a.getDimension(R.styleable.VerificationCodeView_child_h_padding, 0);
		childVPadding = (int) a.getDimension(R.styleable.VerificationCodeView_child_v_padding, 0);
		boxBgFocus = a.getDrawable(R.styleable.VerificationCodeView_box_bg_focus);
		boxBgNormal = a.getDrawable(R.styleable.VerificationCodeView_box_bg_normal);
		inputType = a.getString(R.styleable.VerificationCodeView_inputType);
		boxWidth = (int) a.getDimension(R.styleable.VerificationCodeView_child_width, boxWidth);
		boxHeight = (int) a.getDimension(R.styleable.VerificationCodeView_child_height, boxHeight);
		showCursor = a.getBoolean(R.styleable.VerificationCodeView_showCursor, showCursor);
		textSize = (int) a.getDimension(R.styleable.VerificationCodeView_TextSize, Utils.getDimenPx(R.dimen.txtSize30));
		initViews();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	private void initViews() {

		OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					v.setBackground(boxBgFocus);
					attachKeyboard((EditText) v);
				} else {
					v.setBackground(boxBgNormal);
				}
			}
		};

		for (int i = 0; i < box; i++) {
			final EditText editText = new EditText(getContext());
			map.put(i, editText);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(boxWidth, boxHeight);
			layoutParams.bottomMargin = childVPadding;
			layoutParams.topMargin = childVPadding;
			layoutParams.leftMargin = childHPadding;
			layoutParams.rightMargin = childHPadding;
			layoutParams.gravity = Gravity.CENTER;

			editText.setOnFocusChangeListener(onFocusChangeListener);
			editText.setBackground(boxBgNormal);
			editText.setTextColor(Color.BLACK);
			editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			editText.setLayoutParams(layoutParams);
			editText.setGravity(Gravity.CENTER);
			editText.setCursorVisible(showCursor);
			editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
			if (TYPE_NUMBER.equals(inputType)) {
				editText.setInputType(InputType.TYPE_CLASS_NUMBER);
				editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
			} else if (TYPE_PASSWORD.equals(inputType)) {
				editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
			} else if (TYPE_TEXT.equals(inputType)) {
				editText.setInputType(InputType.TYPE_CLASS_TEXT);
			} else if (TYPE_PHONE.equals(inputType)) {
				editText.setInputType(InputType.TYPE_CLASS_PHONE);
			}
			editText.setId(i);
			editText.setEms(1);
			editText.setTag(i);
			if (i != 0) editText.setEnabled(false);
			editText.addTextChangedListener(new MyTextWatcher(editText));
			addView(editText, i);
			mEditTextList.add(editText);
		}
	}

	//回到上一个录入框并删除框内数据
	private void backFocus(EditText editText) {
		int i = (int) editText.getTag();
		if (editText.getText().toString().isEmpty() && i > 0) {
			EditText lastEditText = (EditText) getChildAt(i - 1);
			lastEditText.setText("");
			focus();
		} else {
			editText.setText("");
			focus();
		}
	}

	private void focus() {
		int count = getChildCount();
		EditText editText;
		for (int i = 0; i < count; i++) {
			editText = (EditText) getChildAt(i);
			if (editText.getText().length() < 1) {
				editText.requestFocus();
				return;
			}
		}
	}

	private void checkAndCommit() {
		StringBuilder stringBuilder = new StringBuilder();
		boolean full = true;
		for (int i = 0; i < box; i++) {
			EditText editText = (EditText) getChildAt(i);
			String content = editText.getText().toString();
			if (content.length() == 0) {
				full = false;
				break;
			} else {
				stringBuilder.append(content);
			}
		}

		if (full) {
			if (listener != null) {
				mKeyboardUtil.hideAllKeyBoard();
				listener.onComplete(stringBuilder.toString());
			}
		}

	}

	public EditText getCurEditText() {
		return mEditTextList.get(currentPosition);
	}

	@Override
	public void setEnabled(boolean enabled) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			child.setEnabled(enabled);
		}
	}

	public void setOnCompleteListener(Listener listener) {
		this.listener = listener;
	}

	@Override

	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LinearLayout.LayoutParams(getContext(), attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int count = getChildCount();

		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
		}
		if (count > 0) {
			View child = getChildAt(0);
			int cHeight = child.getMeasuredHeight();
			int cWidth = child.getMeasuredWidth();
			int maxH = cHeight + 2 * childVPadding;
			int maxW = (cWidth + childHPadding) * box - childHPadding;
			setMeasuredDimension(resolveSize(maxW, widthMeasureSpec), resolveSize(maxH, heightMeasureSpec));
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);

			child.setVisibility(View.VISIBLE);
			int cWidth = child.getMeasuredWidth();
			int cHeight = child.getMeasuredHeight();
			int cl = (i) * (cWidth + childHPadding);
			int ct = childVPadding;
			int cr = cl + cWidth;
			int cb = ct + cHeight;
			child.layout(cl, ct, cr, cb);
		}
	}

	public interface Listener {
		void onComplete(String content);
	}

	public void clear(boolean showKeyboard) {
		for (int i = 0; i < box; i++) {
			EditText editText = (EditText) getChildAt(i);
			editText.setText("");
			editText.clearFocus();
			editText.setEnabled(false);
		}
		if (showKeyboard) {
			focus();
		} else {
			if (mKeyboardUtil != null) {
				mKeyboardUtil.hideAllKeyBoard();
			}
		}
	}

	private class MyTextWatcher implements TextWatcher {
		private EditText mEditText;

		MyTextWatcher(EditText editText) {
			mEditText = editText;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			try {
				if (start == 0 && count >= 1 && currentPosition != mEditTextList.size() - 1) {
					currentPosition++;
					getCurEditText().requestFocus();
					getCurEditText().setEnabled(true);
					mEditTextList.get(currentPosition - 1).setEnabled(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			focus();
			checkAndCommit();
		}

		@Override
		public void afterTextChanged(Editable s) {}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int count = getChildCount();
		EditText editText;
		for (int i = 0; i < count; i++) {
			editText = (EditText) getChildAt(i);
			if (editText.getText().length() < 1) {
				attachKeyboard(editText);
				editText.requestFocus();
				return true;
			} else if (i == count - 1) {
				attachKeyboard(editText);
				editText.requestFocus();
			}
		}
		return true;
	}

	public void setKeyboardUtil(KeyboardUtil keyboardUtil) {
		mKeyboardUtil = keyboardUtil;
		mKeyboardUtil.setOnBackSpaceListener(new KeyboardUtil.OnBackSpaceListener() {
			@Override
			public void onBackSpace(EditText editText) {
				backFocus(editText);
			}
		});
	}

	private void attachKeyboard(EditText editText) {
		//绑定数字键盘
		if (mKeyboardUtil != null) {
			if (mKeyboardUtil.getEditText() != null && editText.getId() != mKeyboardUtil.getEditText().getId()) {
				mKeyboardUtil.showKeyBoardLayout(editText, KeyboardUtil.INPUT_TYPE_1_NUM, -1);
			} else if (mKeyboardUtil.getEditText() == null) {
				mKeyboardUtil.showKeyBoardLayout(editText, KeyboardUtil.INPUT_TYPE_1_NUM, -1);
			} else {
				mKeyboardUtil.setKeyBoardCursorNew(editText);
			}
			mKeyboardUtil.setDeleteRepeatable(false);
		}
	}

}
