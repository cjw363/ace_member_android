package com.ace.member.main.home;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class HomePresenterModule {

	private final HomeContract.HomeView mHomeView;
	private final Context mContext;

	public HomePresenterModule(HomeContract.HomeView view, Context context) {
		mHomeView = view;
		mContext = context;
	}

	@Provides
	HomeContract.HomeView provideHomeContractView() {
		return mHomeView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
