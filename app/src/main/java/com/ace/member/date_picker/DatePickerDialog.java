package com.ace.member.date_picker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.ace.member.R;
import com.ace.member.listener.LoopScrollListener;
import com.ace.member.view.LoopView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DatePickerDialog extends BottomSheetDialog implements View.OnClickListener {

	private int mMinYear;
	private int mMaxYear;
	private LoopView mPickerDay;
	private LoopView mPickerMonth;
	private LoopView mPickerYear;

	private IDateSelectListener mDateSelectListener;
	private List<String> mYearList;
	private List<String> mMonthList;
	private List<String> mDayList;

	public DatePickerDialog(@NonNull Context context) {
		super(context);
		init();
	}

	public DatePickerDialog(@NonNull Context context, @StyleRes int theme) {
		super(context, theme);
		init();
	}

	protected DatePickerDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public DatePickerDialog setDateSelectListener(IDateSelectListener dateSelectListener) {
		mDateSelectListener = dateSelectListener;
		return this;
	}

	public DatePickerDialog setMinYear(int minYear) {
		if (minYear <= 1900) minYear = 1900;
		mMinYear = minYear;
		return this;
	}

	private void init() {
		mMinYear = 1900;
		mMaxYear = Calendar.getInstance()
			.get(Calendar.YEAR);
		View view = LayoutInflater.from(getContext())
			.inflate(R.layout.dlg_date_picker, null, false);
		FrameLayout flClose = (FrameLayout) view.findViewById(R.id.fl_close);
		FrameLayout flConfirm = (FrameLayout) view.findViewById(R.id.fl_confirm);
		mPickerYear = (LoopView) view.findViewById(R.id.picker_year);
		mPickerMonth = (LoopView) view.findViewById(R.id.picker_month);
		mPickerDay = (LoopView) view.findViewById(R.id.picker_day);

		setContentView(view);
		setCancelable(false);
		setCanceledOnTouchOutside(true);

		flClose.setOnClickListener(this);
		flConfirm.setOnClickListener(this);

		mPickerYear.setClickable(false);
		mPickerMonth.setClickable(false);
		mPickerDay.setClickable(false);

		mPickerYear.setLoopListener(new LoopScrollListener() {
			@Override
			public void onItemSelect(int item) {
				initDayPickerView();
			}
		});

		mPickerMonth.setLoopListener(new LoopScrollListener() {
			@Override
			public void onItemSelect(int item) {
				initDayPickerView();
			}
		});

		initYearPickerView();
		initMonthPickerView();
		initDayPickerView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.fl_close:
				dismiss();
				break;
			case R.id.fl_confirm:
				dismiss();
				if (mDateSelectListener != null) mDateSelectListener.onDateSelect(getFullDate());
				break;
		}
	}

	private void initYearPickerView() {
		mYearList = new ArrayList<>();
		for (int i = mMinYear; i <= mMaxYear; i++) {
			mYearList.add(format2LenStr(i));
		}
		mPickerYear.setDataList(mYearList);
		int pos = (mMaxYear - mMinYear);
		mPickerYear.setInitPosition(pos);
	}

	private void initMonthPickerView() {
		mMonthList = new ArrayList<>();
		for (int j = 1; j <= 12; j++) {
			mMonthList.add(format2LenStr(j));
		}
		mPickerMonth.setDataList(mMonthList);
		mPickerMonth.setInitPosition(0);
	}

	private void initDayPickerView() {
		if (mDayList == null) mDayList = new ArrayList<>();
		else mDayList.clear();
		int days = getDayCount(mMinYear + mPickerYear.getSelectedItem(), mPickerMonth.getSelectedItem() + 1);
		for (int i = 1; i <= days; i++) {
			mDayList.add(format2LenStr(i));
		}
		mPickerDay.setDataList(mDayList);
		mPickerDay.setInitPosition(0);
	}

	private static String format2LenStr(int num) {
		return (num < 10) ? "0" + num : String.valueOf(num);
	}

	private String getFullDate() {
		int year = mMinYear + mPickerYear.getSelectedItem();
		int month = mPickerMonth.getSelectedItem() + 1;
		int day = mPickerDay.getSelectedItem() + 1;

		return String.format("%s-%s-%s", String.valueOf(year), format2LenStr(month), format2LenStr(day));
	}

	public interface IDateSelectListener {
		void onDateSelect(String date);
	}

	private int getDayCount(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, 1);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
}
