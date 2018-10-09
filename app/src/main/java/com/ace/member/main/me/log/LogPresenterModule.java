package com.ace.member.main.me.log;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module
public class LogPresenterModule {
	private LogContract.LogView mLogView;
	private final Context mContext;

	public LogPresenterModule(LogContract.LogView mLogView, Context mContext){
		this.mLogView = mLogView;
		this.mContext = mContext;
	}

	@Provides
	LogContract.LogView provideLogContractView() { return mLogView; }

	@Provides
	Context providesContext() {return mContext; }
}
