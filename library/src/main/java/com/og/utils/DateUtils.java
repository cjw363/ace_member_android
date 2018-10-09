package com.og.utils;

import com.og.LibGlobal;
import com.og.R;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {

	private static Format getFormat() {
		return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	}

	public static String getToday() {
		Format f = getFormat();
		Calendar c = Calendar.getInstance();
		return f.format(c.getTime());
	}

	private static String getYesterday() {
		Format f = getFormat();
		return f.format(new Date(new Date().getTime() - LibGlobal.MILLISECOND_IN_DAY));
	}

	//This Week from
	private static String getMondayOFWeek() {
		Format f = getFormat();
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DAY_OF_MONTH, mondayPlus);
		return f.format(currentDate.getTime());
	}

	//This Week To
	private static String getCurrentWeekday() {
		Format f = getFormat();
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DAY_OF_MONTH, mondayPlus + 6);
		return f.format(currentDate.getTime());
	}

	//Last Week from
	private static String getPreviousWeekday() {
		Format f = getFormat();
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DAY_OF_MONTH, mondayPlus + 7 * -1);
		return f.format(currentDate.getTime());
	}

	//Last week to
	private static String getPreviousWeekSunday() {
		Format f = getFormat();
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DAY_OF_MONTH, mondayPlus - 1);
		return f.format(currentDate.getTime());
	}

	//this month from
	private static String getMonthDay() {
		Format f = getFormat();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return f.format(c.getTime());
	}

	//this month to
	private static String getMonthSunday() {
		Format f = getFormat();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return f.format(c.getTime());
	}

	//last month from
	private static String getPreviousMonthDay() {
		Format f = getFormat();
		Calendar c2 = (Calendar) Calendar.getInstance().clone();
		c2.add(Calendar.MONTH, -1);
		c2.set(Calendar.DAY_OF_MONTH, 1);
		return f.format(c2.getTime());
	}

	private static String getPreviousMonthSunday() {
		Format f = getFormat();
		Calendar c3 = (Calendar) Calendar.getInstance().clone();
		c3.set(Calendar.DAY_OF_MONTH, 0);
		c3.getTime();
		return f.format(c3.getTime());
	}

	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();

		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek == 1) {
			return 0;
		}
		return (1 - dayOfWeek);
	}

	public static String[] getDate(String data) {
		String[] str = new String[2];
		switch (data) {
			case "0":
				str[0] = getToday();
				str[1] = getToday();
				break;
			case "1":
				str[0] = getYesterday();
				str[1] = getYesterday();
				break;
			case "2":
				str[0] = getMondayOFWeek();
				str[1] = getCurrentWeekday();
				break;
			case "3":
				str[0] = getPreviousWeekday();
				str[1] = getPreviousWeekSunday();
				break;
			case "4":
				str[0] = getMonthDay();
				str[1] = getMonthSunday();
				break;
			case "5":
				str[0] = getPreviousMonthDay();
				str[1] = getPreviousMonthSunday();
				break;
		}
		return str;
	}

	//获得日期/时间格式器，该格式器具有默认语言环境的默认格式化风格
	public static String getFormatDataTime() {
		return DateFormat.getDateTimeInstance().format(new Date());
	}

	/**
	 * 比较两个日期的大小，日期格式为yyyy-MM-dd
	 *
	 * @param str1 the first date
	 * @param str2 the second date
	 * @return true <br/>false
	 */
	public static boolean isDateOneBigger(String str1, String str2) {
		boolean isBigger = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		try {
			Date dt1 = sdf.parse(str1);
			Date dt2 = sdf.parse(str2);
			if (dt1.getTime() > dt2.getTime()) {
				isBigger = true;
			} else if (dt1.getTime() < dt2.getTime()) {
				isBigger = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
		return isBigger;
	}

	/**
	 * 获取前n天日期、后n天日期
	 *
	 * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
	 */
	public static String getPreviousDate(int distanceDay) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date beginDate = new Date();
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
		Date endDate = null;
		try {
			endDate = dft.parse(dft.format(date.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
			FileUtils.addErrorLog(e);
		}
		return dft.format(endDate);
	}

	/**
	 * 转换英文月份
	 * 2017-Feb-05 ===> 2017-02-05
	 */
	private static String parseDateWithEnglishMonth(String year, String month, String day) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-d", Locale.ENGLISH);
			Date date = dateFormat.parse(year + "-" + month + "-" + day);
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			return dateFormat2.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 根据年月获取当月天数
	 * @return 天数
	 */
	public static int getDayOfMonth(String year, String month) {
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); //如果写成年月日的形式的话，要写小d，如："yyyy/MM/dd"
		try {
			rightNow.setTime(simpleDate.parse(parseDateWithEnglishMonth(year, month, "01")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
	}


	public static int getYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public static int getMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getDay() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 计算两个日期之间相差的天数
	 *
	 * @param smdate 较小的时间
	 * @param bdate  较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 计算两个日期之间相差的秒数
	 */
	public static long secsBetween(String smdate, String bdate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(smdate));
			long time1 = cal.getTimeInMillis();
			cal.setTime(sdf.parse(bdate));
			long time2 = cal.getTimeInMillis();
			return (time2 - time1) / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate) throws ParseException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(smdate));
			long time1 = cal.getTimeInMillis();
			cal.setTime(sdf.parse(bdate));
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);
			return Integer.parseInt(String.valueOf(between_days));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getYearMonth(String date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			int year=cal.get(Calendar.YEAR);
			int month=cal.get(Calendar.MONTH)+1;
			return String.valueOf(year)+"-"+(month<10?"0"+month:month);
		}catch (Exception e){
			e.printStackTrace();
			FileUtils.addErrorLog(e);
			return null;
		}
	}

	public static String getYearMonthDay(String date){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date));
			int year=cal.get(Calendar.YEAR);
			int month=cal.get(Calendar.MONTH)+1;
			int day=cal.get(Calendar.HOUR_OF_DAY);
			return String.valueOf(year)+"."+(month<10?"0"+month:month)+"."+(day<10?"0"+day:day);
		}catch (Exception e){
			e.printStackTrace();
			FileUtils.addErrorLog(e);
			return null;
		}
	}

	public static boolean compareMonth(String date1,String date2){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(date1));
			int year1=cal.get(Calendar.YEAR);
			int month1=cal.get(Calendar.MONTH);
			cal.setTime(sdf.parse(date2));
			int year2=cal.get(Calendar.YEAR);
			int month2=cal.get(Calendar.MONTH);
			return year1==year2 && month1==month2;
		}catch (Exception e){
			e.printStackTrace();
			FileUtils.addErrorLog(e);
			return false;
		}

	}

	/**
	 * 日期转星期
	 */
	public static String getWeekOfDate(String datetime) {
		try {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd ");
		int[] weekDays = { R.string.Sunday, R.string.Monday, R.string.Tuesday, R.string.Wednesday, R.string.Thursday, R.string.Friday, R.string.Saturday };
		Calendar cal = Calendar.getInstance(); // 获得一个日历
		Date date = f.parse(datetime);
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
		if (w < 0) w = 0;
		return Utils.getString(weekDays[w]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static boolean isExpired(String dateTime) {
		try {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			Date date=f.parse(dateTime);

			Calendar cal = Calendar.getInstance();
			int year= cal.get(Calendar.YEAR);
			int month=cal.get(Calendar.MONTH);
			int day=cal.get(Calendar.HOUR_OF_DAY);
			cal.setTime(date);
			return year<cal.get(Calendar.YEAR) || month<cal.get(Calendar.MONTH) || day<cal.get(Calendar.HOUR_OF_DAY) ;
		} catch (ParseException e) {
			e.printStackTrace();
			return true;
		}
	}
}
