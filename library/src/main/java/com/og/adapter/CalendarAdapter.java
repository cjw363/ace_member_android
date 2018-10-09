package com.og.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.og.R;
import com.og.utils.FileUtils;
import com.og.utils.SpecialCalendar;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarAdapter extends BaseAdapter {

	private boolean isLeapYear = false;//是否为闰年
	private int daysOfMonth = 0;//某月的天数
	private int dayOfWeek = 0;//具体某一天是星期几
	private int lastDaysOfMonth = 0;//上个月的总天数
	private Context context;
	private String[] dayNumber = new String[42];//一个GridView中的日期存入此数组中

	private SpecialCalendar sc = null;
	private Resources res = null;
	private Drawable drawable = null;

	private String currentYear = "";
	private String currentMonth = "";
	private String currentDay = "";

	private int currentFlag = -1;//用于标记当天
	private int[] schDateTagFlag = null;//存储当月所有的日程日期

	private int showYear = 0;//用于在头部显示的年份
	private int showMonth = 0;//用于在头部显示的月份
	private String animalsYear = "";
	private String leapMonth = "";//闰哪一个月
	private String cyclical = "";//天干地支

	private TextView[] arrView = null;

	//系统当前时间
	private String sysDate = "";
	private String sysYear = "";
	private String sysMonth = "";
	private String sysDay = "";

	public CalendarAdapter() {
		Date date = new Date();
		@SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		sysDate = sdf.format(date);//当前日期
		String[] str = sysDate.split("-");
		sysYear = str[0];
		sysMonth = str[1];
		sysDay = str[2];
	}

	public void restartAdapter(int jumpMonth, int jumpYear, int year_c, int month_c, int day_c) {
		int stepYear = year_c + jumpYear;
		int stepMonth = month_c + jumpMonth;
		if (stepMonth > 0) {
			if (stepMonth % 12 == 0) {
				stepYear = stepYear + stepMonth / 12 - 1;
				stepMonth = 12;
			} else {
				stepYear = stepYear + stepMonth / 12;
				stepMonth = stepMonth % 12;
			}
		} else {
			stepYear = stepYear - 1 + stepMonth / 12;
			stepMonth = stepMonth % 12 + 12;
		}

		currentYear = String.valueOf(stepYear);
		currentMonth = String.valueOf(stepMonth);
		currentDay = String.valueOf(day_c);

		getCalender(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));
	}

	public CalendarAdapter(Context context, Resources rs, int jumpMonth, int jumpYear, int year_c, int month_c, int day_c) {
		this();
		this.context = context;
		sc = new SpecialCalendar();
		this.res = rs;

		int stepYear = year_c + jumpYear;
		int stepMonth = month_c + jumpMonth;
		if (stepMonth > 0) {
			if (stepMonth % 12 == 0) {
				stepYear = stepYear + stepMonth / 12 - 1;
				stepMonth = 12;
			} else {
				stepYear = stepYear + stepMonth / 12;
				stepMonth = stepMonth % 12;
			}
		} else {
			stepYear = stepYear - 1 + stepMonth / 12;
			stepMonth = stepMonth % 12 + 12;
		}

		currentYear = String.valueOf(stepYear);
		currentMonth = String.valueOf(stepMonth);
		currentDay = String.valueOf(day_c);

		getCalender(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));
	}

	public CalendarAdapter(Context context, Resources rs, int year, int month, int day) {
		this();
		this.context = context;
		sc = new SpecialCalendar();
		this.res = rs;
		currentYear = String.valueOf(year);
		//得到跳转到的年份
		currentMonth = String.valueOf(month);  //得到跳转到的月份
		currentDay = String.valueOf(day);  //得到跳转到的天

		getCalender(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));

	}

	public void setEveryDayNumber(JSONObject object) {
		if (arrView == null || object == null) return;
		try {
			int len = arrView.length;
			for (int i = 0; i < len; i++) {
				String str = object.has(String.valueOf(i + 1)) ? object.optString(String.valueOf(i + 1)) : "";
				((TextView) arrView[i]).setText(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dayNumber.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		try {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.calendar_item, null);
			}
			TextView textView = (TextView) convertView.findViewById(R.id.tv_date);
			LinearLayout llDate = (LinearLayout) convertView.findViewById(R.id.ll_date);
			String d = dayNumber[position];
			//		String dv = dayNumber[position].split("\\.")[1];
			TextView textNumber = (TextView) convertView.findViewById(R.id.tv_number);

			//		SpannableString sp = new SpannableString(d+"\n"+dv);
			SpannableString sp = new SpannableString(d);
			sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			sp.setSpan(new RelativeSizeSpan(1.2f), 0, d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			textView.setText(sp);
			textView.setTextColor(Color.GRAY);

			if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
				// 当前月信息显示
				textView.setTextColor(Color.BLACK);// 当月字体设黑
				if (arrView != null) arrView[position - dayOfWeek] = textNumber;

			}
			if (schDateTagFlag != null && schDateTagFlag.length > 0) {
				for (int aSchDateTagFlag : schDateTagFlag) {
					if (aSchDateTagFlag == position) {
						//设置日程标记背景
						//					textView.setBackgroundResource(R.drawable.mark);
					}
				}
			}
			if (currentFlag == position) {
				//设置当天的背景
				//			drawable = ContextCompat.getDrawable(context, R.drawable.current_day_bgc);
				llDate.setBackgroundResource(R.color.btn_pressed_color1);
				textView.setTextColor(Color.WHITE);
			}
		} catch (Exception e) {
			FileUtils.addErrorLog(e);
		}
		return convertView;
	}

	//得到某年的某月的天数且这月的第一天是星期几
	private void getCalender(int year, int month) {
		isLeapYear = sc.isLeapYear(year);//是否为闰年
		daysOfMonth = sc.getDaysOfMonth(isLeapYear, month);//某月的总天数
		dayOfWeek = sc.getWeekdayOfMonth(year, month);//某月第一天为星期几
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapYear, month - 1);//上一个月的总天数
		arrView = new TextView[daysOfMonth];
		getWeek(year, month);
	}

	//将一个月中的每一天的值添加入数组dayNuber中
	private void getWeek(int year, int month) {
		int j = 1;

		for (int i = 0; i < dayNumber.length; i++) {
			if (i < dayOfWeek) {
				int temp = lastDaysOfMonth - dayOfWeek + 1;
				dayNumber[i] = String.valueOf(temp + i);
			} else if (i < daysOfMonth + dayOfWeek) {
				String day = String.valueOf(i - dayOfWeek + 1);
				dayNumber[i] = String.valueOf(i - dayOfWeek + 1);
				if (sysYear.equals(String.valueOf(year)) && sysMonth.equals(String.valueOf(month)) && sysDay.equals(day)) {
					currentFlag = i;
				}
				setShowYear(year);
				setShowMonth(month);
			} else {
				dayNumber[i] = String.valueOf(j);
				j++;
			}
		}
	}

	public void matchScheduleDate(int year, int month, int day) {

	}

	/**
	 * 点击每一个item时返回item中的日期
	 *
	 * @param position int
	 * @return String
	 */
	public String getDateByClickItem(int position) {
		return dayNumber[position];
	}

	/**
	 * 在点击gridView时，得到这个月中第一天的位置
	 *
	 * @return int
	 */
	public int getStartPosition() {
		return dayOfWeek + 7;
	}

	public int getStartDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * 在点击gridView时，得到这个月中最后一天的位置
	 *
	 * @return int
	 */
	public int getEndPosition() {
		return (dayOfWeek + daysOfMonth + 7) - 1;
	}

	public int getShowYear() {
		return showYear;
	}

	public void setShowYear(int showYear) {
		this.showYear = showYear;
	}

	public int getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(int showMonth) {
		this.showMonth = showMonth;
	}

	public String getAnimalsYear() {
		return animalsYear;
	}

	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}

	public String getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(String leapMonth) {
		this.leapMonth = leapMonth;
	}

	public String getCyclical() {
		return cyclical;
	}

	public void setCyclical(String cyclical) {
		this.cyclical = cyclical;
	}
}
