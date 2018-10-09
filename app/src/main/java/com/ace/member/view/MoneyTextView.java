package com.ace.member.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.utils.AppUtils;
import com.og.utils.Utils;

@SuppressLint("AppCompatCustomView")
public class MoneyTextView extends TextView {

	private String mCurrency;
	private boolean mIsShowCurrency;
	private boolean mIsSimplyAmount;
	private boolean mIsCurrencyAtEnd;
	private boolean mIsAbsAmount;
	private boolean mIsNoDecimal;

	public MoneyTextView(Context context) {
		super(context);
		initView(context, null);
	}

	public MoneyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}

	private void initView(Context context, AttributeSet attrs) {
		// Setting Default Parameters
		mCurrency = "";
		mIsShowCurrency = false;//默认不展现货币
		mIsSimplyAmount = true;//默认(VND,KHR 不保留小数)
		mIsCurrencyAtEnd = true;//默认货币符号展现在货币之后
		mIsAbsAmount = false;//默认不取绝对值
		mIsNoDecimal = false;//默认有小数位

		// Check for the attributes
		if (attrs != null) {
			// Attribute initialization
			final TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.MoneyTextView, 0, 0);
			try {
				mCurrency = attrArray.getString(R.styleable.MoneyTextView_currency);
				mIsShowCurrency = attrArray.getBoolean(R.styleable.MoneyTextView_show_currency, false);
				mIsSimplyAmount = attrArray.getBoolean(R.styleable.MoneyTextView_simply_amount, true);
				mIsCurrencyAtEnd = attrArray.getBoolean(R.styleable.MoneyTextView_currency_at_end, true);
				mIsAbsAmount = attrArray.getBoolean(R.styleable.MoneyTextView_abs_amount, false);
				mIsNoDecimal = attrArray.getBoolean(R.styleable.MoneyTextView_no_decimal, false);
			} finally {
				attrArray.recycle();
			}
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		setValue(getText().toString());
	}

	private String formatValue(String number) {
		String decoStr;
		if (mIsAbsAmount){
			number = Utils.absAmount(number);
		}

		if (mIsSimplyAmount) {
			decoStr = AppUtils.simplifyAmount(mCurrency, String.valueOf(number));
		} else {
			decoStr = Utils.format(number, 2);
		}

		if (mIsNoDecimal){
			decoStr = Utils.format(number, 0);
		}

		if (mIsShowCurrency && !TextUtils.isEmpty(mCurrency)) {
			if (mIsCurrencyAtEnd) {
				decoStr = decoStr + " " + mCurrency;
			} else {
				decoStr = mCurrency + " " + decoStr;
			}
		}

		return decoStr;
	}

	private void setValue(String valueStr) {
		try {
			String originalString;

			String stringVal;

			originalString = getValueString();
			stringVal = Utils.format(originalString, 2);
			String formattedString = formatValue(stringVal);

			//setting text after format to EditText
			setText(formattedString);

		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			setText(valueStr);
		}
	}

	/**
	 * Get the value of the text without any commas and currency.
	 * For example, if the edit text value is USD 1,34,000.60 then this method will return 134000.60
	 *
	 * @return A string of the raw value in the text field
	 */
	public String getValueString() {

		String string = getText().toString();

		if (string.contains(",")) {
			string = string.replace(",", "");
		}
		if (string.contains(" ") && !mIsCurrencyAtEnd) {
			string = string.substring(string.indexOf(" ") + 1, string.length());
		} else if (string.contains(" ") && mIsCurrencyAtEnd) {
			string = string.substring(0, string.indexOf(" "));
		}
		return string;
	}

	/**
	 * Get the value of the text with formatted commas and currency.
	 * For example, if the edit text value is USD 1,34,000.60 then this method will return exactly USD 1,34,000.60
	 *
	 * @return A string of the text value in the text field
	 */
	public String getFormattedString() {
		return getText().toString();
	}

	/**
	 * Set the currency for the edit text. (Default is USD).
	 *
	 * @param currency the new currency in string
	 */
	public void setCurrency(String currency) {
		mCurrency = currency;
		setValue(getText().toString());
	}

	/**
	 * Shows the currency in the text. (Default is not shown).
	 */
	public void showCurrency() {
		mIsShowCurrency = true;
		setValue(getText().toString());
	}

	/**
	 * 设置 Currency 位置
	 * true  : amount之后(默认)
	 * false : amount之前
	 */
	public void setCurrencyAtEnd(boolean isCurrencyAtEnd) {
		mIsCurrencyAtEnd = isCurrencyAtEnd;
		setValue(getText().toString());
	}

	/**
	 * Simplify the money in the text. (Default is simplified).
	 * "KHR" & "VND" have no decimal
	 */
	public void setSimplyMoney(boolean isSimplyAmount) {
		mIsSimplyAmount = isSimplyAmount;
		setValue(getText().toString());
	}

	public void setMoney(String currency, String amount) {
		setText(amount);
		setCurrency(currency);
	}

	/**
	 * Abs the money in the text. (Default is not abs).
	 */
	public void setAbsAmount(boolean isAbsAmount) {
		mIsAbsAmount = isAbsAmount;
		setValue(getText().toString());
	}

	/**
	 * no decimal in the text. (Default have decimal).
	 */
	public void setNoDecimal(boolean isNoDecimal) {
		mIsNoDecimal = isNoDecimal;
		setValue(getText().toString());
	}

}
