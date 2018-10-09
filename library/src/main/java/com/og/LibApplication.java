package com.og;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.view.View;

import com.og.exception.CrashHandler;
import com.og.utils.FileUtils;

import java.util.Locale;

public class LibApplication extends Application{

	private static Handler sHandler;
	private static LibApplication sApplication;
	private static int sMyPid;
	private static int sMainTid;

	@Override
	public void onCreate() {
		super.onCreate();
		try{
			sHandler = new Handler();
			sApplication = this;
			sMyPid=android.os.Process.myPid();
			sMainTid=android.os.Process.myTid();
			setLanguage();
			CrashHandler.getInstance().init(getApplicationContext()); //在Appliction里面设置我们的异常处理器为UncaughtExceptionHandler处理器;
		}catch (Exception e){
			FileUtils.addErrorLog(e);
		}
	}

	public static Handler getHandler() {
		return sHandler;
	}

	public static Context getContext(){
		return sApplication;
	}

	public static int getMyPid() {
		return sMyPid;
	}

	public static int getMainTid(){
		return sMainTid;
	}

	private void setLanguage() {
		try {
			SharedPreferences ls = getSharedPreferences("language_status", Context.MODE_PRIVATE);
			int id = ls.getInt("id", 0);
			Configuration configuration = getResources().getConfiguration();
			if (id == 0){
				Locale locale = getResources().getConfiguration().locale;
				String language = locale.getLanguage();
				switch (language) {
					case "en":
						configuration.setLocale(new Locale("en", "US"));
						break;
					case "zh":
						configuration.setLocale(Locale.CHINESE);
						break;
					case "km":
						configuration.setLocale(new Locale("km", "KH"));
						break;
					case "vi":
						configuration.setLocale(new Locale("vi", "VN"));
						break;
					default:
						//应用没有支持系统的语言包时默认用英语
						configuration.setLocale(new Locale("en", "US"));
						break;
				}
			}else {
				switch (id) {

					case 1:
						configuration.setLocale(new Locale("en", "US"));
						break;
					case 2:
						configuration.setLocale(Locale.CHINESE);
						break;
					case 3:
						configuration.setLocale(new Locale("km", "KH"));
						break;
					case 4:
						configuration.setLocale(new Locale("vi", "VN"));
						break;
				}
			}

			getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
