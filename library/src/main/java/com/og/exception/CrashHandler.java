package com.og.exception;

import android.content.Context;
import android.os.Process;

import com.og.utils.FileUtils;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

	private static CrashHandler instance;  //单例引用，这里我们做成单例的，因为我们一个应用程序里面只需要一个UncaughtExceptionHandler实例
	private Context mContext;
	private Thread.UncaughtExceptionHandler mCrashHandler;

	private CrashHandler(){}

	public synchronized static CrashHandler getInstance(){  //同步方法，以免单例多线程环境下出现异常
		if (instance == null){
			instance = new CrashHandler();
		}
		return instance;
	}

	public void init(Context context){  //初始化，把当前对象设置成UncaughtExceptionHandler处理器
		mContext = context.getApplicationContext();
		mCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {  //当有未处理的异常发生时，就会来到这里
		try{
			FileUtils.addErrorLog(ex);
		}catch(Exception e){
			e.printStackTrace();
		}

		//如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
		if (mCrashHandler != null) {
			mCrashHandler.uncaughtException(thread, ex);
		} else {
			Process.killProcess(Process.myPid());
		}
	}

}
