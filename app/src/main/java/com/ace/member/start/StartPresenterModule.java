package com.ace.member.start;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class StartPresenterModule {

	private final StartContract.StartView mView;
	private final Context mContext;

	public StartPresenterModule(StartContract.StartView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	StartContract.StartView provideStartContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
