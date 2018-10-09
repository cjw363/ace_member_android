package com.ace.member.main;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class MainPresenterModule {

	private final MainContract.MainView mMainView;
	private final Context mContext;

	public MainPresenterModule(MainContract.MainView view, Context context) {
		mMainView = view;
		mContext = context;
	}

	@Provides
	MainContract.MainView provideMainContractView() {
		return mMainView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
