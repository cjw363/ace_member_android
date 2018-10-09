package com.ace.member.simple_listener;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.ace.member.utils.AppGlobal;
import com.og.utils.FileUtils;
import com.og.utils.Utils;

import java.util.concurrent.FutureTask;


public class SimpleTextWatcher implements TextWatcher {
	private int mMax;//最大位数(不含小数)
	private FutureTask<Void> mAmountFutureTask;
	private boolean mFlagAmount;
	private boolean mFlagPhone;

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	protected void limitAmount(CharSequence s, EditText editText) {
		try {
			//限制输入金额最多为 limit 位小数
			if (s.toString().contains(".")) {
				if (s.length() - 1 - s.toString().indexOf(".") > 2) {
					s = s.toString().subSequence(0, s.toString().indexOf(".") + 2 + 1);
					editText.setText(s);
					editText.setSelection(s.length());
				}
			} else {
				if (s.length() > mMax) {
					s = s.toString().subSequence(0, mMax);
					editText.setText(s);
					editText.setSelection(s.length());
				}
			}
			//第一位输入小数点的话自动变换为 0.
			if (s.toString().trim().equals(".")) {
				s = "0" + s;
				editText.setText(s);
				editText.setSelection(2);
			}

			//避免重复输入小数点前的0 ,没有意义
			if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
				if (!s.toString().substring(1, 2).equals(".")) {
					editText.setText(s.subSequence(0, 1));
					editText.setSelection(1);
				}
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	protected void setMax(int max) {
		this.mMax = max;
	}

	//当所有EditText都有内容时,返回true
	protected boolean checkTextViewContent(EditText... editTexts) {
		boolean isChecked = true;
		try {
			for (EditText editText : editTexts) {
				isChecked = isChecked & !TextUtils.isEmpty(editText.getText().toString());
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return isChecked;
	}

	protected void formatMoney(final EditText editText, final CharSequence s, final int digits) {
		try {
			if (mAmountFutureTask != null) {
				mAmountFutureTask.cancel(true);
				mFlagAmount = false;
			}
			if (mFlagAmount) return;
			mFlagAmount = true;
			if (s.toString().equals(".")) {
				editText.setText("0.");
				editText.setSelection(2);
			}
			mAmountFutureTask = new FutureTask<>(new Runnable() {
				@Override
				public void run() {
					editText.setText(TextUtils.isEmpty(s) ? "" : Utils.format(s, digits <= 0 ? 0 : digits));
					editText.setSelection(editText.getText().toString().length());
					mFlagAmount = false;
					mAmountFutureTask.cancel(true);
				}
			}, null);
			Utils.runOnUIThread(mAmountFutureTask, 3000);
			editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					Utils.runOnUIThread(mAmountFutureTask, 0);
				}
			});
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	protected void formatMoney(final EditText editText, final CharSequence s, final String currency) {
		try {
			if (mAmountFutureTask != null) {
				mAmountFutureTask.cancel(true);
				mFlagAmount = false;
			}
			if (mFlagAmount) return;
			mFlagAmount = true;
			if (s.toString().equals(".")) {
				editText.setText("0.");
				editText.setSelection(2);
			}
			mAmountFutureTask = new FutureTask<>(new Runnable() {
				@Override
				public void run() {
					editText.setText(TextUtils.isEmpty(s) ? "" : Utils.format(s, currency));
					editText.setSelection(editText.getText().toString().length());
					mFlagAmount = false;
					mAmountFutureTask.cancel(true);
				}
			}, null);
			Utils.runOnUIThread(mAmountFutureTask, 3000);
			editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					Utils.runOnUIThread(mAmountFutureTask, 0);
				}
			});
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	protected void formatPhone(EditText editText, String s) {
		try {
			if (mFlagPhone) return;
			mFlagPhone = true;
			if (TextUtils.isEmpty(s)) s = "";
			else s = s.replaceAll("\\s*", "");
			if (s.startsWith("00")) {
				s = s.replaceFirst("00", "0");
				editText.setText(s);
				editText.setSelection(1);
			}
			mFlagPhone = false;
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
	}

	protected String dealCurrency(String s) {
		return Utils.dealCurrency(s);
	}

	public static class PhoneTextWatcher extends SimpleTextWatcher {
		EditText mEditText;

		public PhoneTextWatcher(EditText editText) {
			mEditText = editText;
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			formatPhone(mEditText, s.toString());
		}
	}

	public static class MoneyTextWatcher extends SimpleTextWatcher {
		EditText mEditText;
		int mDigits;
		String mCurrency;

		public MoneyTextWatcher(EditText editText) {
			mEditText = editText;
			mDigits = 2;
			mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
		}

		public MoneyTextWatcher(EditText editText, int digits) {
			mEditText = editText;
			mDigits = digits;
			if (digits == 0) editText.setInputType(InputType.TYPE_CLASS_NUMBER);
			mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
		}

		public MoneyTextWatcher(EditText editText, String currency) {
			mEditText = editText;
			boolean b = currency.equals(AppGlobal.KHR) || currency.equals(AppGlobal.VND);
			mDigits = b ? 0 : 2;
			mCurrency = currency;
			if (b) editText.setInputType(InputType.TYPE_CLASS_NUMBER);
			mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (TextUtils.isEmpty(mCurrency)) formatMoney(mEditText, s, mDigits);
			else formatMoney(mEditText, s, mCurrency);
		}

		public void setCurrency(EditText editText, String currency) {
			boolean b = currency.equals(AppGlobal.KHR) || currency.equals(AppGlobal.VND);
			mDigits = b ? 0 : 2;
			mCurrency = currency;
			if (b) editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		}
	}
}
