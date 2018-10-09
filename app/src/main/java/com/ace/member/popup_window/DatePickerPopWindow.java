package com.ace.member.popup_window;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ace.member.R;
import com.ace.member.listener.LoopScrollListener;
import com.ace.member.utils.SnackBarUtil;
import com.ace.member.view.LoopView;
import com.og.LibGlobal;
import com.og.utils.DateUtils;
import com.og.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatePickerPopWindow extends PopupWindow implements OnClickListener {

	private FrameLayout mFlCanecl;
	private FrameLayout mFlConfirm;
	private LoopView yearLoopView;
	private LoopView monthLoopView;
	private LoopView dayLoopView;
	private View pickerContainerV;
	private View contentView;//root view

	private int minYear; // min year
	private int maxYear; // max year
	private int yearPos = 0;
	private int monthPos = 0;
	private int dayPos = 0;
	private Context mContext;
	private TextView mTvDateStart;
	private TextView mTvDateEnd;
	private String mDateStart;
	private String mDateEnd;
	private String mCurrentSelect;

	private AppCompatImageView mIvClear;
	private LinearLayout mLlCurrency;
	private ArrayList<String> mSearchList;
	private boolean mSearch;
	private String mInitialDateStart;
	private String mInitialDateEnd;
	private FrameLayout mFlDatePicker;
	private int mCurrencyWidth;


	public static class Builder {

		//Required
		private Context context;
		private final String mDateStart;
		private final String mDateEnd;
		private final String mCurrentSelect;
		private OnDatePickedListener listener;
		private ArrayList<String> searchList;
		private boolean mSearch = true;
		private int mTvCurrencyWidth = Utils.getDimenPx(R.dimen.margin60);

		public Builder(Context context, String dateStart, String dateEnd, String currentSelect, OnDatePickedListener listener) {
			this.context = context;
			this.mDateStart = dateStart;
			this.mDateEnd = dateEnd;
			this.mCurrentSelect = currentSelect;
			this.listener = listener;
		}

		//Option
		private boolean showDayMonthYear = false;
		private int minYear = LibGlobal.MIN_YEAR;
		private int maxYear = Calendar.getInstance().get(Calendar.YEAR) + 1;

		public Builder minYear(int minYear) {
			this.minYear = minYear;
			return this;
		}

		public Builder maxYear(int maxYear) {
			this.maxYear = maxYear;
			return this;
		}


		public Builder setSearch(boolean search) {
			this.mSearch = search;
			return this;
		}

		//设置选择条件
		public Builder setSearchList(ArrayList<String> searchList) {
			this.searchList = searchList;
			return this;
		}

		public Builder setCurrencyWidth(int width) {
			mTvCurrencyWidth = width;
			return this;
		}

		public DatePickerPopWindow build() {
			if (minYear > maxYear) {
				throw new IllegalArgumentException();
			}
			return new DatePickerPopWindow(this);
		}

		public Builder showDayMonthYear(boolean useDayMonthYear) {
			this.showDayMonthYear = useDayMonthYear;
			return this;
		}
	}

	private DatePickerPopWindow(Builder builder) {
		this.minYear = builder.minYear;
		this.maxYear = builder.maxYear;
		this.mContext = builder.context;
		this.mDateStart = builder.mDateStart;
		this.mDateEnd = builder.mDateEnd;
		this.mCurrentSelect = builder.mCurrentSelect;
		this.mListener = builder.listener;
		this.mSearchList = builder.searchList;
		this.mSearch = builder.mSearch;
		this.mCurrencyWidth = builder.mTvCurrencyWidth;
		this.mInitialDateStart = builder.mDateStart;
		this.mInitialDateEnd = builder.mDateEnd;
		initView();
	}

	private OnDatePickedListener mListener;

	private void initView() {

		contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_date_picker, null);
		mTvDateStart = (TextView) contentView.findViewById(R.id.tv_start);
		mTvDateEnd = (TextView) contentView.findViewById(R.id.tv_end);
		mLlCurrency = (LinearLayout) contentView.findViewById(R.id.ll_currency);
		mFlCanecl = (FrameLayout) contentView.findViewById(R.id.fl_cancel);
		mFlConfirm = (FrameLayout) contentView.findViewById(R.id.fl_confirm);
		mIvClear = (AppCompatImageView) contentView.findViewById(R.id.iv_clear);
		mFlDatePicker = (FrameLayout) contentView.findViewById(R.id.fl_date_picker);
		pickerContainerV = contentView.findViewById(R.id.container_picker);

		//set default date
		if (!TextUtils.isEmpty(mDateStart) && !TextUtils.isEmpty(mDateEnd)) {
			mTvDateStart.setText(mDateStart);
			mTvDateEnd.setText(mDateEnd);
		}

		if (mSearch) {
			if (mSearchList != null) {
				for (int i = 0; i < mSearchList.size(); i++) {
					TextView tvCurrency = new TextView(mContext);
					tvCurrency.setBackground(Utils.getDrawable(R.drawable.bg_exchange_currency));
					ColorStateList textColor = mContext.getResources()
							.getColorStateList(R.color.sel_currency_list);
					tvCurrency.setTextColor(textColor);
					int padding = Utils.getDimenPx(R.dimen.padding5);
					tvCurrency.setPadding(padding, padding, padding, padding);
					tvCurrency.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.getDimenPx(R.dimen.txtSize18));
					tvCurrency.setText(mSearchList.get(i));
					tvCurrency.setGravity(Gravity.CENTER);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mCurrencyWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
					if (i != mSearchList.size() - 1) {
						int margin = Utils.getDimenPx(R.dimen.margin5);
						lp.setMargins(0, 0, margin, 0);
					}
					tvCurrency.setLayoutParams(lp);
					tvCurrency.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							v.setSelected(!v.isSelected());
							for (int j = 0; j < mSearchList.size(); j++) {
								if (mLlCurrency.getChildAt(j) == v) {
									v.setSelected(true);
								} else {
									mLlCurrency.getChildAt(j).setSelected(false);
								}
							}
						}
					});
					mLlCurrency.addView(tvCurrency);
				}
			}

			//设置上一次选择的 Currency
			if (!TextUtils.isEmpty(mCurrentSelect)) {
				for (int j = 0; j < mSearchList.size(); j++) {
					if (mCurrentSelect.equals(((TextView) mLlCurrency.getChildAt(j)).getText())) {
						mLlCurrency.getChildAt(j).callOnClick();
					}
				}
			} else {
				mLlCurrency.getChildAt(0).callOnClick();
			}
		}else{
			mLlCurrency.setVisibility(View.GONE);
		}

		//set checked listen
		mTvDateStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setSelected(true);
				mTvDateEnd.setSelected(false);
				initLoopViews(mFlDatePicker, mTvDateStart);
			}
		});
		mTvDateEnd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setSelected(true);
				mTvDateStart.setSelected(false);
				initLoopViews(mFlDatePicker, mTvDateEnd);
			}
		});

		mFlCanecl.setOnClickListener(this);
		mFlConfirm.setOnClickListener(this);
		mIvClear.setOnClickListener(this);
		//		contentView.setOnClickListener(this);

		setTouchable(true);
		setFocusable(true);
		// setOutsideTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setAnimationStyle(R.style.AnimBottom);
		setContentView(contentView);
		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
	}

	/**
	 * Init year and month loop view,
	 * Let the day loop view be handled separately
	 */
	private void initPickerViews() {
		List<String> yearList = new ArrayList<>();
		List<String> monthList = new ArrayList<>();

		int yearCount = maxYear - minYear;

		for (int i = 0; i < yearCount; i++) {
			yearList.add(format2LenStr(minYear + i));
		}

		for (int j = 0; j < 12; j++) {
			monthList.add(format2LenStr(j + 1));
		}

		yearLoopView.setDataList(yearList);
		yearLoopView.setInitPosition(yearPos);

		monthLoopView.setDataList(monthList);
		monthLoopView.setInitPosition(monthPos);
	}

	/**
	 * Init day item
	 */
	private void initDayPickerView() {

		int dayMaxInMonth;
		Calendar calendar = Calendar.getInstance();
		List<String> dayList = new ArrayList<>();

		calendar.set(Calendar.YEAR, minYear + yearPos);
		calendar.set(Calendar.MONTH, monthPos);

		//get max day in month
		dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		for (int i = 0; i < dayMaxInMonth; i++) {
			dayList.add(format2LenStr(i + 1));
		}

		dayLoopView.setDataList(dayList);
		dayLoopView.setInitPosition(dayPos);
	}

	/**
	 * set selected date position value when initView.
	 */
	private void setSelectedDate(String dateStr) {
		if (!TextUtils.isEmpty(dateStr)) {
			long milliseconds = getLongFromyyyyMMdd(dateStr);
			Calendar calendar = Calendar.getInstance(Locale.CHINA);

			if (milliseconds != -1) {
				calendar.setTimeInMillis(milliseconds);
				yearPos = calendar.get(Calendar.YEAR) - minYear;
				monthPos = calendar.get(Calendar.MONTH);
				dayPos = calendar.get(Calendar.DAY_OF_MONTH) - 1;
			}
		}
	}

	public void setCurrencyList(ArrayList<String> currencyList) {
		mSearchList = currencyList;
	}

	/**
	 * Show date picker popWindow
	 */
	public void showPopWin(final Activity activity) {

		if (null != activity) {
			setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
					lp.alpha = 1f;
					activity.getWindow().setAttributes(lp);
				}
			});

			showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

			//虚化背景
			WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
			lp.alpha = 0.7f;
			activity.getWindow().setAttributes(lp);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mFlCanecl) {
			dismiss();
		} else if (v == mFlConfirm) {

			String selectedCurrency = "";
			if(mSearch){
			for (int i = 0; i < mSearchList.size(); i++) {
				TextView tvCurrency = (TextView) mLlCurrency.getChildAt(i);
				if (tvCurrency.isSelected()) {
					selectedCurrency = tvCurrency.getText().toString();
				}
			}}
			if (null != mListener) {
				if (!mTvDateStart.getText().equals(Utils.getString(R.string.start)) && !mTvDateEnd.getText()
						.equals(Utils.getString(R.string.end_date))) {
					if (DateUtils.isDateOneBigger(mTvDateStart.getText().toString(), mTvDateEnd.getText()
							.toString())) {
						mListener.onDatePickCompleted(mTvDateEnd.getText().toString(), mTvDateStart.getText()
								.toString(), selectedCurrency);
					} else {
						mListener.onDatePickCompleted(mTvDateStart.getText().toString(), mTvDateEnd.getText()
								.toString(), selectedCurrency);
					}
					dismiss();
				} else {
					SnackBarUtil.show(getContentView(), R.string.please_select_a_valid_date);
				}
			}

		} else if (v == mIvClear) {
			mTvDateStart.setSelected(false);
			mTvDateEnd.setSelected(false);
			mTvDateStart.setText(mInitialDateStart);
			mTvDateEnd.setText(mInitialDateEnd);
			mFlDatePicker.removeAllViews();
			if(mSearch) mLlCurrency.getChildAt(0).callOnClick();
		}
	}

	private String getFullDate() {
		int year = minYear + yearPos;
		int month = monthPos + 1;
		int day = dayPos + 1;
		return String.format("%s-%s-%s", String.valueOf(year), format2LenStr(month), format2LenStr(day));
	}

	/**
	 * get long from yyyy-MM-dd
	 */
	private static long getLongFromyyyyMMdd(String date) {
		SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		Date parse = null;
		try {
			parse = mFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (parse != null) {
			return parse.getTime();
		} else {
			return -1;
		}
	}

	private static String getStrDate() {
		SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		return dd.format(new Date());
	}

	/**
	 * Transform int to String with prefix "0" if less than 10
	 */
	private static String format2LenStr(int num) {

		return (num < 10) ? "0" + num : String.valueOf(num);
	}

	public static int spToPx(Context context, int spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}


	public interface OnDatePickedListener {
		/**
		 * Listener when date has been checked
		 *
		 * @param dateStart yyyy-MM-dd
		 * @param dateEnd   yyyy-MM-dd
		 */
		void onDatePickCompleted(String dateStart, String dateEnd, String selectedCurrency);
	}

	private void initLoopViews(FrameLayout contentView, final TextView tvDate) {
		setSelectedDate(tvDate.getText().toString());
		LinearLayout llDatePicker = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.view_date_picker, null);
		contentView.removeAllViews();
		contentView.addView(llDatePicker);
		yearLoopView = (LoopView) llDatePicker.findViewById(R.id.picker_year);
		monthLoopView = (LoopView) llDatePicker.findViewById(R.id.picker_month);
		dayLoopView = (LoopView) llDatePicker.findViewById(R.id.picker_day);
		initPickerViews(); // init year and month loop view
		initDayPickerView(); //init day loop view

		yearLoopView.setLoopListener(new LoopScrollListener() {
			@Override
			public void onItemSelect(int item) {
				yearPos = item;
				initDayPickerView();
				tvDate.setText(getFullDate());
			}
		});

		monthLoopView.setLoopListener(new LoopScrollListener() {
			@Override
			public void onItemSelect(int item) {
				monthPos = item;
				initDayPickerView();
				tvDate.setText(getFullDate());
			}
		});

		dayLoopView.setLoopListener(new LoopScrollListener() {
			@Override
			public void onItemSelect(int item) {
				dayPos = item;
				tvDate.setText(getFullDate());
			}
		});
	}

}
