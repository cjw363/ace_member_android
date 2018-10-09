package com.ace.member.gesture_unlock;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class UnlockPresenterModule {

	private final UnlockContract.UnlockView mUnlockView;
	private final Context mContext;

	public UnlockPresenterModule(UnlockContract.UnlockView view, Context context) {
		mUnlockView = view;
		mContext = context;
	}

	@Provides
	UnlockContract.UnlockView provideUnlockContractView() {
		return mUnlockView;
	}

	@Provides
	Context providesContext() {
		return mContext;
	}
}
