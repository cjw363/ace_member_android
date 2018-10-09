package com.datepicker.bizs.languages;

import android.content.Context;

import com.og.R;

/**
 * 语言对象抽象父类
 * DatePicker暂且支持中文和英文两种显示语言
 * 如果你需要定义更多的语言可以新建自己的语言类并继承Language重写其方法即可
 * 同时你需要在Language的单例方法{@link #getInstance(Context context)}的分支语句中添加自己的语言类判断
 * <p/>
 * The abstract of language.
 * The current language only two support chinese and english in DatePicker.
 * If you need more language you want,you can define your own language class and extends Language
 * override all method.
 * Also you must add a judge of your language in branching statement of single case method{@link #getInstance(Context context)}
 *
 * @author AigeStudio 2015-03-26
 */
public class DPLManager {
	private static DPLManager sLanguage;
	private static Context mContext;

	/**
	 * 获取日历语言管理器
	 * <p/>
	 * Get DatePicker language manager
	 *
	 * @return 日历语言管理器 DatePicker language manager
	 */
	public static DPLManager getInstance(Context context) {
		mContext = context;
		if (null == sLanguage) {
			sLanguage = new DPLManager();
		}
		return sLanguage;
	}

	/**
	 * 月份标题显示
	 * <p/>
	 * Titles of month
	 *
	 * @return 长度为12的月份标题数组 Array in 12 length of month titles
	 */
	public String[] titleMonth() {
		return new String[]{mContext.getResources().getString(R.string.january), mContext.getResources().getString(R.string.february), mContext.getResources().getString(R.string.march), mContext.getResources().getString(R.string.april), mContext.getResources().getString(R.string.may), mContext.getResources().getString(R.string.june), mContext.getResources().getString(R.string.july), mContext.getResources().getString(R.string.august), mContext.getResources().getString(R.string.september), mContext.getResources().getString(R.string.october), mContext.getResources().getString(R.string.november), mContext.getResources().getString(R.string.december)};
	}

	/**
	 * 确定按钮文本
	 * <p/>
	 * Text of ensure button
	 *
	 * @return Text of ensure button
	 */
	public String titleEnsure() {
		return "Ok";
	}


	/**
	 * 公元前文本
	 * <p/>
	 * Text of B.C.
	 *
	 * @return Text of B.C.
	 */
	public String titleBC() {
		return "B.C.";
	}

	/**
	 * 星期标题显示
	 * <p/>
	 * Titles of week
	 *
	 * @return 长度为7的星期标题数组 Array in 7 length of week titles
	 */
	public String[] titleWeek() {
		return new String[]{mContext.getResources().getString(R.string.mon), mContext.getResources().getString(R.string.tue), mContext.getResources().getString(R.string.wed), mContext.getResources().getString(R.string.thu), mContext.getResources().getString(R.string.fri), mContext.getResources().getString(R.string.set), mContext.getResources().getString(R.string.sun)};
	}
}
