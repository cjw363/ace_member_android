package com.ace.member.main.home.top_up.recent_order;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class RecentOrderPresenterModule {

	private final RecentOrderContract.View mView;
	private final Context mContext;

	public RecentOrderPresenterModule(RecentOrderContract.View view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	RecentOrderContract.View provideRecentOrderContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
