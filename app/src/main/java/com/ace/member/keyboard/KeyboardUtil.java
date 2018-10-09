package com.ace.member.keyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.ace.member.R;
import com.og.utils.FileUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class KeyboardUtil {

	private Context mContext;
	private Activity mActivity;
	private PopKeyboardView mKeyboardView;
	private Keyboard mNumKeyboard;// 数字键盘
	public Keyboard mKeyboard;//提供给keyboardView 进行画
	public boolean mIsShow = false;
	private boolean mIfRandom;//是否打乱键盘

	private InputFinishListener mInputOver;
	private KeyBoardStateChangeListener mKeyBoardStateChangeListener;
	private View mLayoutView;
	private View mKeyBoardLayout;

	// 开始输入的键盘状态设置
	private static int mInputType = 1;// 默认
	public static final int INPUT_TYPE_1_NUM = 1; // 数字，右下角 为空
	private static final int KEYBOARD_1_SHOW = 1;
	private static final int KEYBOARD_2_HIDE = 2;

	private final static float SIZE_KEYBOARD_H = 0.398f;// Keyboard 整体高度
	private final static float SIZE_KEYBOARD_TOP_H = 0.06f;// Keyboard 顶部高度

	private EditText mEditText;
	private Handler mHandler;
	private ScrollView mSvMain;
	private View mRootView;
	private int mScrollTo = 0;
	private View mInflaterView;
	private OnBackSpaceListener mOnBackSpaceListener;
	private SubmitBtnListener mSubmitBtnListener;
	private AppCompatImageView mIvSubmit;

	/**
	 * 最新构造方法，现在都用这个
	 *
	 * @param rootView rootView 需要是LinearLayout,以适应键盘
	 */
	public KeyboardUtil(Context context, LinearLayout rootView, ScrollView scrollView) {
		this.mContext = context;
		this.mActivity = (Activity) mContext;
		initKeyBoardView(rootView);
		initScrollHandler(rootView, scrollView);
		this.mIfRandom = false;
	}

	/**
	 * @param ifRandom 是否打乱键盘
	 */
	public KeyboardUtil(Context context, LinearLayout rootView, ScrollView scrollView, boolean ifRandom) {
		this.mContext = context;
		this.mActivity = (Activity) mContext;
		initKeyBoardView(rootView);
		initScrollHandler(rootView, scrollView);
		this.mIfRandom = ifRandom;
	}

	//	/**
	//	 * 弹框类，用这个
	//	 *
	//	 * @param view 是弹框的inflaterView
	//	 */
	//	public KeyboardUtil(View view, Context ctx, LinearLayout root_View, ScrollView scrollView, boolean ifRandom) {
	//		this(ctx, root_View, scrollView, ifRandom);
	//		this.mInflaterView = view;
	//	}

	//设置监听事件
	public void setInputOverListener(InputFinishListener listener) {
		this.mInputOver = listener;
	}

	public Keyboard getKeyBoardType() {
		return mKeyboard;
	}

	private void initKeyBoardView(LinearLayout rootView) {
		try {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			mKeyBoardLayout = inflater.inflate(R.layout.view_keyboard, null);

			mKeyBoardLayout.setVisibility(View.GONE);
			initLayoutHeight((LinearLayout) mKeyBoardLayout);
			this.mLayoutView = mKeyBoardLayout;
			rootView.addView(mKeyBoardLayout);
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void initLayoutHeight(LinearLayout layoutView) {
		try {
			LinearLayout llKeyboardTop = (LinearLayout) layoutView.findViewById(R.id.ll_keyboard_view_top);
			RelativeLayout rlFinish = (RelativeLayout) layoutView.findViewById(R.id.rl_finish);
			rlFinish.setOnClickListener(new finishListener());

			//设置 mKeyboard 整体高度
			LinearLayout.LayoutParams keyboardMainLayoutParams = (LinearLayout.LayoutParams) layoutView.getLayoutParams();
			if (keyboardMainLayoutParams == null) {
				int height = (int) (mActivity.getResources()
					.getDisplayMetrics().heightPixels * SIZE_KEYBOARD_H);
				layoutView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
			} else {
				keyboardMainLayoutParams.height = (int) (mActivity.getResources()
					.getDisplayMetrics().heightPixels * SIZE_KEYBOARD_H);
			}

			//设置 mKeyboard 顶部高度
			LinearLayout.LayoutParams rlTopLayoutParams = (LinearLayout.LayoutParams) llKeyboardTop.getLayoutParams();
			if (rlTopLayoutParams == null) {
				int height = (int) (mActivity.getResources()
					.getDisplayMetrics().heightPixels * SIZE_KEYBOARD_TOP_H);
				llKeyboardTop.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
			} else {
				rlTopLayoutParams.height = (int) (mActivity.getResources()
					.getDisplayMetrics().heightPixels * SIZE_KEYBOARD_TOP_H);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	private void setMargins(View view, int left, int top, int right, int bottom) {
		try {
			if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
				layoutParams.setMargins(left, top, right, bottom);
			} else if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
				LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
				layoutParams.setMargins(left, top, right, bottom);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	public boolean setKeyBoardCursorNew(EditText edit) {
		this.mEditText = edit;
		boolean flag = false;

		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();// isOpen若返回true，则表示输入法打开
		if (isOpen) {
			if (imm.hideSoftInputFromWindow(edit.getWindowToken(), 0)) flag = true;
		}
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		String methodName = null;
		if (currentVersion >= 16) {
			// 4.2
			methodName = "setShowSoftInputOnFocus";
		} else if (currentVersion >= 14) {
			// 4.0
			methodName = "setSoftInputShownOnFocus";
		}

		if (methodName == null) {
			edit.setInputType(InputType.TYPE_NULL);
		} else {
			Class<EditText> cls = EditText.class;
			Method setShowSoftInputOnFocus;
			try {
				setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(edit, false);
			} catch (NoSuchMethodException e) {
				edit.setInputType(InputType.TYPE_NULL);
				e.printStackTrace();
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	private void hideSystemKeyBoard() {
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) imm.hideSoftInputFromWindow(mKeyBoardLayout.getWindowToken(), 0);
	}

	public void hideAllKeyBoard() {
		hideSystemKeyBoard();
		hideKeyboardLayout();
	}


	public int getInputType() {
		return mInputType;
	}

	public EditText getEditText() {
		return mEditText;
	}

	//初始化滑动handler
	@SuppressLint("HandlerLeak")
	private void initScrollHandler(View rootView, final ScrollView scrollView) {
		this.mSvMain = scrollView;
		this.mRootView = rootView;
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == mEditText.getId()) {
					//System.out.println(mScrollTo);
					if (mSvMain != null) mSvMain.smoothScrollTo(0, mScrollTo);
				}
			}
		};
	}

	//滑动监听
	private void keyBoardScroll(final EditText editText, int scrollTo) {
		this.mScrollTo = scrollTo;
		ViewTreeObserver viewTreeObserver = mRootView.getViewTreeObserver();
		viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Message msg = new Message();
				msg.what = editText.getId();
				mHandler.sendMessageDelayed(msg, 500);
				// // 防止多次促发
				mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		});
	}

	//设置一些不需要使用这个键盘的edittext,解决切换问题
	public void setOtherEditText(EditText... edittexts) {
		for (EditText editText : edittexts) {
			editText.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						//防止没有隐藏键盘的情况出现
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								hideKeyboardLayout();
							}
						}, 300);
						mEditText = (EditText) v;
						hideKeyboardLayout();
					}
					return false;
				}
			});
		}
	}

	private class finishListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			hideKeyboardLayout();
		}
	}


	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
			if (mEditText == null) return;
			Editable editable = mEditText.getText();
			int start = mEditText.getSelectionStart();
			int end = mEditText.getSelectionEnd();
			String temp = editable.subSequence(0, start) + text.toString() + editable.subSequence(start, editable
				.length());
			mEditText.setText(temp);
			Editable etext = mEditText.getText();
			Selection.setSelection(etext, start + 1);
		}

		@Override
		public void onRelease(int primaryCode) {

		}

		@Override
		public void onPress(int primaryCode) {
			if (mInputType == KeyboardUtil.INPUT_TYPE_1_NUM) {
				mKeyboardView.setPreviewEnabled(false);
				return;
			}
			if (primaryCode == Keyboard.KEYCODE_SHIFT || primaryCode == Keyboard.KEYCODE_DELETE || primaryCode == 123123 || primaryCode == 456456 || primaryCode == 789789 || primaryCode == 32) {
				mKeyboardView.setPreviewEnabled(false);
				return;
			}
			mKeyboardView.setPreviewEnabled(true);
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = mEditText.getText();
			int start = mEditText.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 收起
				hideKeyboardLayout();
				if (mInputOver != null) mInputOver.inputHasOver(primaryCode, mEditText);
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (null != mOnBackSpaceListener) {
					mOnBackSpaceListener.onBackSpace(getEditText());
				} else if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else if (primaryCode == 0) {
				// 空白键
			} else {
				editable.insert(start, Character.toString((char) primaryCode));
			}
		}
	};

	public interface OnBackSpaceListener {
		void onBackSpace(EditText editText);
	}

	private void showKeyboard() {
		if (mKeyboardView != null) {
			mKeyboardView.setVisibility(View.GONE);
		}
		initInputType();
		mIsShow = true;
		mKeyboardView.setVisibility(View.VISIBLE);
	}

	private void initKeyBoard(int keyBoardViewID) {
		mActivity = (Activity) mContext;
		if (mInflaterView != null) {
			mKeyboardView = (PopKeyboardView) mInflaterView.findViewById(keyBoardViewID);
		} else {
			mKeyboardView = (PopKeyboardView) mKeyBoardLayout.findViewById(keyBoardViewID);
		}
		mKeyboardView.setKeyboardUtil(this);
		mKeyboardView.setEnabled(true);
		mKeyboardView.setOnKeyboardActionListener(listener);
		mKeyboardView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return event.getAction() == MotionEvent.ACTION_MOVE;
			}
		});
	}

	private Key getCodes(int i) {
		return mKeyboardView.getKeyboard().getKeys().get(i);
	}

	private void initInputType() {
		if (mInputType == INPUT_TYPE_1_NUM) {
			initKeyBoard(R.id.keyboard_view);
			mKeyboardView.setPreviewEnabled(false);
			if (mNumKeyboard == null) {
				mNumKeyboard = new Keyboard(mContext, R.xml.symbols);
				if (mIfRandom) {
					randomKeyboardNumber();
				}
			}
			setMyKeyBoard(mNumKeyboard);
		}
	}

	private void setMyKeyBoard(Keyboard newKeyboard) {
		mKeyboard = newKeyboard;
		mKeyboardView.setKeyboard(newKeyboard);
	}

	//新的隐藏方法
	public void hideKeyboardLayout() {
		if (mIsShow) {
			if (mKeyBoardLayout != null){
				mKeyBoardLayout.setVisibility(View.GONE);
			}
			if (mKeyBoardStateChangeListener != null)
				mKeyBoardStateChangeListener.KeyBoardStateChange(KEYBOARD_2_HIDE, mEditText);
			mIsShow = false;
			hideKeyboard();
			mEditText = null;
		}
	}

	/**
	 * @param editText     目标
	 * @param keyBoardType 类型
	 * @param scrollTo     滑动到某个位置,可以是大于等于0的数，其他数不滑动
	 */
	//新的show方法
	public void showKeyBoardLayout(final EditText editText, int keyBoardType, int scrollTo) {
		editText.setText(editText.getText());//解决scrollView偏移问题
		if (editText.equals(mEditText) && mIsShow && mInputType == keyBoardType) return;
		mInputType = keyBoardType;
		this.mScrollTo = scrollTo;

		if (setKeyBoardCursorNew(editText)) {
			Handler showHandler = new Handler();
			showHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					show(editText);
				}
			}, 400);
		} else {
			//直接显示
			show(editText);
		}
	}

	private void show(EditText editText) {
		this.mEditText = editText;
		if (mKeyBoardLayout != null) {
			mKeyBoardLayout.setVisibility(View.VISIBLE);
		}
		showKeyboard();
		if (mKeyBoardStateChangeListener != null) {
			mKeyBoardStateChangeListener.KeyBoardStateChange(KEYBOARD_1_SHOW, editText);
		}
		//用于滑动
		if (mScrollTo >= 0) {
			keyBoardScroll(editText, mScrollTo);
		}
	}

	private void hideKeyboard() {
		mIsShow = false;
		if (mKeyboardView != null) {
			int visibility = mKeyboardView.getVisibility();
			if (visibility == View.VISIBLE) {
				mKeyboardView.setVisibility(View.INVISIBLE);
			}
		}
		if (mLayoutView != null) {
			mLayoutView.setVisibility(View.GONE);
		}
	}

	/**
	 * 输入监听
	 */
	public interface InputFinishListener {
		public void inputHasOver(int onclickType, EditText editText);
	}

	/**
	 * 监听键盘变化
	 */
	public interface KeyBoardStateChangeListener {
		public void KeyBoardStateChange(int state, EditText editText);
	}

	public void setKeyBoardStateChangeListener(KeyBoardStateChangeListener listener) {
		this.mKeyBoardStateChangeListener = listener;
	}

	public void setOnBackSpaceListener(OnBackSpaceListener onBackSpaceListener) {
		this.mOnBackSpaceListener = onBackSpaceListener;
	}

	private boolean isNumber(String str) {
		String wordstr = "0123456789";
		return wordstr.contains(str);
	}

	private void randomKeyboardNumber() {
		List<Key> keyList = mNumKeyboard.getKeys();
		// 查找出0-9的数字键
		List<Keyboard.Key> newKeyList = new ArrayList<Key>();
		for (int i = 0; i < keyList.size(); i++) {
			if (keyList.get(i).label != null && isNumber(keyList.get(i).label.toString())) {
				newKeyList.add(keyList.get(i));
			}
		}
		// 数组长度
		int count = newKeyList.size();
		// 结果集
		List<KeyModel> resultList = new ArrayList<KeyModel>();
		// 用一个LinkedList作为中介
		LinkedList<KeyModel> temp = new LinkedList<KeyModel>();
		// 初始化temp
		for (int i = 0; i < count; i++) {
			temp.add(new KeyModel(48 + i, i + ""));
		}
		// 取数
		Random rand = new Random();
		for (int i = 0; i < count; i++) {
			int num = rand.nextInt(count - i);
			resultList.add(new KeyModel(temp.get(num).getCode(), temp.get(num).getLabel()));
			temp.remove(num);
		}
		for (int i = 0; i < newKeyList.size(); i++) {
			newKeyList.get(i).label = resultList.get(i).getLabel();
			newKeyList.get(i).codes[0] = resultList.get(i).getCode();
		}

		mKeyboardView.setKeyboard(mNumKeyboard);
	}

	/**
	 * 根据传入控件的坐标和用户的焦点坐标，判断是否隐藏键盘，如果点击的位置在控件内，则不隐藏键盘
	 */
	public void hideKeyboard(MotionEvent event, View view) {
		int[] location = {0, 0};
		mLayoutView.getLocationInWindow(location);
		int left = location[0], top = location[1], right = left + mLayoutView.getWidth(), bottom = top + mLayoutView
			.getHeight();
		// 判断焦点位置坐标是否在键盘内
		if (event.getRawX() < left || event.getRawX() > right || event.getY() < top || event.getRawY() > bottom) {
			int[] location2 = {0, 0};
			view.getLocationInWindow(location2);
			int left2 = location2[0], top2 = location2[1], right2 = left2 + view.getWidth(), bottom2 = top2 + view.getHeight();
			// 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
			if (event.getRawX() < left2 || event.getRawX() > right2 || event.getY() < top2 || event.getRawY() > bottom2) {
				hideAllKeyBoard();
			}
		}
	}

	public void bindSubmitBtnListener(SubmitBtnListener submitBtnListener) {
		mIvSubmit = (AppCompatImageView) mLayoutView.findViewById(R.id.iv_submit);
		mIvSubmit.setVisibility(View.VISIBLE);
		mIvSubmit.setEnabled(submitBtnListener.isBtnEnabled());
		this.mSubmitBtnListener = submitBtnListener;

		mIvSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mSubmitBtnListener != null) {
					mSubmitBtnListener.onClick();
				}
			}
		});
	}

	public void setIvSubmitEnabled(boolean enabled) {
		mIvSubmit.setEnabled(enabled);
	}

	public void setDeleteRepeatable(boolean repeatable){
		if (mNumKeyboard !=null) {
			List<Key> keys = mNumKeyboard.getKeys();
			for (int i = 0; i < keys.size(); i++) {
				Key key = keys.get(i);
				if (key.codes[0] == -5) {
					key.repeatable = repeatable;
				}
			}
		}
	}

}
