package com.ace.member.main.me;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class MePresenterModule {

	private final MeContract.MeView mView;
	private final Context mContext;

	MePresenterModule(MeContract.MeView view, Context context) {
		mView = view;
		mContext = context;
	}

	@Provides
	MeContract.MeView provideMeContractView() {
		return mView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
